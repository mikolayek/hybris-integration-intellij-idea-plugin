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

package com.intellij.idea.plugin.hybris.ant;

import com.intellij.ide.util.treeView.AbstractTreeBuilder;
import com.intellij.idea.plugin.hybris.common.services.CommonIdeaService;
import com.intellij.lang.ant.config.explorer.AntExplorer;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ex.ToolWindowManagerAdapter;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.ui.ListenerUtil;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Eugene.Kudelevsky
 */
public class AntTreeUpdatingHack extends AbstractProjectComponent {

    private static final Logger LOG = Logger.getInstance(AntTreeUpdatingHack.class);

    private static final String ANT_TOOL_WINDOW_ID = "Ant Build";

    public AntTreeUpdatingHack(final Project project) {
        super(project);
    }

    @Override
    public void initComponent() {
        StartupManager.getInstance(myProject).runWhenProjectIsInitialized(this::onProjectInitialized);
    }

    private void onProjectInitialized() {
        if (!CommonIdeaService.getInstance().isHybrisProject(myProject)) {
            return;
        }
        final ToolWindowManagerEx toolWindowManager = ToolWindowManagerEx.getInstanceEx(myProject);
        final ToolWindow antToolWindow = toolWindowManager.getToolWindow(ANT_TOOL_WINDOW_ID);

        if (antToolWindow == null) {
            LOG.info("Cannot get Ant tool window");
            return;
        }
        if (!tryToPatchToolWindow(antToolWindow)) {
            toolWindowManager.addToolWindowManagerListener(new ToolWindowManagerAdapter() {

                private boolean pending = true;

                @Override
                public void stateChanged() {
                    if (pending && tryToPatchToolWindow(antToolWindow)) {
                        pending = false;
                        toolWindowManager.removeToolWindowManagerListener(this);
                    }
                }
            });
        }
    }

    private boolean tryToPatchToolWindow(@NotNull final ToolWindow antToolWindow) {
        final AntExplorer antExplorer = getAntExplorer(antToolWindow);
        final AbstractTreeBuilder antTreeBuilder = antExplorer == null ? null : getAntTreeBuilder(antExplorer);

        if (antTreeBuilder == null) {
            return false;
        }
        ListenerUtil.addFocusListener(antExplorer, new FocusListener() {

            private boolean focused = false;

            @Override
            public void focusGained(final FocusEvent e) {
                stateChanged();
            }

            @Override
            public void focusLost(final FocusEvent e) {
                stateChanged();
            }

            private void stateChanged() {
                final Component focusOwner = IdeFocusManager.getInstance(myProject).getFocusOwner();
                final boolean newFocused = focusOwner != null && UIUtil.isAncestor(antExplorer, focusOwner);

                if (focused != newFocused) {
                    focused = newFocused;

                    if (newFocused && !antTreeBuilder.isDisposed()) {
                        queueUpdate(antTreeBuilder);
                    }
                }
            }
        });
        queueUpdate(antTreeBuilder);

        DumbService.getInstance(myProject).runWhenSmart(() -> {
            if (!antTreeBuilder.isDisposed()) {
                queueUpdate(antTreeBuilder);
            }
        });
        return true;
    }

    private static void queueUpdate(@NotNull final AbstractTreeBuilder antTreeBuilder) {
        antTreeBuilder.queueUpdate();
        antTreeBuilder.getTree().repaint();
    }

    @Nullable
    private static AbstractTreeBuilder getAntTreeBuilder(final AntExplorer antExplorer) {
        final JTree tree = UIUtil.findComponentOfType(antExplorer, JTree.class);

        if (tree == null) {
            LOG.info("Cannot get tree object from AntExplorer");
            return null;
        }
        final AbstractTreeBuilder antTreeBuilder = AbstractTreeBuilder.getBuilderFor(tree);

        if (antTreeBuilder == null) {
            LOG.info("Cannot get Ant tree builder");
            return null;
        }
        if (antTreeBuilder.isDisposed()) {
            LOG.info("Ant tree builder is disposed");
            return null;
        }
        return antTreeBuilder;
    }

    @Nullable
    private static AntExplorer getAntExplorer(final @NotNull ToolWindow antToolWindow) {
        final AntExplorer antExplorer = Arrays
            .stream(antToolWindow.getContentManager().getContents())
            .map(content -> {
                final JComponent component = content.getComponent();
                return component instanceof AntExplorer ? (AntExplorer) component : null;
            })
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);

        if (antExplorer == null) {
            LOG.info("Cannot get AntExplorer object");
        }
        return antExplorer;
    }
}
