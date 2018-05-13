package com.intellij.idea.plugin.hybris.tools.remote.console.actions.handler


import com.intellij.execution.console.ConsoleHistoryController
import com.intellij.execution.ui.ConsoleViewContentType.*
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisConsole
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisFSConsole
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisGroovyConsole
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisImpexConsole
import com.intellij.idea.plugin.hybris.tools.remote.console.view.HybrisTabs
import com.intellij.idea.plugin.hybris.tools.remote.http.HybrisHacHttpClient
import com.intellij.idea.plugin.hybris.tools.remote.http.impex.HybrisHttpResult.HybrisHttpResultBuilder.createResult
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import java.util.concurrent.TimeUnit


/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
class HybrisConsoleExecuteActionHandler(private val project: Project,
                                        private val preserveMarkup: Boolean) {

    private fun processLine(console: HybrisConsole, text: String) {
        ApplicationManager.getApplication().runReadAction {
            ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Execute HTTP Call to Hybris...") {
                override fun run(indicator: ProgressIndicator) {
                    isProcessRunning = true
                    try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(5))

                        val client = HybrisHacHttpClient()
                        val httpResult =
                                when (console) {
                                    is HybrisFSConsole -> client.executeFlexibleSearch(project, text)
                                    is HybrisImpexConsole -> client.importImpex(project, text)
                                    is HybrisGroovyConsole -> client.executeGroovyScript(project, text)
                                    else -> null
                                }

                        val result = createResult().output(httpResult?.output).detailMessage(httpResult?.detailMessage).build()
                        val detailMessage = result.detailMessage
                        val output = result.output

                        console.print("[RESULT] \n", SYSTEM_OUTPUT)
                        val outputType = if (result.hasError()) ERROR_OUTPUT else NORMAL_OUTPUT
                        console.print("$output\n$detailMessage", outputType)
                    } finally {
                        isProcessRunning = false
                    }
                }
            })
        }
    }

    fun runExecuteAction(tabbedPane: HybrisTabs) {

        val console = tabbedPane.activeConsole()
        val consoleHistoryModel = ConsoleHistoryController.getController(console)

        execute(console, consoleHistoryModel)
    }

    private fun execute(console: HybrisConsole,
                        consoleHistoryController: ConsoleHistoryController) {

        // Process input and add to history
        val document = console.currentEditor.document
        val text = document.text
        val range = TextRange(0, document.textLength)

        if (text.isNotEmpty()) {
            console.currentEditor.selectionModel.setSelection(range.startOffset, range.endOffset)
            console.addToHistory(range, console.consoleEditor, preserveMarkup)
            console.setInputText("")
            if (!StringUtil.isEmptyOrSpaces(text)) {
                consoleHistoryController.addToHistory(text.trim())
            }

            processLine(console, text)
        }
    }

    var isProcessRunning: Boolean = false
}
