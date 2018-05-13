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

package com.intellij.idea.plugin.hybris.project.wizard;

import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettingsComponent;
import com.intellij.idea.plugin.hybris.statistics.StatsCollector;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class PermissionToSendStatisticsDialog extends DialogWrapper {

    private JPanel rootPanel;
    private JCheckBox permissionToSendStatisticsCheckBox;
    private JTextPane permissionToSendStatisticsTextPane;
    private Project myProject;
    private static final Logger LOG = Logger.getInstance(PermissionToSendStatisticsDialog.class);

    public PermissionToSendStatisticsDialog(@Nullable final Project project) {
        super(project, false, DialogWrapper.IdeModalityType.PROJECT);
        myProject = project;
        setTitle(HybrisI18NBundleUtils.message("hybris.stats.permission.label"));
        if (StatsCollector.getInstance().isOpenCollectiveContributor()) {
            setCancelButtonText(HybrisI18NBundleUtils.message("hybris.stats.permission.no"));
        } else {
            setCancelButtonText(HybrisI18NBundleUtils.message("hybris.stats.permission.cancel"));
        }
        permissionToSendStatisticsCheckBox.addItemListener(e -> setOKActionEnabled(e.getStateChange() == ItemEvent.SELECTED));
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return rootPanel;
    }

    @Override
    protected void doOKAction() {
        HybrisApplicationSettingsComponent.getInstance().getState().setAllowedSendingPlainStatistics(true);
        super.doOKAction();
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
        if (StatsCollector.getInstance().isOpenCollectiveContributor()) {
            HybrisApplicationSettingsComponent.getInstance().getState().setDisallowedSendingStatistics(true);
            return;
        }
        ApplicationManager.getApplication().invokeLater(() -> ApplicationManager.getApplication().runWriteAction(() -> {
            LOG.error("User chose to close the project.");
            ProjectManager.getInstance().closeProject(myProject);
        }));

    }

}
