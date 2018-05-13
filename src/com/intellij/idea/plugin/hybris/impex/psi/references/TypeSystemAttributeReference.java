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

package com.intellij.idea.plugin.hybris.impex.psi.references;

import com.intellij.idea.plugin.hybris.impex.psi.ImpexAnyHeaderParameterName;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexFullHeaderType;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexHeaderLine;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexHeaderTypeName;
import com.intellij.idea.plugin.hybris.psi.references.TypeSystemReferenceBase;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaClass;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaModel;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaProperty;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaReference;
import com.intellij.idea.plugin.hybris.type.system.model.Attribute;
import com.intellij.idea.plugin.hybris.type.system.model.RelationElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 15/06/2016.
 */
class TypeSystemAttributeReference extends TypeSystemReferenceBase<ImpexAnyHeaderParameterName> {

    public TypeSystemAttributeReference(@NotNull final ImpexAnyHeaderParameterNameMixin owner) {
        super(owner);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(final boolean incompleteCode) {
        final TSMetaModel meta = getTypeSystemMeta();
        final String featureName = getElement().getText();
        final Optional<TSMetaClass> metaClass = findItemTypeReference()
            .map(PsiElement::getText)
            .map(meta::findMetaClassByName);

        if (!metaClass.isPresent()) {
            return ResolveResult.EMPTY_ARRAY;
        }

        final List<ResolveResult> result = metaClass.get()
                                                    .findPropertiesByName(featureName, true)
                                                    .stream()
                                                    .map(TSMetaProperty::retrieveDom)
                                                    .filter(Objects::nonNull)
                                                    .map(AttributeResolveResult::new)
                                                    .collect(Collectors.toCollection(LinkedList::new));

        metaClass.get().findReferenceEndsByRole(featureName, true)
                 .stream()
                 .map(TSMetaReference.ReferenceEnd::retrieveDom)
                 .filter(Objects::nonNull)
                 .map(RelationElementResolveResult::new)
                 .collect(Collectors.toCollection(() -> result));

        return result.toArray(new ResolveResult[result.size()]);
    }

    private Optional<ImpexHeaderTypeName> findItemTypeReference() {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(getElement(), ImpexHeaderLine.class))
                       .map(ImpexHeaderLine::getFullHeaderType)
                       .map(ImpexFullHeaderType::getHeaderTypeName);
    }

    private static class AttributeResolveResult implements TypeSystemResolveResult {

        private final Attribute myDomAttribute;

        public AttributeResolveResult(@NotNull final Attribute domAttribute) {
            myDomAttribute = domAttribute;
        }

        @Nullable
        @Override
        public PsiElement getElement() {
            return myDomAttribute.getQualifier().getXmlAttributeValue();
        }

        @Override
        public boolean isValidResult() {
            return getElement() != null;
        }

        @NotNull
        @Override
        public DomElement getSemanticDomElement() {
            return myDomAttribute;
        }
    }

    private static class RelationElementResolveResult implements TypeSystemResolveResult {

        @NotNull
        private final RelationElement myDomRelationEnd;

        public RelationElementResolveResult(@NotNull final RelationElement domRelationEnd) {
            myDomRelationEnd = domRelationEnd;
        }

        @Nullable
        @Override
        public PsiElement getElement() {
            return myDomRelationEnd.getQualifier().getXmlAttributeValue();
        }

        @Override
        public boolean isValidResult() {
            return getElement() != null;
        }

        @NotNull
        @Override
        public DomElement getSemanticDomElement() {
            return myDomRelationEnd;
        }
    }

}
