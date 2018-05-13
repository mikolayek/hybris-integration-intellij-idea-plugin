package com.intellij.idea.plugin.hybris.tools.remote.console.view

import com.intellij.execution.ui.RunnerLayoutUi
import com.intellij.execution.ui.layout.PlaceInGrid
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisConsoleToolWindowFactory
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import javax.swing.JPanel


/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */

class HybrisConsolePanelView(val project: Project) : Disposable {

    companion object {
        fun getInstance(project: Project): HybrisConsolePanelView = ServiceManager.getService(project, HybrisConsolePanelView::class.java)
    }
    
    private val consolePanel = HybrisConsolePanel(project)
    private val panel = JPanel()


    /**
     * Creats the tool window content
     * @param toolWindow
     */
    fun createToolWindowContent(toolWindow: ToolWindow) {
        //Create runner UI layout
        val factory = RunnerLayoutUi.Factory.getInstance(project)
        val layoutUi = factory.create("", "", "session", project)

        val console = layoutUi.createContent(HybrisConsoleToolWindowFactory.ID, consolePanel.component, " (Experimental)", null, null)
        layoutUi.addContent(console, 0, PlaceInGrid.right, false)

        val layoutComponent = layoutUi.component
        panel.add(layoutComponent, BorderLayout.CENTER)
        val content = ContentFactory.SERVICE.getInstance().createContent(layoutComponent, null, true)
        toolWindow.contentManager.addContent(content)
    }

    override fun dispose() {
    }
}
