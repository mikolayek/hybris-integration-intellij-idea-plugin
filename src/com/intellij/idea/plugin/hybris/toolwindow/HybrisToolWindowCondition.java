package com.intellij.idea.plugin.hybris.toolwindow;

import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;

public class HybrisToolWindowCondition implements Condition<Project> {

    @Override
    public boolean value(final Project project) {
        return HybrisProjectSettingsComponent.getInstance(project).getState().isHybrisProject();
    }
}
