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

package com.intellij.idea.plugin.hybris.tools.remote.console;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.impl.ContentImpl;

import javax.swing.*;
import java.awt.*;

public class ConsoleToolWindowUtil {

    private static ConsoleToolWindowUtil instance = new ConsoleToolWindowUtil();

    private ToolWindow toolWindow;

    private String[] consoleName;

    private String[] consoleDescription;

    private ConsoleToolWindowUtil() {
    }

    public static ConsoleToolWindowUtil getInstance() {
        return instance;
    }

    public void showConsoleToolWindow(final Project project, final ConsoleView... consoles) {
        Integer currentSelectedContentIndex = 0;
        if (toolWindow == null) {
            createNewToolWindow(project);
        } else if (!toolWindow.getTitle().equals("Hybris Console")) {
            final ToolWindow window = ToolWindowManager.getInstance(project).getToolWindow("Hybris Console");
            if (window == null) {
                createNewToolWindow(project);
            } else {
                toolWindow = window;
                currentSelectedContentIndex = getCurrentSelectedTab();
                toolWindow.getContentManager().removeAllContents(false);
            }
        } else {
            currentSelectedContentIndex = getCurrentSelectedTab();
            toolWindow.getContentManager().removeAllContents(false);
        }
        setConsolesInToolWindow(consoles);
        toolWindow.activate(null);
        selectTab(currentSelectedContentIndex);
    }

    public void setConsoleName(final String[] consoleName) {
        this.consoleName = consoleName;
    }

    public void setConsoleDescription(final String[] consoleDescription) {
        this.consoleDescription = consoleDescription;
    }

    public void selectTab(final Integer currentSelectedContentIndex) {
        final Content contentToSelect = toolWindow.getContentManager().getContent(currentSelectedContentIndex);
        toolWindow.getContentManager().setSelectedContent(contentToSelect);
    }

    private void setConsolesInToolWindow(final ConsoleView... consoles) {
        int i = 0;
        for (ConsoleView consoleView : consoles) {
            final Content consoleContent = createConsoleContent(consoleView, consoleName[i], consoleDescription[i]);
            toolWindow.getContentManager().addContent(consoleContent);
            i++;
        }
    }

    private Integer getCurrentSelectedTab() {
        final Integer currentSelectedContentIndex;
        final Content currentSelectedContent = toolWindow.getContentManager().getSelectedContent();
        if (currentSelectedContent == null) {
            return 0;
        }
        currentSelectedContentIndex = toolWindow.getContentManager().getIndexOfContent(currentSelectedContent);
        return currentSelectedContentIndex;
    }

    private void createNewToolWindow(final Project project) {
        toolWindow = ToolWindowManager.getInstance(project).registerToolWindow(
            "Hybris Console",
            true,
            ToolWindowAnchor.BOTTOM,
            project,
            true,
            true
        );

        toolWindow.setTitle("Hybris Console");
        toolWindow.setIcon(HybrisIcons.HYBRIS_ICON_13x13);
    }

    private Content createConsoleContent(final ConsoleView console, final String title, final String description) {
        final Content content = new ContentImpl(createConsolePanel(console), description, true);
        content.setTabName(title);
        content.setDisplayName(title);
        content.setIcon(HybrisIcons.HYBRIS_ICON);
        return content;
    }

    private JComponent createConsolePanel(ConsoleView view) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(view.getComponent(), BorderLayout.CENTER);
        return panel;
    }

}
