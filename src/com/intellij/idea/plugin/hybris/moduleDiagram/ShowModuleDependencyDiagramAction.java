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

package com.intellij.idea.plugin.hybris.moduleDiagram;

import com.intellij.diagram.DiagramProvider;
import com.intellij.idea.plugin.hybris.actions.ActionUtils;
import com.intellij.idea.plugin.hybris.statistics.StatsCollector;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.uml.core.actions.ShowDiagram;
import org.jetbrains.annotations.Nullable;

/**
 * @author Eugene.Kudelevsky
 */
public class ShowModuleDependencyDiagramAction extends ShowDiagram {

    @Override
    public void update(final AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(ActionUtils.isHybrisContext(e));
    }

    @Nullable
    @Override
    public DiagramProvider getProvider(final AnActionEvent e) {
        return new ModuleDepDiagramProvider();
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        StatsCollector.getInstance().collectStat(StatsCollector.ACTIONS.DEPENDENCY_DIAGRAM);
        super.actionPerformed(e);
    }
}
