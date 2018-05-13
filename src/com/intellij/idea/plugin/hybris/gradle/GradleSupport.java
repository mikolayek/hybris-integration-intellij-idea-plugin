package com.intellij.idea.plugin.hybris.gradle;

import com.intellij.idea.plugin.hybris.project.configurators.GradleConfigurator;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.gradle.settings.GradleSettings;

import java.util.Collections;

/**
 * @author Eugene.Kudelevsky
 */
public class GradleSupport {

    @Nullable
    public static GradleSupport getInstance() {
        return ServiceManager.getService(GradleSupport.class);
    }

    public void clearLinkedProjectSettings(@NotNull Project project) {
        GradleSettings.getInstance(project).setLinkedProjectsSettings(Collections.emptyList());
    }
}
