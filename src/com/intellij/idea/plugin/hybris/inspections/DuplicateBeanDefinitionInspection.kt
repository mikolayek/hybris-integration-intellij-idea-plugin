package com.intellij.idea.plugin.hybris.inspections

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.XmlSuppressableInspectionTool
import com.intellij.codeInspection.ex.UnfairLocalInspectionTool
import com.intellij.idea.plugin.hybris.common.HybrisConstants.HYBRIS_BEANS_XML_FILE_ENDING
import com.intellij.idea.plugin.hybris.inspections.util.BeanDefinitionCountHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.util.PsiUtilCore
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlFile

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
class DuplicateBeanDefinitionInspection : XmlSuppressableInspectionTool(), UnfairLocalInspectionTool {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : XmlElementVisitor() {
            override fun visitXmlAttributeValue(value: XmlAttributeValue) {
                if (value.textRange.isEmpty) {
                    return
                }
                val file = value.containingFile as? XmlFile ?: return
                val baseFile = PsiUtilCore.getTemplateLanguageFile(file)
                if (baseFile != null) {
                    if (baseFile !== file && baseFile !is XmlFile) {
                        return
                    }

                    if (baseFile.name.notEndsWith(HYBRIS_BEANS_XML_FILE_ENDING)) {
                        return
                    }
                }
                val refHolder = BeanDefinitionCountHolder.getCountHolder(file) ?: return

                checkValue(value, refHolder, holder)
            }
        }
    }

    private fun checkValue(value: XmlAttributeValue, beanRefHolder: BeanDefinitionCountHolder, holder: ProblemsHolder) {
        if (beanRefHolder.isDuplicateBeanDefinition(value)) {
            holder.registerProblem(value, "Bean definition duplicated", ProblemHighlightType.WEAK_WARNING)

            val duplicatedAttributes = beanRefHolder.getDuplicatedAttributes(value)
            if (duplicatedAttributes.isNotEmpty()) {
                duplicatedAttributes.forEach {holder.registerProblem(it, "Duplicate definition of attribute", ProblemHighlightType.GENERIC_ERROR)}
            }
        }
    }
    
    private fun String.notEndsWith(suffix: String) = !this.endsWith(suffix)
}


