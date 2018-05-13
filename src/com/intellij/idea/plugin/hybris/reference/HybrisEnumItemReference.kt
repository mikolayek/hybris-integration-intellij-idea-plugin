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

package com.intellij.idea.plugin.hybris.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.PsiShortNamesCache

/**
 * @author Nosov Aleksandr
 */
class HybrisEnumItemReference(element: PsiElement, soft: Boolean) : PsiReferenceBase<PsiElement>(element, soft), PsiPolyVariantReference {

    private val QUOTE_LENGTH = 2

    override fun getRangeInElement(): TextRange = TextRange.from(1, element.textLength - QUOTE_LENGTH)

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project = myElement.project
        val enumJavaModelName = myElement.text.replace("\"".toRegex(), "")

        val javaModelClasses = PsiShortNamesCache.getInstance(project)
                .getClassesByName(enumJavaModelName, GlobalSearchScope.allScope(project))

        return PsiElementResolveResult.createResults(*javaModelClasses)
    }

    override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun getVariants(): Array<out PsiReference> = PsiReference.EMPTY_ARRAY

}
