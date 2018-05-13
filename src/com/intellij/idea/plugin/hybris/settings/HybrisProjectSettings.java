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

package com.intellij.idea.plugin.hybris.settings;

import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Set;

import static com.intellij.util.containers.ContainerUtil.newHashSet;

/**
 * Created 6:51 PM 28 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectSettings {

    protected boolean hybrisProject;
    protected String customDirectory;
    protected String hybrisDirectory;
    protected String configDirectory;
    protected String importedByVersion;
    protected String hybrisVersion;
    protected String javadocUrl;
    protected String sourceCodeFile;
    protected String externalExtensionsDirectory;
    protected String externalConfigDirectory;
    protected String externalDbDriversDirectory;
    protected String ideModulesFilesDirectory;
    protected String hostIP = "localhost";
    protected String port = HybrisConstants.DEFAULT_TOMCAT_SSL_PORT;
    protected String hacLogin = "admin";
    protected String hacPassword = "nimda";
    protected boolean importOotbModulesInReadOnlyMode;
    protected boolean followSymlink;
    protected boolean scanThroughExternalModule;
    protected boolean createBackwardCyclicDependenciesForAddOns = false;
    protected Set<String> completeSetOfAvailableExtensionsInHybris = newHashSet();
    protected Set<String> unusedExtensions = newHashSet();
    protected Set<String> modulesOnBlackList = newHashSet();

    public boolean isHybrisProject() {
        return hybrisProject;
    }

    public void setHybrisProject(final boolean hybrisProject) {
        this.hybrisProject = hybrisProject;
    }

    public String getCustomDirectory() {
        return customDirectory;
    }

    public void setCustomDirectory(final String customDirectory) {
        this.customDirectory = customDirectory;
    }

    public String getConfigDirectory() {
        return configDirectory;
    }

    public void setConfigDirectory(final String configDirectory) {
        this.configDirectory = configDirectory;
    }

    public String getHybrisDirectory() {
        return hybrisDirectory;
    }

    public void setHybrisDirectory(final String hybrisDirectory) {
        this.hybrisDirectory = hybrisDirectory;
    }

    public String getImportedByVersion() {
        return importedByVersion;
    }

    public void setImportedByVersion(final String importedByVersion) {
        this.importedByVersion = importedByVersion;
    }

    public String getHybrisVersion() {
        return hybrisVersion;
    }

    public void setHybrisVersion(final String hybrisVersion) {
        this.hybrisVersion = hybrisVersion;
    }

    public String getJavadocUrl() {
        return javadocUrl;
    }

    public void setJavadocUrl(final String javadocUrl) {
        this.javadocUrl = javadocUrl;
    }

    public String getSourceCodeFile() {
        return sourceCodeFile;
    }

    public void setSourceCodeFile(final String sourceCodeFile) {
        this.sourceCodeFile = sourceCodeFile;
    }

    public String getExternalExtensionsDirectory() {
        return externalExtensionsDirectory;
    }

    public void setExternalExtensionsDirectory(final String externalExtensionsDirectory) {
        this.externalExtensionsDirectory = externalExtensionsDirectory;
    }

    public String getExternalConfigDirectory() {
        return externalConfigDirectory;
    }

    public void setExternalConfigDirectory(final String externalConfigDirectory) {
        this.externalConfigDirectory = externalConfigDirectory;
    }

    public String getExternalDbDriversDirectory() {
        return externalDbDriversDirectory;
    }

    public void setExternalDbDriversDirectory(final String externalDbDriversDirectory) {
        this.externalDbDriversDirectory = externalDbDriversDirectory;
    }

    public String getIdeModulesFilesDirectory() {
        return ideModulesFilesDirectory;
    }

    public void setIdeModulesFilesDirectory(final String ideModulesFilesDirectory) {
        this.ideModulesFilesDirectory = ideModulesFilesDirectory;
    }

    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(final String hostIP) {
        this.hostIP = hostIP;
    }

    public String getPort() {
        return port;
    }

    public void setPort(final String port) {
        this.port = port;
    }

    public String getHacLogin() {
        return hacLogin;
    }

    public void setHacLogin(final String hacLogin) {
        this.hacLogin = hacLogin;
    }

    public String getHacPassword() {
        return hacPassword;
    }

    public void setHacPassword(final String hacPassword) {
        this.hacPassword = hacPassword;
    }

    public boolean getImportOotbModulesInReadOnlyMode() {
        return importOotbModulesInReadOnlyMode;
    }

    public void setImportOotbModulesInReadOnlyMode(final boolean importOotbModulesInReadOnlyMode) {
        this.importOotbModulesInReadOnlyMode = importOotbModulesInReadOnlyMode;
    }

    public boolean isFollowSymlink() {
        return followSymlink;
    }

    public void setFollowSymlink(final boolean followSymlink) {
        this.followSymlink = followSymlink;
    }

    public boolean isScanThroughExternalModule() {
        return scanThroughExternalModule;
    }

    public void setScanThroughExternalModule(final boolean scanThroughExternalModule) {
        this.scanThroughExternalModule = scanThroughExternalModule;
    }

    public Set<String> getCompleteSetOfAvailableExtensionsInHybris() {
        return completeSetOfAvailableExtensionsInHybris;
    }

    public void setCompleteSetOfAvailableExtensionsInHybris(final Set<String> completeSetOfAvailableExtensionsInHybris) {
        this.completeSetOfAvailableExtensionsInHybris = completeSetOfAvailableExtensionsInHybris;
    }

    public boolean isCreateBackwardCyclicDependenciesForAddOns() {
        return createBackwardCyclicDependenciesForAddOns;
    }

    public void setCreateBackwardCyclicDependenciesForAddOns(final boolean createBackwardCyclicDependenciesForAddOns) {
        this.createBackwardCyclicDependenciesForAddOns = createBackwardCyclicDependenciesForAddOns;
    }

    public Set<String> getUnusedExtensions() {
        return unusedExtensions;
    }

    public void setUnusedExtensions(final Set<String> unusedExtensions) {
        this.unusedExtensions = unusedExtensions;
    }

    public void setModulesOnBlackList(final Set<String> modulesOnBlackList) {
        this.modulesOnBlackList = modulesOnBlackList;
    }

    public Set<String> getModulesOnBlackList() {
        return modulesOnBlackList;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(hybrisProject)
            .append(customDirectory)
            .append(hybrisDirectory)
            .append(configDirectory)
            .append(importedByVersion)
            .append(hybrisVersion)
            .append(javadocUrl)
            .append(sourceCodeFile)
            .append(externalExtensionsDirectory)
            .append(externalConfigDirectory)
            .append(externalDbDriversDirectory)
            .append(ideModulesFilesDirectory)
            .append(hostIP)
            .append(port)
            .append(hacLogin)
            .append(hacPassword)
            .append(importOotbModulesInReadOnlyMode)
            .append(followSymlink)
            .append(scanThroughExternalModule)
            .append(completeSetOfAvailableExtensionsInHybris)
            .append(createBackwardCyclicDependenciesForAddOns)
            .append(unusedExtensions)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (null == o || getClass() != o.getClass()) {
            return false;
        }

        final HybrisProjectSettings other = (HybrisProjectSettings) o;

        return new EqualsBuilder()
            .append(hybrisProject, other.hybrisProject)
            .append(customDirectory, other.customDirectory)
            .append(hybrisDirectory, other.hybrisDirectory)
            .append(configDirectory, other.configDirectory)
            .append(importedByVersion, other.importedByVersion)
            .append(hybrisVersion, other.hybrisVersion)
            .append(javadocUrl, other.javadocUrl)
            .append(sourceCodeFile, other.sourceCodeFile)
            .append(externalExtensionsDirectory, other.externalExtensionsDirectory)
            .append(externalConfigDirectory, other.externalConfigDirectory)
            .append(externalDbDriversDirectory, other.externalDbDriversDirectory)
            .append(ideModulesFilesDirectory, other.ideModulesFilesDirectory)
            .append(hostIP, other.hostIP)
            .append(port, other.port)
            .append(hacLogin, other.hacLogin)
            .append(hacPassword, other.hacPassword)
            .append(importOotbModulesInReadOnlyMode, other.importOotbModulesInReadOnlyMode)
            .append(followSymlink, other.followSymlink)
            .append(scanThroughExternalModule, other.scanThroughExternalModule)
            .append(completeSetOfAvailableExtensionsInHybris, other.completeSetOfAvailableExtensionsInHybris)
            .append(createBackwardCyclicDependenciesForAddOns, other.createBackwardCyclicDependenciesForAddOns)
            .append(unusedExtensions, other.unusedExtensions)
            .isEquals();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HybrisProjectSettings{");
        sb.append("hybrisProject=").append(hybrisProject);
        sb.append("customDirectoryPath=").append(customDirectory);
        sb.append("hybrisDirectory=").append(hybrisDirectory);
        sb.append("configDirectory=").append(configDirectory);
        sb.append("importedByVersion=").append(importedByVersion);
        sb.append("hybrisVersion=").append(hybrisVersion);
        sb.append("javadocUrl=").append(javadocUrl);
        sb.append("sourceCodeFile=").append(sourceCodeFile);
        sb.append("externalExtensionsDirectory=").append(externalExtensionsDirectory);
        sb.append("externalConfigDirectory=").append(externalConfigDirectory);
        sb.append("externalDbDriversDirectory=").append(externalDbDriversDirectory);
        sb.append("ideModulesFilesDirectory=").append(ideModulesFilesDirectory);
        sb.append("hostIP=").append(hostIP);
        sb.append("port=").append(port);
        sb.append("hacLogin=").append(hacLogin);
        sb.append("hacPassword=").append(hacPassword);
        sb.append("importOotbModulesInReadOnlyMode=").append(importOotbModulesInReadOnlyMode);
        sb.append("followSymlink=").append(followSymlink);
        sb.append("scanThroughExternalModule=").append(scanThroughExternalModule);
        sb.append("completeSetOfAvailableExtensionsInHybris=").append(completeSetOfAvailableExtensionsInHybris);
        sb.append("createBackwardCyclicDependenciesForAddOns=").append(createBackwardCyclicDependenciesForAddOns);
        sb.append("unusedExtensions=").append(unusedExtensions);
        sb.append('}');
        return sb.toString();
    }
}
