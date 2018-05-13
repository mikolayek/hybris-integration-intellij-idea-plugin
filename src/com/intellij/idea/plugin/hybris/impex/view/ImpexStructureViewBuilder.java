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

package com.intellij.idea.plugin.hybris.impex.view;

import com.intellij.ide.structureView.StructureView;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.impl.TemplateLanguageStructureViewBuilder;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexFile;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.psi.PsiFile;

import static com.intellij.ide.structureView.impl.StructureViewComposite.StructureViewDescriptor;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 6/3/17.
 */
public class ImpexStructureViewBuilder extends TemplateLanguageStructureViewBuilder implements StructureViewBuilder {

    private final ImpexFile psiFile;

    public ImpexStructureViewBuilder(final ImpexFile psiFile) {
        super(psiFile);
        this.psiFile = psiFile;
    }

    @Override
    protected StructureViewDescriptor createMainView(
        final FileEditor fileEditor, final PsiFile mainFile
    ) {
        final Editor editor = fileEditor instanceof TextEditor ? ((TextEditor) fileEditor).getEditor() : null;
        StructureViewModel model = new ImpexStructureViewModel(editor, psiFile);
        StructureView view = new ImpexStructureViewComponent(fileEditor, model, psiFile.getProject());
        StructureViewDescriptor descriptor = new StructureViewDescriptor(psiFile.getName(), view, null);
        return descriptor;
    }


}
