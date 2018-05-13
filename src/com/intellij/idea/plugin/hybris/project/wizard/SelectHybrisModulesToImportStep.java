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

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.ElementsChooser;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.idea.plugin.hybris.project.descriptors.ConfigHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.CustomHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.ExtHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.PlatformHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettings;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor.IMPORT_STATUS.MANDATORY;
import static com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor.IMPORT_STATUS.UNUSED;

public class SelectHybrisModulesToImportStep extends AbstractSelectModulesToImportStep implements NonGuiSupport {

    private HybrisModuleDescriptor.IMPORT_STATUS selectionMode = MANDATORY;

    public SelectHybrisModulesToImportStep(final WizardContext context) {
        super(context);
    }

    @Override
    protected void init() {
        this.fileChooser.addElementsMarkListener((ElementsChooser.ElementsMarkListener<HybrisModuleDescriptor>) (element, isMarked) -> {
            if (isMarked) {
                for (HybrisModuleDescriptor moduleDescriptor : element.getDependenciesPlainList()) {
                    if (fileChooser.isElementMarked(moduleDescriptor)) {
                        continue;
                    }

                    fileChooser.setElementMarked(moduleDescriptor, true);
                    if (selectionMode == MANDATORY) {
                        moduleDescriptor.setImportStatus(MANDATORY);
                    }
                }
            }
            fileChooser.repaint();
        });
    }

    @Override
    public void updateStep() {
        getContext().setCoreStepModuleList();
        super.updateStep();
        selectionMode = MANDATORY;
        for (int index = 0; index < fileChooser.getElementCount(); index++) {
            final HybrisModuleDescriptor hybrisModuleDescriptor = fileChooser.getElementAt(index);
            if (hybrisModuleDescriptor.isPreselected()) {
                fileChooser.setElementMarked(hybrisModuleDescriptor, true);
                hybrisModuleDescriptor.setImportStatus(MANDATORY);
            }
        }
        selectionMode = UNUSED;
    }

    protected void setList(final List<HybrisModuleDescriptor> hybrisModules) {
        getContext().setHybrisModulesToImport(hybrisModules);
    }

    @Override
    public void nonGuiModeImport(final HybrisProjectSettings settings) throws ConfigurationException {
        selectionMode = MANDATORY;
        getContext().setAllModuleList();
        final List<HybrisModuleDescriptor> moduleToImport = new ArrayList<>();
        final Set<HybrisModuleDescriptor> moduleToCheck = new HashSet<>();
        for (HybrisModuleDescriptor hybrisModuleDescriptor : getContext().getList()) {
            if (hybrisModuleDescriptor.isPreselected()) {
                moduleToImport.add(hybrisModuleDescriptor);
                hybrisModuleDescriptor.setImportStatus(selectionMode);
                moduleToCheck.add(hybrisModuleDescriptor);
            }
        }
        resolveDependency(moduleToImport, moduleToCheck);

        selectionMode = UNUSED;
        final Set<String> unusedExtensionNameSet = settings.getUnusedExtensions();
        getContext()
            .getList()
            .stream()
            .filter(e -> unusedExtensionNameSet.contains(e.getName()))
            .forEach(e -> {
                moduleToImport.add(e);
                e.setImportStatus(selectionMode);
                moduleToCheck.add(e);
            });
        resolveDependency(moduleToImport, moduleToCheck);

        final Set<String> modulesOnBlackList = settings.getModulesOnBlackList();
        final List<HybrisModuleDescriptor> filteredModuleToImport = moduleToImport
            .stream()
            .filter(e->!modulesOnBlackList.contains(e.getRelativePath()))
            .collect(Collectors.toList());
        try {
            this.getContext().setList(filteredModuleToImport);
        } catch (ConfigurationException e) {
            // no-op already validated
        }
    }

    private void resolveDependency(
        final List<HybrisModuleDescriptor> moduleToImport,
        final Set<HybrisModuleDescriptor> moduleToCheck
    ) {
        while (!moduleToCheck.isEmpty()) {
            final HybrisModuleDescriptor currentModule = moduleToCheck.iterator().next();
            for (HybrisModuleDescriptor moduleDescriptor : currentModule.getDependenciesPlainList()) {
                if (!moduleToImport.contains(moduleDescriptor)) {
                    moduleToImport.add(moduleDescriptor);
                    moduleDescriptor.setImportStatus(selectionMode);
                    moduleToCheck.add(moduleDescriptor);
                }
            }
            moduleToCheck.remove(currentModule);
        }
    }

    @Override
    protected boolean isElementEnabled(HybrisModuleDescriptor hybrisModuleDescriptor) {
        if (hybrisModuleDescriptor instanceof ConfigHybrisModuleDescriptor) {
            if (hybrisModuleDescriptor.isPreselected()) {
                return false;
            }
        }
        if (hybrisModuleDescriptor instanceof PlatformHybrisModuleDescriptor) {
            return false;
        }
        if (hybrisModuleDescriptor instanceof ExtHybrisModuleDescriptor) {
            return false;
        }

        return super.isElementEnabled(hybrisModuleDescriptor);
    }

    @Override
    @Nullable
    protected Icon getElementIcon(final HybrisModuleDescriptor module) {
        if (this.isInConflict(module)) {
            return AllIcons.Actions.Cancel;
        }
        if (module instanceof CustomHybrisModuleDescriptor) {
            return AllIcons.Nodes.JavaModule;
        }
        if (module instanceof ConfigHybrisModuleDescriptor) {
            return AllIcons.Nodes.Module;
        }

        return HybrisIcons.HYBRIS_ICON;
    }
}