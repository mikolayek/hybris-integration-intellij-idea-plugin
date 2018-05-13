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

package com.intellij.idea.plugin.hybris.reference.contributor;

import com.intellij.idea.plugin.hybris.reference.provider.HybrisTransitionProcessReferenceProvider;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import org.jetbrains.annotations.NotNull;

import static com.intellij.idea.plugin.hybris.common.utils.PsiXmlUtils.tagAttributeValuePattern;

/**
 * @author Nosov Aleksandr
 */
public class HybrisProcessReferenceContributor extends PsiReferenceContributor {

    public static final String ITEMS_TYPE_FILE_NAME = "-process";

    @Override
    public void registerReferenceProviders(@NotNull final PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
            tagAttributeValuePattern("transition", "to", ITEMS_TYPE_FILE_NAME),
            new HybrisTransitionProcessReferenceProvider()
        );
        registrar.registerReferenceProvider(
            tagAttributeValuePattern("then", ITEMS_TYPE_FILE_NAME),
            new HybrisTransitionProcessReferenceProvider()
        );
    }


}
