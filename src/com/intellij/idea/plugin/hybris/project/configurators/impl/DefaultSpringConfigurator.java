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

import com.intellij.facet.ModifiableFacetModel;
import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.project.configurators.SpringConfigurator;
import com.intellij.idea.plugin.hybris.project.descriptors.ConfigHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.CoreHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.PlatformHybrisModuleDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider;
import com.intellij.openapi.module.Module;
import com.intellij.spring.facet.SpringFacet;
import com.intellij.spring.facet.SpringFileSet;
import com.intellij.util.containers.ContainerUtil;
import org.apache.commons.lang3.Validate;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by Martin Zdarsky (martin.zdarsky@hybris.com) on 10/08/15.
 */
public class DefaultSpringConfigurator implements SpringConfigurator {

    private static final Logger LOG = Logger.getInstance(DefaultSpringConfigurator.class);

    @Override
    public void findSpringConfiguration(@NotNull final List<HybrisModuleDescriptor> modulesChosenForImport) {
        Validate.notNull(modulesChosenForImport);

        final Map<String, HybrisModuleDescriptor> moduleDescriptorMap = ContainerUtil.newHashMap();
        File localProperties = null;
        File advancedProperties = null;
        for (HybrisModuleDescriptor moduleDescriptor : modulesChosenForImport) {
            moduleDescriptorMap.put(moduleDescriptor.getName(), moduleDescriptor);
            if (moduleDescriptor instanceof ConfigHybrisModuleDescriptor) {
                final ConfigHybrisModuleDescriptor configModule = (ConfigHybrisModuleDescriptor) moduleDescriptor;
                localProperties = new File(configModule.getRootDirectory(), HybrisConstants.LOCAL_PROPERTIES);
            }
            if (moduleDescriptor instanceof PlatformHybrisModuleDescriptor) {
                final PlatformHybrisModuleDescriptor platformModule = (PlatformHybrisModuleDescriptor) moduleDescriptor;
                advancedProperties = new File(platformModule.getRootDirectory(), HybrisConstants.ADVANCED_PROPERTIES);
            }
        }
        for (HybrisModuleDescriptor moduleDescriptor : modulesChosenForImport) {
            processHybrisModule(moduleDescriptorMap, moduleDescriptor);
            if (moduleDescriptor instanceof CoreHybrisModuleDescriptor) {
                if (advancedProperties != null) {
                    moduleDescriptor.addSpringFile(advancedProperties.getAbsolutePath());
                }
                if (localProperties != null) {
                    moduleDescriptor.addSpringFile(localProperties.getAbsolutePath());
                }
            }
        }
    }

    @Override
    public void configureDependencies(
        final @NotNull HybrisProjectDescriptor hybrisProjectDescriptor,
        final @NotNull IdeModifiableModelsProvider modifiableModelsProvider
    ) {
        final Map<String, ModifiableFacetModel> modifiableFacetModelMap = ContainerUtil.newHashMap();

        for (Module module : modifiableModelsProvider.getModules()) {
            final ModifiableFacetModel modifiableFacetModel = modifiableModelsProvider.getModifiableFacetModel(module);
            modifiableFacetModelMap.put(module.getName(), modifiableFacetModel);
        }

        for (HybrisModuleDescriptor moduleDescriptor : hybrisProjectDescriptor.getModulesChosenForImport()) {
            configureFacetDependencies(moduleDescriptor, modifiableFacetModelMap);
        }
    }

    private void configureFacetDependencies(
        @NotNull final HybrisModuleDescriptor moduleDescriptor,
        @NotNull final Map<String, ModifiableFacetModel> modifiableFacetModelMap
    ) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(modifiableFacetModelMap);

        final SpringFileSet springFileSet = getSpringFileSet(modifiableFacetModelMap, moduleDescriptor.getName());
        if (springFileSet == null) {
            return;
        }

