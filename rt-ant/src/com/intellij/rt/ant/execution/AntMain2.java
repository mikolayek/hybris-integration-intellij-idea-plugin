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

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Martin Zdarsky-Jones on 24/10/16.
 * <p>
 * It's the same class like Intellij version but uses HybrisAnsiAntLogger instead
 */
public class AntMain2 {

    public static void main(String[] args) throws
                                           ClassNotFoundException,
                                           NoSuchMethodException,
                                           IllegalAccessException,
                                           InvocationTargetException,
                                           FileNotFoundException,
                                           UnsupportedEncodingException {
        com.intellij.rt.ant.execution.HybrisIdeaAntLogger.guardStreams();

        for (int index = 0; index < args.length; index++) {
            if (IdeaAntLogger2.class.getName().equals(args[index])) {
                args[index] = com.intellij.rt.ant.execution.HybrisAnsiAntLogger.class.getName();
            }
        }

        // as we build classpath ourselves, and ensure all libraries are added to classpath,
        // preferred way for us to run ant will be using the traditional ant entry point, via the "Main" class
        try {
            final Class antMain = Class.forName("org.apache.tools.ant.Main");
            //noinspection HardCodedStringLiteral
            antMain.getMethod("main", new Class[]{args.getClass()}).invoke(null, new Object[]{args});
            return;
        } catch (ClassNotFoundException e) {
            // ignore
        }

        // fallback: try the newer approach, launcher
        // This approach is less preferred in our case, but still...
        // From the ant documentation: "You should start the launcher with the most minimal classpath possible, generally just the ant-launcher.jar."
        final Class antLauncher = Class.forName("org.apache.tools.ant.launch.Launcher");
        //noinspection HardCodedStringLiteral
        antLauncher.getMethod("main", new Class[]{args.getClass()}).invoke(null, new Object[]{args});

    }
}
