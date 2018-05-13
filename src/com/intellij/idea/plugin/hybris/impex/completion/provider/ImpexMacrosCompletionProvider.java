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

package com.intellij.idea.plugin.hybris.impex.completion.provider;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexMacroDeclaration;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public class ImpexMacrosCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(
        @NotNull final CompletionParameters parameters,
        final ProcessingContext context,
        @NotNull final CompletionResultSet result
    ) {
        final PsiFile originalFile = parameters.getOriginalFile();

        final Collection<ImpexMacroDeclaration> macroDeclarations = PsiTreeUtil.findChildrenOfType(
            originalFile, ImpexMacroDeclaration.class
        );

        if (macroDeclarations.isEmpty()) {
            return;
        }

        for (final ImpexMacroDeclaration macroDeclaration : macroDeclarations) {
            final PsiElement declaration = macroDeclaration.getFirstChild();
            result.addElement(LookupElementBuilder.create(declaration.getText())
                                                  .withIcon(HybrisIcons.MACROS));
        }
    }
}
