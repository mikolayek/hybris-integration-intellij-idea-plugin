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

import com.intellij.diagram.DiagramBuilder;
import com.intellij.diagram.DiagramColorManagerBase;
import com.intellij.diagram.DiagramEdge;
import com.intellij.diagram.DiagramNode;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author Eugene.Kudelevsky
 */
public class ModuleDepDiagramColorManager extends DiagramColorManagerBase {

    private static final JBColor NON_CUSTOM_EXTENSIONS_COLOR = new JBColor(
        new Color(201, 228, 238),
        new Color(49, 66, 90)
    );

    @Override
    public Color getNodeHeaderColor(final DiagramBuilder builder, @Nullable final DiagramNode node) {
        if (node instanceof ModuleDepDiagramNode &&
            !((ModuleDepDiagramNode) node).getIdentifyingElement().isCustomExtension()) {
            return NON_CUSTOM_EXTENSIONS_COLOR;
        }
        return super.getNodeHeaderColor(builder, node);
    }

    @Override
    public Color getEdgeColor(DiagramEdge diagramEdge) {
        if (diagramEdge instanceof ModuleDepDiagramEdge) {
            ModuleDepDiagramEdge edge = (ModuleDepDiagramEdge) diagramEdge;
            if (edge.isCircular()) {
                int redFragment = 128 / edge.getNumberOfCircles();
                int redDelta = redFragment * edge.getCircleNumber();
                int red = 127 + redDelta;
                return new Color(red, 0, 0);
            }
        }
        return super.getEdgeColor(diagramEdge);
    }
}
