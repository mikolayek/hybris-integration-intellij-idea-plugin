package com.intellij.idea.plugin.hybris.tools.remote.console.view

import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisFSConsole
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisGroovyConsole
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisImpexConsole
import com.intellij.idea.plugin.hybris.tools.remote.console.actions.HybrisExecuteImmediatelyAction
import com.intellij.idea.plugin.hybris.tools.remote.console.actions.HybrisSuspendAction
import com.intellij.idea.plugin.hybris.tools.remote.console.actions.handler.HybrisConsoleExecuteActionHandler
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.CommonShortcuts
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.JBTabsPaneImpl
import icons.JetgroovyIcons.Groovy.Groovy_16x16
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.SwingConstants.TOP


/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
class HybrisConsolePanel(val project: Project) : SimpleToolWindowPanel(true), Disposable {
    
    override fun dispose() {
        
    }

    private val impexConsole = HybrisImpexConsole(project)
    private val fsConsole = HybrisFSConsole(project)
    private val groovyConsole = HybrisGroovyConsole(project)

    init {
        layout = BorderLayout()

        val toolbarActions = DefaultActionGroup()
        val actionManager = ActionManager.getInstance()
        val actionToolbar = actionManager.createActionToolbar(ActionPlaces.UNKNOWN, toolbarActions, false)

        val panel = JPanel(BorderLayout())
        
        val tabsPane = HybrisTabs(impexConsole, fsConsole, groovyConsole, project, TOP)

        panel.add(tabsPane.component, BorderLayout.CENTER)
        actionToolbar.setTargetComponent(tabsPane.component)
        panel.add(actionToolbar.component, BorderLayout.WEST)

        val actionHandler = HybrisConsoleExecuteActionHandler(project, false)
        val executeAction = HybrisExecuteImmediatelyAction(tabsPane, actionHandler)
        executeAction.registerCustomShortcutSet(CommonShortcuts.ALT_ENTER, this.component)
        
        toolbarActions.add(executeAction)
        toolbarActions.add(HybrisSuspendAction(tabsPane, actionHandler))

        toolbarActions.addAll(*impexConsole.createConsoleActions())
        add(panel)
    }

    override fun getComponent() = super.getComponent()!!
}

class HybrisTabs(private val impexConsole: HybrisImpexConsole,
                 private val fsConsole: HybrisFSConsole,
                 private val groovyConsole: HybrisGroovyConsole,
                 project: Project,
                 tabPlacement: Int) : JBTabsPaneImpl(project, tabPlacement, Disposable {  }) {
    init {
        insertTab("Impex", HybrisIcons.IMPEX_FILE, impexConsole.component, "Impex Console", 0)
        insertTab("FlexibleSearch", HybrisIcons.FS_FILE, fsConsole.component, "FlexibleSearch Console", 1)
        insertTab("Groovy Scripting", Groovy_16x16, groovyConsole.component, "Groovy Console", 2)
    }

    fun activeConsole() = when (selectedIndex) {
        0 -> impexConsole
        1 -> fsConsole
        2 -> groovyConsole
        else -> impexConsole
    } 
}