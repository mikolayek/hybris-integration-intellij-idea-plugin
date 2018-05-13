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

package com.intellij.idea.plugin.hybris.impex.file.actions;


import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.idea.plugin.hybris.actions.ActionUtils;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
public class ImpexFileCreateAction extends CreateFileFromTemplateAction implements DumbAware {

    public static final String FILE_TEMPLATE = "Impex File";


    private static final String NEW_IMPEX_FILE = "New Impex File";

    public ImpexFileCreateAction() {
        super(NEW_IMPEX_FILE, "", HybrisIcons.IMPEX_FILE);
    }

    @Override
    protected void buildDialog(
        final Project project,
        final PsiDirectory directory,
        final CreateFileFromTemplateDialog.Builder builder
    ) {
        builder.setTitle(NEW_IMPEX_FILE)
               .addKind("Empty file", HybrisIcons.IMPEX_FILE, FILE_TEMPLATE);
    }

    @Nullable
    @Override
    protected String getDefaultTemplateProperty() {
        return null;
    }

    @NotNull
    @Override
    protected String getActionName(PsiDirectory directory, String newName, String templateName) {
        return NEW_IMPEX_FILE;
    }


    @Override
    protected void postProcess(PsiFile createdElement, String templateName, Map<String, String> customProperties) {
        super.postProcess(createdElement, templateName, customProperties);
    }

    @Override
    protected boolean isAvailable(final DataContext dataContext) {
        return super.isAvailable(dataContext) && ActionUtils.isHybrisContext(dataContext);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ImpexFileCreateAction;
    }
}
