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

package com.intellij.rt.ant.execution;

import com.intellij.idea.plugin.hybris.ant.AntGenResult;
import org.apache.tools.ant.BuildEvent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

/**
 * Created by Martin Zdarsky-Jones on 17/2/17.
 * <p>
 * Logger parses output messages for known patterns and tried to detect added or removed extensions.
 * That can happen for targets extgen and modulegen.
 * If there is a need to change localextensions tyhis logger will serialize the result to hard drive to be picked up by IDEA JVM
 */
public class HybrisParsingAntLogger extends com.intellij.rt.ant.execution.HybrisIdeaAntLogger {

    private enum HYBRIS_MODE {NONE, ADD, REMOVE}

    private static final String HYBRIS_PLATFORM_DIR = "hybris Platform directory";
    private static final String HYBRIS_ADD = "Add your extension to your";
    private static final String HYBRIS_REMOVE = "Remove the following extensions from your";
    private static final String HYBRIS_STOP = "Make sure the applicationserver is stopped before you build the extension the first time.";

    private HYBRIS_MODE myMode = HYBRIS_MODE.NONE;
    private AntGenResult result;

    @Override
    public synchronized void buildStarted(BuildEvent event) {
        super.buildStarted(event);
        result = null;
    }

    @Override
    public synchronized void messageLogged(BuildEvent event) {
        super.messageLogged(event);
        final String message = event.getMessage();
        if (message == null) {
            return;
        }
        processHybrisMessage(message);
    }

    private void processHybrisMessage(final String message) {
        if (message.contains(HYBRIS_PLATFORM_DIR)) {
            final String platformDir = message.substring(message.indexOf(":") + 1).trim();
            result = new AntGenResult(platformDir);
            return;
        }
        if (message.contains(HYBRIS_ADD)) {
            myMode = HYBRIS_MODE.ADD;
            return;
        }
        if (message.contains(HYBRIS_REMOVE)) {
            myMode = HYBRIS_MODE.REMOVE;
            return;
        }
        if (message.contains(HYBRIS_STOP)) {
            myMode = HYBRIS_MODE.NONE;
            publishExtensionsDelta();
            return;
        }
        switch (myMode) {
            case ADD:
                addExtension(message);
                break;
            case REMOVE:
                removeExtensions(message);
                break;
        }
    }

    private void publishExtensionsDelta() {
        if (result == null || result.getExtensionsToAdd().isEmpty()) {
            return;
        }
        final String firstExtensionPath = result.getExtensionsToAdd().iterator().next().replaceAll("\"", "/");
        final int index = firstExtensionPath.indexOf("/bin/custom");
        final String fileName = firstExtensionPath.substring(0, index) + "/temp/ant.ser";
        try (
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
        ) {
            out.writeObject(result);
        } catch (IOException e) {
            //we don't have proper logger in this JVM
            e.printStackTrace();
        }
    }

    private void addExtension(final String message) {
        int index = message.indexOf("<extension dir=");
        if (index >= 0) {
            result.getExtensionsToAdd().add(message.substring(message.indexOf("\"") + 1, message.lastIndexOf("\"")));
        }
    }

    private void removeExtensions(final String message) {
        String commaSeparated = message.trim();
        if (commaSeparated.isEmpty()) {
            return;
        }

        final String[] extensions = commaSeparated.split(",");
        for (String extension : extensions) {
            result.getExtensionsToRemove().add(extension);
        }
    }

}
