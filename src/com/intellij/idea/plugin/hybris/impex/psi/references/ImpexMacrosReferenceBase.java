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

import com.intellij.idea.plugin.hybris.impex.psi.ImpexMacroDeclaration;
import com.intellij.idea.plugin.hybris.impex.rename.manipulator.ImpexMacrosManipulator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public class ImpexMacrosReferenceBase extends PsiReferenceBase.Poly<PsiElement> {

    protected static final Object[] NO_VARIANTS = new Object[0];

    public ImpexMacrosReferenceBase(@NotNull final PsiElement owner) {
        super(owner, false);
    }

    @Override
    public final TextRange getRangeInElement() {
        return TextRange.from(0, getElement().getTextLength());
    }

    @NotNull
    @Override
    public final Object[] getVariants() {
        return NO_VARIANTS;
    }

    @NotNull
    protected final Project getProject() {
        return getElement().getProject();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(final boolean incompleteCode) {
        final PsiFile originalFile = getElement().getContainingFile();

        final Collection<ImpexMacroDeclaration> macroDeclarations =
            PsiTreeUtil.findChildrenOfType(
                originalFile,
                ImpexMacroDeclaration.class
            );

        if (!macroDeclarations.isEmpty()) {
            final ArrayList<PsiElement> references = ContainerUtil.newArrayList();
            for (final ImpexMacroDeclaration declaration : macroDeclarations) {
                if (getElement().textMatches(declaration.getFirstChild())) {
                    references.add(declaration.getFirstChild());
                }
            }
            return PsiElementResolveResult.createResults(references);
        }
        return ResolveResult.EMPTY_ARRAY;
    }

    @Override
    public PsiElement handleElementRename(final String newElementName) throws IncorrectOperationException {
        return getManipulator().handleContentChange(myElement, getRangeInElement(), newElementName);
    }

    private ElementManipulator<PsiElement> getManipulator() {
        return new ImpexMacrosManipulator();
    }
}
