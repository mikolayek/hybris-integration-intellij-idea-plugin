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
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.idea.plugin.hybris.tools.remote.http.impex.HybrisHttpResult;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

import static org.apache.commons.lang.StringUtils.center;
import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.http.HttpStatus.SC_MOVED_TEMPORARILY;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;


/**
 * refer @see HybrisConsole
 */
@Deprecated
public class ExecuteHybrisConsole extends ExecuteConsole {

    private static final String[] CONSOLE_NAMES = new String[]{"Hybris output console", "Error"};
    private static final String[] CONSOLE_DESCRIPTIONS = new String[]{"Hybris Message Result", "Hybris message errors"};
    private static final int FADEOUT_TIME = 5500;

    //    private ConsoleView executionStatisticsConsole;
    private ConsoleView searchResultConsole;
    private ConsoleView errorConsoleView;

    private static ExecuteHybrisConsole ourInstance = new ExecuteHybrisConsole();

    public static ExecuteHybrisConsole getInstance() {
        return ourInstance;
    }

    private ExecuteHybrisConsole() {
    }

    @Override
    public void show(final HybrisHttpResult httpResult, final Project project) {
        initConsoleWindow(project);
        startSendRequestAnimationInAllConsoles();

        ApplicationManager.getApplication().invokeLater(() -> {
            markRequestAsFinish();

            handleBadRequest(httpResult, project);

            if (httpResult.getStatusCode() == SC_OK) {
                if (!httpResult.hasError()) {
                    updateConsoleWindow(
                        httpResult.getOutput(),
                        ConsoleViewContentType.NORMAL_OUTPUT,
                        searchResultConsole
                    );
                    ConsoleToolWindowUtil.getInstance().selectTab(0);
                } else {
                    final StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
                    JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(
                        "Error validation impex",
                        MessageType.ERROR,
                        null
                    ).setFadeoutTime(FADEOUT_TIME)
                                  .createBalloon().show(
                        RelativePoint.getCenterOf(statusBar.getComponent()),
                        Balloon.Position.atRight
                    );
                    if (isEmpty(httpResult.getDetailMessage())) {
                        updateConsoleWindow(
                            httpResult.getErrorMessage(),
                            ConsoleViewContentType.ERROR_OUTPUT,
                            errorConsoleView
                        );
                    } else {
                        updateConsoleWindow(
                            httpResult.getErrorMessage() + "\n\n" + center(
                                " Details message ",
                                150,
                                "#"
                            ) + "\n\n" + httpResult.getDetailMessage(),
                            ConsoleViewContentType.ERROR_OUTPUT,
                            errorConsoleView
                        );
                    }
                    ConsoleToolWindowUtil.getInstance().selectTab(1);
                }
            }
        });
        ConsoleToolWindowUtil.getInstance().setConsoleName(CONSOLE_NAMES);
        ConsoleToolWindowUtil.getInstance().setConsoleDescription(CONSOLE_DESCRIPTIONS);
        ConsoleToolWindowUtil.getInstance().showConsoleToolWindow(
            project,
            searchResultConsole, errorConsoleView
        );
    }

    private void handleBadRequest(final HybrisHttpResult httpResult, final Project project) {
        if (httpResult.getStatusCode() != SC_OK) {
            final StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
            final String message;
            if (httpResult.getStatusCode() == SC_NOT_FOUND || httpResult.getStatusCode() == SC_MOVED_TEMPORARILY) {
                message = "Hybris Host URL '" + httpResult.getErrorMessage() + "' was incorrect. Please, check your settings.";
            } else {
                message = httpResult.getErrorMessage();
            }
            JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(
                message,
                MessageType.ERROR,
                null
            ).setFadeoutTime(FADEOUT_TIME)
                          .createBalloon().show(
                RelativePoint.getCenterOf(statusBar.getComponent()),
                Balloon.Position.atRight
            );
        }
    }


    @Override
    public void initConsoleWindow(final Project project) {
        searchResultConsole = createConsole(project);
        errorConsoleView = createConsole(project);
    }
}
