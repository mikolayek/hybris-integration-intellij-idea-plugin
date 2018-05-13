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

package com.intellij.idea.plugin.hybris.project.configurators.impl;

import com.intellij.ide.actions.ImportModuleAction;
import com.intellij.ide.util.newProjectWizard.AddModuleWizard;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.project.configurators.GradleConfigurator;
import com.intellij.idea.plugin.hybris.project.descriptors.GradleModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptorType;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisProjectDescriptor;
import com.intellij.openapi.application.AccessToken;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.externalSystem.model.ExternalSystemDataKeys;
import com.intellij.openapi.externalSystem.service.project.ProjectDataManager;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.gradle.service.project.wizard.GradleProjectImportBuilder;
import org.jetbrains.plugins.gradle.service.project.wizard.GradleProjectImportProvider;
import org.jetbrains.plugins.gradle.settings.GradleProjectSettings;

import java.util.List;

public class DefaultGradleConfigurator implements GradleConfigurator {

    @Override
    public void configure(
        @NotNull final HybrisProjectDescriptor hybrisProjectDescriptor,
        @NotNull final Project project,
        @NotNull final List<GradleModuleDescriptor> gradleModules,
        @Nullable final String[] gradleRootGroup
    ) {
        if (gradleModules.isEmpty()) {
            return;
        }
        final ProjectDataManager projectDataManager = ServiceManager.getService(ProjectDataManager.class);
        final GradleProjectImportBuilder gradleProjectImportBuilder = new GradleProjectImportBuilder(projectDataManager);
        final GradleProjectImportProvider gradleProjectImportProvider = new GradleProjectImportProvider(
            gradleProjectImportBuilder);

        gradleModules.forEach(gradleModule -> {
            ApplicationManager.getApplication().invokeAndWait(() -> {
                final AddModuleWizard wizard = new AddModuleWizard(
                    project,
                    gradleModule.getGradleFile().getPath(),
                    gradleProjectImportProvider
                );
                final GradleProjectSettings projectSettings = gradleProjectImportBuilder.getControl(project)
                                                                                        .getProjectSettings();
                projectSettings.setUseAutoImport(true);
                projectSettings.setCreateEmptyContentRootDirectories(false);
                if (wizard.getStepCount() > 0) {
                    final ModuleWizardStep step = wizard.getCurrentStepObject();
                    if (step.isStepVisible()) {
                        step.updateStep();
                        step.updateDataModel();
                    }
                }
                wizard.doFinishAction();
                final List<Module> newModules = ImportModuleAction.createFromWizard(project, wizard);
                if (gradleRootGroup != null && gradleRootGroup.length > 0) {
                    moveGradleModulesToGroup(project, newModules, gradleRootGroup);
                }
            });
        });

        project.putUserData(ExternalSystemDataKeys.NEWLY_CREATED_PROJECT, Boolean.TRUE);
    }

    private void moveGradleModulesToGroup(
        final Project project,
        final List<Module> gradleModules,
        final String[] gradleGroup
    ) {
        final ModifiableModuleModel modifiableModuleModel = ModuleManager.getInstance(project).getModifiableModel();

        for (Module module : gradleModules) {
            if (module == null) {
                // https://youtrack.jetbrains.com/issue/IDEA-177512
                continue;
            }
            module.setOption(HybrisConstants.DESCRIPTOR_TYPE, HybrisModuleDescriptorType.GRADLE.name());
            modifiableModuleModel.setModuleGroupPath(module, gradleGroup);
        }
        AccessToken token = null;
        try {
            token = ApplicationManager.getApplication().acquireWriteActionLock(getClass());
            modifiableModuleModel.commit();
        } finally {
            if (token != null) {
                token.finish();
            }
        }
    }
}
