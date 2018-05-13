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

package com.intellij.idea.plugin.hybris.impex.find.findUsages;

import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.FindUsagesHandlerFactory;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isMacroNameDeclaration;
import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isMacroUsage;

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
public class MacrosFindUsagesHandlerFactory extends FindUsagesHandlerFactory {

    @Override
    public boolean canFindUsages(@NotNull final PsiElement element) {
        return isMacroNameDeclaration(element) || isMacroUsage(element);
    }

    @Nullable
    @Override
    public FindUsagesHandler createFindUsagesHandler(
        @NotNull final PsiElement element, final boolean forHighlightUsages
    ) {
        if (canFindUsages(element)) {
            return new FindUsagesHandler(element) {

            };
        }
        return null;

    }
}
