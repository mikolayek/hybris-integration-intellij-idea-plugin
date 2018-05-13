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

import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.idea.plugin.hybris.tools.remote.http.impex.HybrisHttpResult;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

import java.util.List;

import static com.intellij.util.containers.ContainerUtil.newArrayList;

/**
 * refer @see HybrisConsole
 */
@Deprecated
public abstract class ExecuteConsole {

    private List<ConsoleView> consoleViewList;
    private boolean processComplete = false;

    public ExecuteConsole() {
        super();
        consoleViewList = newArrayList();
    }

    public abstract void show(HybrisHttpResult httpResult, Project project);

    public abstract void initConsoleWindow(Project project);

    public void updateConsoleWindow(
        final String text,
        final ConsoleViewContentType type,
        final ConsoleView currentConsole
    ) {
        currentConsole.print(text, type);
    }

    public void updateConsoleWindow(final String text, final ConsoleViewContentType type) {
        consoleViewList.get(0).print(text, type);
    }

    protected void startSendRequestAnimationInAllConsoles() {
        processComplete = false;
        clearAllConsoles();
        ApplicationManager.getApplication().executeOnPooledThread(new SendRequestAnimation());
    }

    protected void markRequestAsFinish() {
        processComplete = true;
        clearAllConsoles();
    }

    protected void fillAllConsolesWithTheSameContent(final String content) {
        for (ConsoleView consoleView : consoleViewList) {
            consoleView.print(content, ConsoleViewContentType.NORMAL_OUTPUT);
        }
    }

    protected void clearAllConsoles() {
        //TODO this should be driven by a clear button to give a user a chance to preserve logs...
        for (ConsoleView consoleView : consoleViewList) {
            consoleView.clear();
        }
    }


    protected ConsoleView createConsole(final Project project) {
        final TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
        final ConsoleView thisConsole = consoleBuilder.getConsole();
        consoleViewList.add(thisConsole);
        return thisConsole;
    }

    private class SendRequestAnimation implements Runnable {

        @Override
        public void run() {
            int i = 0;
            while (!processComplete) {
                if (i > 5) {
                    i = 0;
                    clearAllConsoles();
                    fillAllConsolesWithTheSameContent("Send request");
                } else {
                    String dots = "";
                    for (int j = 0; j < i; j++) {
                        dots += ".";
                    }
                    clearAllConsoles();
                    fillAllConsolesWithTheSameContent("Send request" + dots);
                }
                i++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
