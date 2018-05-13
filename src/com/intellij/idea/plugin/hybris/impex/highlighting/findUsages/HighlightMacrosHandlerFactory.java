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

package com.intellij.idea.plugin.hybris.impex.highlighting.findUsages;

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase;
import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerFactoryBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isMacroNameDeclaration;
import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isMacroUsage;

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
public class HighlightMacrosHandlerFactory extends HighlightUsagesHandlerFactoryBase {

    @Override
    public HighlightUsagesHandlerBase createHighlightUsagesHandler(
        @NotNull final Editor editor,
        @NotNull final PsiFile file,
        @NotNull final PsiElement target
    ) {

        if (isMacroNameDeclaration(target)
            || isMacroUsage(target)) {
            return new HighlightMacrosHandler(editor, file, target);
        }

        return null;
    }
}
