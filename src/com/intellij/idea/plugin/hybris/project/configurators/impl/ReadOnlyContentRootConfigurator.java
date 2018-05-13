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

import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.vfs.VfsUtil;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import static com.intellij.idea.plugin.hybris.common.HybrisConstants.ADDON_SRC_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.BACK_OFFICE_MODULE_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.CLASSES_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.COMMON_WEB_SRC_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.TEST_CLASSES_DIRECTORY;

/**
 * Created by Martin Zdarsky-Jones <zdary@zdary.cz> on 12/4/17.
 */
public class ReadOnlyContentRootConfigurator extends RegularContentRootConfigurator {

    protected void configureCommonRoots(
        @NotNull final HybrisModuleDescriptor moduleDescriptor,
        @NotNull final ContentEntry contentEntry,
        @NotNull final List<File> dirsToIgnore
    ) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        excludeCommonNeedlessDirs(contentEntry, moduleDescriptor);
    }

    protected void configureAdditionalRoots(
        @NotNull final HybrisModuleDescriptor moduleDescriptor,
        @NotNull final String directoryName,
        @NotNull final ContentEntry contentEntry,
        @NotNull final File parentDirectory
    ) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(directoryName);
        Validate.notNull(contentEntry);
        Validate.notNull(parentDirectory);

        final File additionalModuleDirectory = new File(parentDirectory, directoryName);
        if (!additionalModuleDirectory.exists() || additionalModuleDirectory.isFile()) {
            return;
        }

        final File additionalClassesDirectory = new File(additionalModuleDirectory, CLASSES_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(additionalClassesDirectory.getAbsolutePath())
        );
    }

    protected void configureBackOfficeRoots(
        @NotNull final HybrisModuleDescriptor moduleDescriptor,
        @NotNull final ContentEntry contentEntry,
        @NotNull final List<File> dirsToIgnore
    ) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        final File backOfficeModuleDirectory = new File(
            moduleDescriptor.getRootDirectory(), BACK_OFFICE_MODULE_DIRECTORY
        );

        final File hmcClassesDirectory = new File(backOfficeModuleDirectory, CLASSES_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(hmcClassesDirectory.getAbsolutePath())
        );
    }

    protected void configureWebModuleRoots(
        @NotNull final HybrisModuleDescriptor moduleDescriptor,
        final @NotNull ContentEntry contentEntry,
        @NotNull final File webModuleDirectory,
        @NotNull final List<File> dirsToIgnore
    ) {
        Validate.notNull(moduleDescriptor);

        final File webAddonSrcDirectory = new File(webModuleDirectory, ADDON_SRC_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(webAddonSrcDirectory.getAbsolutePath())
        );

        final File webTestClassesDirectory = new File(webModuleDirectory, TEST_CLASSES_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(webTestClassesDirectory.getAbsolutePath())
        );

        final File commonWebSrcDirectory = new File(webModuleDirectory, COMMON_WEB_SRC_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(commonWebSrcDirectory.getAbsolutePath())
        );
    }
}
