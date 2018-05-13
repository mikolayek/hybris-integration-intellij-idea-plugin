package com.intellij.idea.plugin.hybris.tools.remote.console

import com.intellij.execution.console.ConsoleHistoryController
import com.intellij.execution.console.ConsoleRootType
import com.intellij.execution.console.LanguageConsoleImpl
import com.intellij.idea.plugin.hybris.flexibleSearch.FlexibleSearchLanguage
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage
import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.groovy.GroovyLanguage

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
abstract class HybrisConsole(project: Project, title: String, language: Language) : LanguageConsoleImpl(project, title, language)

class HybrisImpexConsole(project: Project) : HybrisConsole(project, "Hybris Impex Console", ImpexLanguage.getInstance()) {
    object MyConsoleRootType : ConsoleRootType("hybris.impex.shell", null)

    init {
        ConsoleHistoryController(MyConsoleRootType, "hybris.impex.shell", this).install()
    }
}

class HybrisFSConsole(project: Project) : HybrisConsole(project, "Hybris FS Console", FlexibleSearchLanguage.getInstance()) {
    object MyConsoleRootType : ConsoleRootType("hybris.fs.shell", null)

    init {
        ConsoleHistoryController(MyConsoleRootType, "hybris.fs.shell", this).install()
    }
}

class HybrisGroovyConsole(project: Project) : HybrisConsole(project, "Hybris Groovy Console", GroovyLanguage) {
    object MyConsoleRootType : ConsoleRootType("hybris.groovy.shell", null)

    init {
        ConsoleHistoryController(MyConsoleRootType, "hybris.groovy.shell", this).install()
    }
}