        for (HybrisModuleDescriptor dependsOnModule : moduleDescriptor.getDependenciesTree()) {
            final SpringFileSet parentFileSet = getSpringFileSet(modifiableFacetModelMap, dependsOnModule.getName());
            if (parentFileSet == null) {
                continue;
            }
            springFileSet.addDependency(parentFileSet);
        }
    }

    private SpringFileSet getSpringFileSet(
        @NotNull final Map<String, ModifiableFacetModel> modifiableFacetModelMap,
        @NotNull final String moduleName
    ) {
        Validate.notNull(moduleName);
        Validate.notNull(modifiableFacetModelMap);

        final ModifiableFacetModel modifiableFacetModel = modifiableFacetModelMap.get(moduleName);
        if (modifiableFacetModel == null) {
            return null;
        }
        final SpringFacet springFacet = modifiableFacetModel.getFacetByType(SpringFacet.FACET_TYPE_ID);
        if (springFacet == null || springFacet.getFileSets().isEmpty()) {
            return null;
        }
        return springFacet.getFileSets().iterator().next();
    }

    private void processHybrisModule(
        @NotNull final Map<String, HybrisModuleDescriptor> moduleDescriptorMap,
        @NotNull final HybrisModuleDescriptor moduleDescriptor
    ) {
        Validate.notNull(moduleDescriptorMap);
        Validate.notNull(moduleDescriptor);

        processPropertiesFile(moduleDescriptorMap, moduleDescriptor);
        try {
            processWebXml(moduleDescriptor);
        } catch (Exception e) {
            LOG.error("Unable to parse web.xml for module "+moduleDescriptor.getName(), e);
        }
    }

    private void processPropertiesFile(
        final Map<String, HybrisModuleDescriptor> moduleDescriptorMap,
        final HybrisModuleDescriptor moduleDescriptor
    ) {
        final Properties projectProperties = new Properties();

        final File propFile = new File(moduleDescriptor.getRootDirectory(), HybrisConstants.PROJECT_PROPERTIES);
        moduleDescriptor.addSpringFile(propFile.getAbsolutePath());

        try (FileInputStream fis = new FileInputStream(propFile)) {
            projectProperties.load(fis);
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            LOG.error("", e);
            return;
        }

        for (String key : projectProperties.stringPropertyNames()) {
            if (key.endsWith(HybrisConstants.APPLICATION_CONTEXT_SPRING_FILES)
                || key.endsWith(HybrisConstants.ADDITIONAL_WEB_SPRING_CONFIG_FILES)
                || key.endsWith(HybrisConstants.GLOBAL_CONTEXT_SPRING_FILES)) {
                final String moduleName = key.substring(0, key.indexOf('.'));
                // relevantModule can be different to a moduleDescriptor. e.g. addon concept
                final HybrisModuleDescriptor relevantModule = moduleDescriptorMap.get(moduleName);
                if (relevantModule != null) {
                    final String rawFile = projectProperties.getProperty(key);
                    if (rawFile == null) {
                        continue;
                    }
                    for (String file : rawFile.split(",")) {
                        addSpringXmlFile(relevantModule, file);
                    }
                }
            }
        }
    }

    private void scanForSpringImport(
        final HybrisModuleDescriptor moduleDescriptor,
        final File springFile
    ) throws IOException, JDOMException {
        final Document document = getDocument(springFile);
        final List<Element> importList = document.getRootElement().getChildren()
                                                 .stream()
                                                 .filter(e -> e.getName().equals("import"))
                                                 .collect(Collectors.toList());
        if (importList != null) {
            processImportNodeList(moduleDescriptor, importList);
        }
    }

    private void processImportNodeList(
        final HybrisModuleDescriptor moduleDescriptor,
        final List<Element> importList
    ) {
        for (Element importElement: importList) {
            String resource = importElement.getAttributeValue("resource");
            addSpringXmlFile(moduleDescriptor, resource);
        }
    }
    private void processWebXml(final HybrisModuleDescriptor moduleDescriptor) throws IOException, JDOMException {
        File webXml = new File(moduleDescriptor.getRootDirectory(), HybrisConstants.WEB_XML_DIRECTORY_RELATIVE_PATH);
        if (webXml == null || !webXml.exists()) {
            return;
        }
        final Document document = getDocument(webXml);
        final Element root = document.getRootElement();
        if (root == null || !root.getName().equals("web-app")) {
            return;
        }
        String location = root.getChildren()
                              .stream()
                              .filter(e -> e.getName().equals("context-param"))
                              .filter(e -> e.getChildren().stream().anyMatch(p -> p.getName().equals("param-name") && p.getValue().equals("contextConfigLocation")))
                              .filter(Objects::nonNull)
                              .map(e -> e.getChildren().stream().filter(p -> p.getName().equals("param-value")).findFirst().orElse(null))
                              .filter(Objects::nonNull)
                              .map(Element::getValue)
                              .findFirst().orElse(null);
        if (location == null) {
            return;
        }
        processContextParam(moduleDescriptor, location.trim());
    }

    private void processContextParam(final HybrisModuleDescriptor moduleDescriptor, final String contextConfigLocation) {
        File webModuleDir = new File(moduleDescriptor.getRootDirectory(), HybrisConstants.WEB_ROOT_DIRECTORY_RELATIVE_PATH);;
        for (String xml: contextConfigLocation.split(" ,")) {
            if (!xml.endsWith(".xml")) {
                continue;
            }
            File springFile = new File(webModuleDir, xml);
            if (!springFile.exists()) {
                continue;
            }
            addSpringFle(moduleDescriptor, springFile);
        }
    }

    private void addSpringXmlFile(final HybrisModuleDescriptor moduleDescriptor, final String file) {
        final File resourcesDir = new File(moduleDescriptor.getRootDirectory(), HybrisConstants.RESOURCES_DIRECTORY);
        final File springFile;
        if (file.startsWith("classpath:")) {
            springFile = new File(resourcesDir, file.substring("classpath:".length(), file.length()));
        } else {
            springFile = new File(resourcesDir, file);
        }
        if (springFile.exists()) {
            addSpringFle(moduleDescriptor, springFile);
        }
    }


    private void addSpringFle(final HybrisModuleDescriptor relevantModule, final File springFile) {
        try {
            if (!hasSpringContent(springFile)) {
                return;
            }
            if (relevantModule.addSpringFile(springFile.getAbsolutePath())) {
                scanForSpringImport(relevantModule, springFile);
            }
        } catch (Exception e) {
            LOG.error("unable scan file for spring imports "+springFile.getName());
        }
    }

    private boolean hasSpringContent(final File springFile) throws IOException, JDOMException {
        final Document document = getDocument(springFile);
        if (document == null) {
            return false;
        }
        return document.getRootElement() != null && document.getRootElement().getName().equals("beans");
    }

    private Document getDocument(File inputFile) throws IOException, JDOMException {
        SAXBuilder saxBuilder = new SAXBuilder();
        return saxBuilder.build(inputFile);
    }
}
