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

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.projectImport.ProjectOpenProcessorBase;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created 8:57 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectOpenProcessor extends ProjectOpenProcessorBase<DefaultHybrisProjectImportBuilder> {

    private static final Logger LOG = Logger.getInstance(HybrisProjectOpenProcessor.class);

    public HybrisProjectOpenProcessor(final DefaultHybrisProjectImportBuilder builder) {
        super(builder);
    }

    @Override
    public boolean doQuickImport(final VirtualFile file, final WizardContext wizardContext) {
        this.getBuilder().cleanup();
        this.getBuilder().setRootProjectDirectory(VfsUtil.virtualToIoFile(file.getParent()));

        final List<HybrisModuleDescriptor> projects = this.getBuilder().getList();
        if (null == projects || 1 != projects.size()) {
            return false;
        }

        try {
            this.getBuilder().setList(projects);
        } catch (ConfigurationException e) {
            LOG.error(e);
        }

        wizardContext.setProjectName(projects.get(0).getName());

        return true;
    }

    @Nullable
    @Override
    public String[] getSupportedExtensions() {
        return new String[]{
            HybrisConstants.EXTENSION_INFO_XML,
            HybrisConstants.LOCAL_EXTENSIONS_XML,
            HybrisConstants.EXTENSIONS_XML
        };
    }

}
