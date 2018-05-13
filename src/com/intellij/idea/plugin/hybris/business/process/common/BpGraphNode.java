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

package com.intellij.idea.plugin.hybris.business.process.common;

import com.intellij.idea.plugin.hybris.business.process.jaxb.Process;
import com.intellij.idea.plugin.hybris.business.process.jaxb.model.BpGenericAction;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


/**
 * Created 2:40 PM 02 February 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface BpGraphNode {

    @NotNull
    BpGenericAction getGenericAction();

    @NotNull
    Map<String, BpGraphNode> getTransitions();

    @NotNull
    VirtualFile getXmlVirtualFile();

    @NotNull
    Process getProcess();

    @NotNull
    Map<String, BpGraphNode> getNodesMap();

}
