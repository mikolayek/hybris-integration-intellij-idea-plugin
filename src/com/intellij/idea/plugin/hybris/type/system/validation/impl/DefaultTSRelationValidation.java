/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.type.system.validation.impl;

import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.type.system.common.TSMessages;
import com.intellij.idea.plugin.hybris.type.system.model.Relation;
import com.intellij.idea.plugin.hybris.type.system.validation.TSRelationsValidation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vlad Bozhenok <vladbozhenok@gmail.com>
 */
public class DefaultTSRelationValidation implements TSRelationsValidation {

    @NotNull
    @Override
    public String validateRelations(
        @NotNull final Map<String, PsiClass> generatedClasses,
        @Nullable final List<Relation> relationsList
    ) {
        Validate.notNull(generatedClasses);

        if (null == relationsList) {
            return StringUtils.EMPTY;
        }

        final Map<String, PsiClass> filteredClasses = this.filterClassesWithRelations(generatedClasses, relationsList);

        for (final Relation relation : relationsList) {
            final String validationResult = this.validateRelation(relation, filteredClasses);
            if (StringUtils.isNotEmpty(validationResult)) {
                return validationResult;
            }
        }

        return StringUtils.EMPTY;
    }

    private String validateRelation(
        @NotNull final Relation relation,
        @NotNull final Map<String, PsiClass> filteredClasses
    ) {
        Validate.notNull(relation);
        Validate.notNull(filteredClasses);

        final String fieldNameInTarget = relation.getSourceElement().getQualifier().toString();
        final String targetClassName = relation.getTargetElement().getType().toString();

        if (this.isNotFieldExistInClass(filteredClasses, targetClassName, fieldNameInTarget)) {
            return HybrisI18NBundleUtils.message(
                TSMessages.ErrorMessages.RELATION_FIELDS_NOT_GENERATED,
                fieldNameInTarget,
                targetClassName
            );
        }

        final String fieldNameInSource = relation.getTargetElement().getQualifier().toString();
        final String sourceClassName = relation.getSourceElement().getType().toString();

        if (this.isNotFieldExistInClass(filteredClasses, sourceClassName, fieldNameInSource)) {
            return HybrisI18NBundleUtils.message(
                TSMessages.ErrorMessages.RELATION_FIELDS_NOT_GENERATED,
                fieldNameInSource,
                sourceClassName
            );
        }

        return StringUtils.EMPTY;
    }

    private boolean isNotFieldExistInClass(
        @NotNull final Map<String, PsiClass> filteredClasses,
        @NotNull final String className, @NotNull final String fieldName
    ) {

        Validate.notNull(filteredClasses);
        Validate.notNull(className);
        Validate.notNull(fieldName);

        final PsiClass classItem = filteredClasses.get(className);

        if (null == classItem) {
            return true;
        }

        for (final PsiField classField : classItem.getAllFields()) {
            if (StringUtils.endsWithIgnoreCase(classField.getName(), fieldName)) {
                return false;
            }
        }

        return true;
    }

    @NotNull
    private Map<String, PsiClass> filterClassesWithRelations(
        @NotNull final Map<String, PsiClass> generatedClasses,
        @NotNull final Collection<Relation> relationsList
    ) {

        final Map<String, PsiClass> filteredClasses = new HashMap<>();

        if (CollectionUtils.isEmpty(relationsList)) {
            return filteredClasses;
        }

        for (final Relation relation : relationsList) {

            this.addToSetIfExist(generatedClasses, filteredClasses, relation.getSourceElement().getType().toString());
            this.addToSetIfExist(generatedClasses, filteredClasses, relation.getTargetElement().getType().toString());

        }

        return filteredClasses;
    }

    private void addToSetIfExist(
        @NotNull final Map<String, PsiClass> generatedClasses,
        @NotNull final Map<String, PsiClass> mapToFill,
        @NotNull final String itemName
    ) {

        Validate.notNull(generatedClasses);
        Validate.notNull(mapToFill);
        Validate.notNull(itemName);

        final PsiClass psiClass = generatedClasses.get(itemName + HybrisConstants.MODEL_SUFFIX);

        if (null != psiClass && !mapToFill.containsKey(itemName)) {
            mapToFill.put(itemName, psiClass);
        }
    }
}
