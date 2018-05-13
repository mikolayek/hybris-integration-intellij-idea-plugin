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

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ex.ApplicationEx;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.projectImport.ProjectImportWizardStep;
import com.intellij.util.PlatformUtils;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 13/3/17.
 */
public class CheckRequiredPluginsStep extends ProjectImportWizardStep {

    private JPanel rootPanel;
    private JLabel warningLabel;
    private JTextPane descriptionTextPane;
    private JList notInstalledList;
    private JList notEnabledList;
    private JLabel notInstalledLablel;
    private JLabel notEnabledLabel;
    private JButton enableButton;
    private final Set<PluginId> notInstalledPlugins;
    private final Set<PluginId> notEnabledPlugins;
    private static final Logger LOG = Logger.getInstance(CheckRequiredPluginsStep.class);

    private final List<String> ULTIMATE_EDITION_ONLY = Arrays.asList(
        "com.intellij.spring",
        "com.intellij.javaee",
        "com.intellij.diagram"
    );
    private final String EXCLUDED_ID_PREFIX = "com.intellij.modules";


    public CheckRequiredPluginsStep(final WizardContext context) {
        super(context);
        notInstalledPlugins = new HashSet<>();
        notEnabledPlugins = new HashSet<>();
    }

    @Override
    public JComponent getComponent() {
        return this.rootPanel;
    }

    @Override
    public void updateDataModel() {
    }

    @Override
    public boolean isStepVisible() {
        notInstalledPlugins.clear();
        notEnabledPlugins.clear();
        checkDependentPlugins();
        boolean missing = isAnyMissing();
        if (!missing) {
            return false;
        }
        fillInGUI();
        return true;
    }

    private void checkDependentPlugins() {
        final IdeaPluginDescriptor hybrisPlugin = PluginManager.getPlugin(PluginId.getId(HybrisConstants.PLUGIN_ID));
        final PluginId[] dependentPluginIds = hybrisPlugin.getOptionalDependentPluginIds();
        Arrays.stream(dependentPluginIds).forEach(id -> {
            if (id.getIdString().startsWith(EXCLUDED_ID_PREFIX)) {
                return;
            }
            final boolean installed = PluginManager.isPluginInstalled(id);
            if (!installed) {
                notInstalledPlugins.add(id);
                return;
            }
            final IdeaPluginDescriptor plugin = PluginManager.getPlugin(id);
            if (!plugin.isEnabled()) {
                notEnabledPlugins.add(id);
            }
        });
    }

    private void fillInGUI() {
        final DefaultListModel notInstalledModel = (DefaultListModel) notInstalledList.getModel();
        notInstalledModel.clear();
        notInstalledPlugins.stream().forEach(id -> notInstalledModel.addElement(id));
        final DefaultListModel notEnabledModel = (DefaultListModel) notEnabledList.getModel();
        notEnabledModel.clear();
        notEnabledPlugins.stream().forEach(id -> {
            final IdeaPluginDescriptor plugin = PluginManager.getPlugin(id);
            notEnabledModel.addElement(plugin.getName());
        });
    }

    public boolean isAnyMissing() {
        if (!notEnabledPlugins.isEmpty()) {
            return true;
        }
        if (PlatformUtils.isIdeaUltimate()) {
            return !notInstalledPlugins.isEmpty();
        }
        for (PluginId pluginId : notInstalledPlugins) {
            if (!ULTIMATE_EDITION_ONLY.contains(pluginId.getIdString())) {
                return true;
            }
        }
        return false;
    }

    private void createUIComponents() {
        notInstalledList = new JList(new DefaultListModel());
        notEnabledList = new JList(new DefaultListModel());
        enableButton = new JButton();
        enableButton.addActionListener(e -> enablePlugins());
    }

    private void enablePlugins() {
        notEnabledPlugins.stream().forEach(id -> {
            PluginManager.enablePlugin(id.getIdString());
        });
        final ApplicationEx app = (ApplicationEx) ApplicationManager.getApplication();
        app.restart(true);
    }

}
