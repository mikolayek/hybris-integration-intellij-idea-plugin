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

import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.project.configurators.ConfiguratorFactory;
import com.intellij.idea.plugin.hybris.project.configurators.MavenConfigurator;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptorType;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.MavenModuleDescriptor;
import com.intellij.openapi.application.AccessToken;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.model.MavenConstants;
import org.jetbrains.idea.maven.model.MavenExplicitProfiles;
import org.jetbrains.idea.maven.project.MavenImportListener;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.jetbrains.idea.maven.wizards.MavenProjectBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 13/11/16.
 */
public class DefaultMavenConfigurator implements MavenConfigurator {

    @Override
    public void configure(
        @NotNull final HybrisProjectDescriptor hybrisProjectDescriptor,
        @NotNull final Project project,
        @NotNull final List<MavenModuleDescriptor> mavenModules,
        @NotNull final ConfiguratorFactory configuratorFactory
    ) {
        final MavenProjectBuilder mavenProjectBuilder = new MavenProjectBuilder();
        final List<VirtualFile> pomList = mavenModules
            .stream()
            .map(e -> new File(e.getRootDirectory(), MavenConstants.POM_XML))
            .map(e -> VfsUtil.findFileByIoFile(e, true))
            .collect(Collectors.toList());
        mavenProjectBuilder.setFiles(pomList);

        if (!mavenProjectBuilder.setSelectedProfiles(MavenExplicitProfiles.NONE)) {
            return;
        }

        List<MavenProject> selectedProjects = new ArrayList<>();
        for (MavenProject mavenProject : mavenProjectBuilder.getList()) {
            final Optional<MavenModuleDescriptor> isPresent = mavenModules
                .stream()
                .filter(e -> e.getRootDirectory().getAbsolutePath().equals(mavenProject.getDirectory()))
                .findAny();
            if (isPresent.isPresent()) {
                selectedProjects.add(mavenProject);
            }
        }
        mavenProjectBuilder.setList(selectedProjects);

        project.getMessageBus().connect().subscribe(
            MavenImportListener.TOPIC,
            (importedProjects, newModules) -> {

                ApplicationManager.getApplication().invokeLater(() -> {
                    if (!project.isDisposed()) {
                        moveMavenModulesToCorrectGroup(
                            project,
                            mavenModules,
                            configuratorFactory,
                            pomList,
                            importedProjects,
                            newModules
                        );
                    }
                });
            }
        );
        mavenProjectBuilder.commit(project);
        MavenProjectsManager.getInstance(project).importProjects();
    }

    private void moveMavenModulesToCorrectGroup(
        final @NotNull Project project,
        final @NotNull List<MavenModuleDescriptor> mavenModules,
        final @NotNull ConfiguratorFactory configuratorFactory,
        final List<VirtualFile> pomList,
        final Collection<MavenProject> importedProjects,
        final List<Module> newModules
    ) {
        final List<MavenProject> importedProjectRoots = importedProjects
            .stream()
            .filter(e -> pomList
                .stream()
                .anyMatch(pom -> pom.equals(e.getFile()))
            )
            .collect(Collectors.toList());

        final List<Module> newRootModules = newModules
            .stream()
            .filter(e -> importedProjectRoots
                .stream()
                .anyMatch(i -> {
                    final String name = i.getName();
                    if (name != null) {
                        return i.getName().equals(e.getName());
                    }
                    return i.getFinalName().startsWith(e.getName());
                })
            )
            .collect(Collectors.toList());

        final String[] rootGroup = configuratorFactory.getGroupModuleConfigurator()
                                                      .getGroupName(mavenModules.get(0));

        if (rootGroup != null && rootGroup.length > 0) {
            moveMavenModulesToGroup(
                project,
                newRootModules,
                rootGroup
            );
        }
    }

    private void moveMavenModulesToGroup(
        final Project project,
        final List<Module> mavenModules,
        final String[] rootGroup
    ) {
        AccessToken token = null;
        final ModifiableModuleModel modifiableModuleModel;
        try {
            token = ApplicationManager.getApplication().acquireReadActionLock();
            modifiableModuleModel = ModuleManager.getInstance(project).getModifiableModel();

            for (Module module : mavenModules) {
                module.setOption(HybrisConstants.DESCRIPTOR_TYPE, HybrisModuleDescriptorType.MAVEN.name());
                final String[] groupPath = modifiableModuleModel.getModuleGroupPath(module);
                modifiableModuleModel.setModuleGroupPath(module, ArrayUtils.addAll(rootGroup, groupPath));
            }
        } finally {
            if (token != null) {
                token.finish();
            }
        }
        ApplicationManager.getApplication().invokeAndWait(() -> WriteAction.run(modifiableModuleModel::commit));
    }


}
