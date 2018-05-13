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

package com.intellij.idea.plugin.hybris.project;

import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.common.services.VirtualFileSystemService;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.idea.plugin.hybris.notifications.NotificationUtil;
import com.intellij.idea.plugin.hybris.project.configurators.AntConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.ConfiguratorFactory;
import com.intellij.idea.plugin.hybris.project.configurators.DataSourcesConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.MavenConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.impl.DefaultConfiguratorFactory;
import com.intellij.idea.plugin.hybris.project.descriptors.DefaultHybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.MavenModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.RootModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.tasks.ImportProjectProgressModalWindow;
import com.intellij.idea.plugin.hybris.project.tasks.SearchModulesRootsTaskModalWindow;
import com.intellij.idea.plugin.hybris.statistics.StatsCollector;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.ExtensionPoint;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.packaging.artifacts.ModifiableArtifactModel;
import com.intellij.util.containers.ContainerUtil;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.GuardedBy;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils.message;
import static com.intellij.idea.plugin.hybris.notifications.NotificationUtil.showSystemNotificationIfNotActive;

/**
 * Created 8:58 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultHybrisProjectImportBuilder extends AbstractHybrisProjectImportBuilder {

    private static final Logger LOG = Logger.getInstance(DefaultHybrisProjectImportBuilder.class);

    protected final Lock lock = new ReentrantLock();

    @Nullable
    @GuardedBy("lock")
    protected volatile HybrisProjectDescriptor hybrisProjectDescriptor;
    protected volatile boolean refresh;
    protected final VirtualFileSystemService virtualFileSystemService;
    private List<HybrisModuleDescriptor> moduleList;
    private List<HybrisModuleDescriptor> hybrisModulesToImport;

    public DefaultHybrisProjectImportBuilder(@NotNull final VirtualFileSystemService virtualFileSystemService) {
        Validate.notNull(virtualFileSystemService);

        this.virtualFileSystemService = virtualFileSystemService;
    }

    public ConfiguratorFactory getConfiguratorFactory() {
        if (!Extensions.getRootArea().hasExtensionPoint(HybrisConstants.CONFIGURATOR_FACTORY_ID)) {
            return ServiceManager.getService(DefaultConfiguratorFactory.class);
        }

        final ExtensionPoint ep = Extensions.getRootArea().getExtensionPoint(HybrisConstants.CONFIGURATOR_FACTORY_ID);
        final ConfiguratorFactory ultimateConfiguratorFactory = (ConfiguratorFactory) ep.getExtension();

        if (ultimateConfiguratorFactory != null) {
            return ultimateConfiguratorFactory;
        }

        return ServiceManager.getService(DefaultConfiguratorFactory.class);
    }

    @Nullable
    public Project createProject(String name, String path) {
        final Project project = super.createProject(name, path);
        getHybrisProjectDescriptor().setHybrisProject(project);
        return project;
    }

    @Override
    public void setRootProjectDirectory(@NotNull final File directory) {
        Validate.notNull(directory);

        ProgressManager.getInstance().run(new SearchModulesRootsTaskModalWindow(
            directory, this.getHybrisProjectDescriptor()
        ));

        this.setFileToImport(directory.getAbsolutePath());
    }

    @Override
    public void cleanup() {
        super.cleanup();

        this.lock.lock();

        try {
            this.hybrisProjectDescriptor = null;
        } finally {
            this.lock.unlock();
        }
        refresh = false;
    }

    @Override
    public void setRefresh(final boolean refresh) {
        this.refresh = refresh;
    }

    @NotNull
    @Override
    public HybrisProjectDescriptor getHybrisProjectDescriptor() {
        this.lock.lock();

        try {
            if (null == this.hybrisProjectDescriptor) {
                this.hybrisProjectDescriptor = new DefaultHybrisProjectDescriptor();
                this.hybrisProjectDescriptor.setProject(getCurrentProject());
            }

            //noinspection ConstantConditions
            return this.hybrisProjectDescriptor;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean isOpenProjectSettingsAfter() {
        return this.getHybrisProjectDescriptor().isOpenProjectSettingsAfterImport();
    }

    @Override
    public void setOpenProjectSettingsAfter(final boolean on) {
        this.getHybrisProjectDescriptor().setOpenProjectSettingsAfterImport(on);
    }

    @Nullable
    @Override
    public List<Module> commit(
        final Project project,
        final ModifiableModuleModel model,
        final ModulesProvider modulesProvider,
        final ModifiableArtifactModel artifactModel
    ) {

        final List<Module> modules = ContainerUtil.newSmartList();

        final HybrisProjectDescriptor hybrisProjectDescriptor = getHybrisProjectDescriptor();
        final List<HybrisModuleDescriptor> allModules = hybrisProjectDescriptor.getModulesChosenForImport();
        if (allModules.isEmpty()) {
            return Collections.emptyList();
        }
        final ConfiguratorFactory configuratorFactory = this.getConfiguratorFactory();

        this.performProjectsCleanup(allModules);
        this.collectStatistics(hybrisProjectDescriptor);

        new ImportProjectProgressModalWindow(
            project, model, configuratorFactory, hybrisProjectDescriptor, modules
        ).queue();

        final boolean[] finished = {false};

        StartupManager.getInstance(project).runWhenProjectIsInitialized(() -> {
            finished[0] = true;

            finishImport(
                project,
                hybrisProjectDescriptor,
                allModules,
                configuratorFactory,
                () -> notifyImportFinished(project)
            );
        });

        if (!finished[0]) {
            notifyImportNotFinishedYet(project);
        }
        return modules;
    }

    private static void finishImport(
        @NotNull final Project project,
        @NotNull final HybrisProjectDescriptor hybrisProjectDescriptor,
        @NotNull final List<HybrisModuleDescriptor> allHybrisModules,
        @NotNull final ConfiguratorFactory configuratorFactory,
        @NotNull final Runnable callback
    ) {
        try {
            final AntConfigurator antConfigurator = configuratorFactory.getAntConfigurator();

            if (antConfigurator != null) {
                antConfigurator.configure(hybrisProjectDescriptor, allHybrisModules, project);
            }
        } catch (Exception e) {
            LOG.error("Can not configure Ant due to an error.", e);
        }
        final DataSourcesConfigurator dataSourcesConfigurator = configuratorFactory.getDataSourcesConfigurator();

        if (dataSourcesConfigurator != null) {
            try {
                dataSourcesConfigurator.configure(project);
            } catch (Exception e) {
                LOG.error("Can not import data sources due to an error.", e);
            }
        }
        // invokeLater is needed to avoid a problem with transaction validation:
        // "Write-unsafe context!...", "Do not use API that changes roots from roots events..."
        ApplicationManager.getApplication().invokeLater(() -> {
            if (project.isDisposed()) {
                return;
            }
            final MavenConfigurator mavenConfigurator = configuratorFactory.getMavenConfigurator();

            if (mavenConfigurator != null) {
                try {
                    final List<MavenModuleDescriptor> mavenModules = hybrisProjectDescriptor
                        .getModulesChosenForImport()
                        .stream()
                        .filter(e -> e instanceof MavenModuleDescriptor)
                        .map(e -> (MavenModuleDescriptor) e)
                        .collect(Collectors.toList());

                    if (!mavenModules.isEmpty()) {
                        mavenConfigurator.configure(hybrisProjectDescriptor, project, mavenModules, configuratorFactory);
                    }
                } catch (Exception e) {
                    LOG.error("Can not import Maven modules due to an error.", e);
                } finally {
                    callback.run();
                }
            }
        });
    }

    private void notifyImportNotFinishedYet(@NotNull Project project) {

        final String notificationTitle = refresh
            ? message("project.refresh.notification.title")
            : message("project.import.notification.title");

        NotificationUtil.NOTIFICATION_GROUP.createNotification(
            notificationTitle,
            message("import.or.refresh.process.not.finished.yet"),
            NotificationType.INFORMATION,
            null
        ).notify(project);
    }

    private void notifyImportFinished(@NotNull Project project) {

        final String notificationName = refresh
            ? message("project.refresh.finished")
            : message("project.import.finished");

        final String notificationTitle = refresh
            ? message("project.refresh.notification.title")
            : message("project.import.notification.title");

        NotificationUtil.NOTIFICATION_GROUP.createNotification(
            notificationTitle,
            notificationName,
            NotificationType.INFORMATION,
            null
        ).notify(project);

        showSystemNotificationIfNotActive(project, notificationName, notificationTitle, notificationName);
    }

    private void collectStatistics(final HybrisProjectDescriptor hybrisProjectDescriptor) {
        try {
            final StringBuilder parameters = new StringBuilder();
            parameters.append("readOnly:");
            parameters.append(hybrisProjectDescriptor.isImportOotbModulesInReadOnlyMode());
            parameters.append(",customDirectoryOverride:");

            final boolean override = hybrisProjectDescriptor.getExternalExtensionsDirectory() != null;
            parameters.append(override);

            final boolean hasSources = hybrisProjectDescriptor.getSourceCodeFile() != null;
            parameters.append(",hasSources:");
            parameters.append(hasSources);

            StatsCollector.getInstance().collectStat(StatsCollector.ACTIONS.IMPORT_PROJECT, parameters.toString());
        } catch (Exception e) {
            // we do not care
        }
    }

    protected void performProjectsCleanup(@NotNull final Iterable<HybrisModuleDescriptor> modulesChosenForImport) {
        Validate.notNull(modulesChosenForImport);
        final List<File> alreadyExistingModuleFiles;
        final File dir = hybrisProjectDescriptor.getModulesFilesDirectory();
        if (dir != null && dir.isDirectory()) {
            alreadyExistingModuleFiles = getAllImlFiles(dir);
        } else {
            alreadyExistingModuleFiles = getModulesChosenForImportFiles(modulesChosenForImport);
        }
        Collections.sort(alreadyExistingModuleFiles);

        try {
            this.virtualFileSystemService.removeAllFiles(alreadyExistingModuleFiles);
        } catch (IOException e) {
            LOG.error("Can not remove old module files.", e);
        }
    }

    private List<File> getAllImlFiles(final File dir) {
        final List<File> imlFiles = Arrays.stream(dir.listFiles(
            e -> e.getName().endsWith(HybrisConstants.NEW_IDEA_MODULE_FILE_EXTENSION)
        )).collect(Collectors.toList());
        return imlFiles;
    }

    private List<File> getModulesChosenForImportFiles(final Iterable<HybrisModuleDescriptor> modulesChosenForImport) {
        List<File> alreadyExistingModuleFiles = new ArrayList<>();
        for (HybrisModuleDescriptor moduleDescriptor : modulesChosenForImport) {
            if (moduleDescriptor.getIdeaModuleFile().exists()) {
                alreadyExistingModuleFiles.add(moduleDescriptor.getIdeaModuleFile());
            }
        }
        return alreadyExistingModuleFiles;
    }

    @Override
    public String getName() {
        return message("hybris.project.name");
    }

    @Override
    public Icon getIcon() {
        return HybrisIcons.HYBRIS_ICON;
    }

    @Override
    public void setAllModuleList() {
        moduleList = this.getHybrisProjectDescriptor().getFoundModules();
    }

    @Override
    public void setCoreStepModuleList() {
        moduleList = this.getHybrisProjectDescriptor()
                         .getFoundModules()
                         .stream()
                         .filter(e -> !(e instanceof RootModuleDescriptor))
                         .collect(Collectors.toList());
    }

    @Override
    public void setExternalStepModuleList() {
        moduleList = this.getHybrisProjectDescriptor()
                         .getFoundModules()
                         .stream()
                         .filter(e -> e instanceof RootModuleDescriptor)
                         .collect(Collectors.toList());
    }

    @Override
    public void setHybrisModulesToImport(final List<HybrisModuleDescriptor> hybrisModules) {
        hybrisModulesToImport = hybrisModules;
        try {
            setList(hybrisModules);
        } catch (ConfigurationException e) {
            LOG.error(e);
            // no-op already validated
        }
    }

    @Override
    public List<HybrisModuleDescriptor> getHybrisModulesToImport() {
        return hybrisModulesToImport;
    }

    @Override
    public List<HybrisModuleDescriptor> getList() {
        if (moduleList == null) {
            setAllModuleList();
        }
        return moduleList;
    }

    @Override
    public void setList(final List<HybrisModuleDescriptor> list) throws ConfigurationException {

        final List<HybrisModuleDescriptor> chosenForImport = new ArrayList<HybrisModuleDescriptor>(list);

        chosenForImport.removeAll(this.getHybrisProjectDescriptor().getAlreadyOpenedModules());

        this.getHybrisProjectDescriptor().setModulesChosenForImport(chosenForImport);
    }

    @Override
    public boolean isMarked(final HybrisModuleDescriptor element) {
        return false;
    }

}
