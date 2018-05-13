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

import com.intellij.idea.plugin.hybris.impex.ImpexLexerAdapter;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isMacroNameDeclaration;
import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isMacroUsage;
import static com.intellij.psi.tree.TokenSet.create;
import static com.intellij.psi.tree.TokenSet.orSet;

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
public class ImpexFindUsagesProvider implements FindUsagesProvider {

    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(
            new ImpexLexerAdapter(),
            orSet(
                create(ImpexTypes.MACRO_NAME_DECLARATION),
                create(ImpexTypes.MACRO_DECLARATION),
                create(ImpexTypes.MACRO_USAGE)
            ),
            create(ImpexTypes.COMMENT),
            TokenSet.ANY
        );
    }

    @Override
    public boolean canFindUsagesFor(@NotNull final PsiElement psiElement) {
        return psiElement instanceof PsiNamedElement;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull final PsiElement psiElement) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull final PsiElement element) {
        if (isMacroNameDeclaration(element) || isMacroUsage(element)) {
            return "macros";
        }
        return "unknown";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull final PsiElement element) {
        return element.getText();
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull final PsiElement element, final boolean useFullName) {
        return element.getText();
    }
}
