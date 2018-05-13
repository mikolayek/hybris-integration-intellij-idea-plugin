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

package com.intellij.idea.plugin.hybris.tools.remote.action;

import com.intellij.idea.plugin.hybris.tools.remote.console.ExecuteHybrisConsole;
import com.intellij.idea.plugin.hybris.tools.remote.http.HybrisHacHttpClient;
import com.intellij.idea.plugin.hybris.tools.remote.http.impex.HybrisHttpResult;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
public class ValidateImpexAction extends AnAction implements DumbAware {

    @Override
    public void actionPerformed(final AnActionEvent e) {
        final Editor editor = CommonDataKeys.EDITOR.getData(e.getDataContext());
        if (editor != null) {
            final SelectionModel selectionModel = editor.getSelectionModel();
            final HybrisHacHttpClient client = HybrisHacHttpClient.getInstance(e.getProject());
            String content = selectionModel.getSelectedText();
            if (content == null || content.trim().isEmpty()) {
                content = editor.getDocument().getText();
            }
            final HybrisHttpResult hybrisHttpResult = client.validateImpex(e.getProject(), content);

            ExecuteHybrisConsole.getInstance().show(hybrisHttpResult, e.getProject());
        }
    }

    @Override
    public void update(final AnActionEvent e) {
        super.update(e);
        final VirtualFile file = e.getDataContext().getData(CommonDataKeys.VIRTUAL_FILE);
        final boolean enabled = file != null && file.getName().endsWith(".impex");
        e.getPresentation().setEnabledAndVisible(enabled);
    }
}
