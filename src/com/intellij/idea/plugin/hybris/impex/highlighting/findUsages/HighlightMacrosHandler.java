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
import com.intellij.featureStatistics.ProductivityFeatureNames;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isMacroNameDeclaration;
import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isMacroUsage;

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
public class HighlightMacrosHandler extends HighlightUsagesHandlerBase<PsiElement> {

    private final PsiElement myTarget;

    public HighlightMacrosHandler(final Editor editor, final PsiFile file, final PsiElement target) {
        super(editor, file);
        myTarget = target;
    }

    @Override
    public List<PsiElement> getTargets() {
        return Collections.singletonList(myTarget);
    }

    @Override
    protected void selectTargets(final List<PsiElement> targets, final Consumer<List<PsiElement>> selectionConsumer) {
        selectionConsumer.consume(targets);
    }

    @Override
    public void computeUsages(final List<PsiElement> targets) {
        final PsiFile file = myTarget.getContainingFile();

        final PsiElement[] psiElements = PsiTreeUtil.collectElements(
            file,
            element -> isMacroNameDeclaration(element)
                       || isMacroUsage(element)
        );

        for (final PsiElement element : psiElements) {
            if (element.textMatches(myTarget)) {
                addOccurrence(element);
            }
        }
    }

    @Nullable
    @Override
    public String getFeatureId() {
        return ProductivityFeatureNames.CODEASSISTS_HIGHLIGHT_RETURN;
    }
}
