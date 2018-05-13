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

package com.intellij.idea.plugin.hybris.impex.tableFormatting;

import com.intellij.idea.plugin.hybris.impex.formatting.ImpexCodeStyleSettings;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexAnyHeaderMode;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexFullHeaderParameter;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexHeaderTypeName;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexValue;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexValueLine;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.intellij.idea.plugin.hybris.impex.utils.CommonPsiUtils.getNextSiblingOfAnyType;
import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isHeaderLine;
import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isImpexValueGroup;
import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isImpexValueLine;
import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isParameterSeparator;
import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isWhiteSpace;
import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfAnyType;
import static com.intellij.psi.util.PsiTreeUtil.getChildOfType;
import static com.intellij.psi.util.PsiTreeUtil.getChildrenOfType;
import static com.intellij.psi.util.PsiTreeUtil.getNextSiblingOfType;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public final class ImpexTableFormatter {

    private ImpexTableFormatter() {
    }

    public static ImpexTable format(final Pair<PsiElement, PsiElement> table) {

        final int[] maxColumnWidth = calculateMaxWidth(table, table.first);

        final StringBuilder sb = createNewTable(table, maxColumnWidth);

        final int startOffset = table.first.getTextRange().getStartOffset();
        final int endOffset = table.second.getTextRange().getEndOffset();

        return new ImpexTable(startOffset, endOffset, sb.toString());
    }


    @NotNull
    private static StringBuilder createNewTable(
        final Pair<PsiElement, PsiElement> table,
        final int[] maxColumnWidth
    ) {
        final StringBuilder sb = new StringBuilder();
        PsiElement currentValueLine = table.first;
        final CodeStyleSettings settings = CodeStyleSettingsManager.getSettings(table.first.getProject());
        final ImpexCodeStyleSettings impexSettings = settings.getCustomSettings(ImpexCodeStyleSettings.class);
        boolean keepProcess = true;
        while (keepProcess) {
            if (currentValueLine == null) {
                break;
            }
            if (isHeaderLine(currentValueLine)) {
                writeHeader(table.first, maxColumnWidth, sb, impexSettings);
            } else if (isImpexValueLine(currentValueLine)) {
                writeValueLine(maxColumnWidth, sb, currentValueLine, impexSettings);
            } else {
                sb.append(currentValueLine.getText().replaceAll(" ", ""));
            }

            if (table.second.equals(currentValueLine)) {
                keepProcess = false;
                continue;
            }
            currentValueLine = getNextSiblingOfAnyType(currentValueLine, PsiElement.class);
        }
        return sb;
    }

    private static void writeValueLine(
        final int[] maxColumnWidth,
        final StringBuilder sb,
        final PsiElement currentValueLine,
        final ImpexCodeStyleSettings impexSettings
    ) {
        if (isImpexValueGroup(currentValueLine.getFirstChild())) {
            sb.append(StringUtils.rightPad("", maxColumnWidth[0] + 1));
        } else {
            final String text = StringUtils.rightPad(
                currentValueLine.getFirstChild().getText().trim(),
                maxColumnWidth[0]
            );
            sb.append(text);
        }
        final PsiElement[] children = currentValueLine.getChildren();

        int i = 1;
        for (final PsiElement element : children) {
            final int length = maxColumnWidth.length - 1;
            if (isFirstFieldValueIsEmpty(element)) {
                addSeparator(impexSettings, ";", sb);
                sb.append(StringUtils.rightPad("", maxColumnWidth[min(i, length
                )]));
            } else {
                addSeparator(impexSettings, ";", sb);
                sb.append(StringUtils.rightPad(
                    element.getLastChild().getText().trim(),
                    maxColumnWidth[min(i, length)]
                ));
            }
            i++;
        }
    }

    private static void addSeparator(
        final ImpexCodeStyleSettings impexSettings,
        final String text,
        final StringBuilder sb
    ) {
        if (impexSettings.SPACE_BEFORE_PARAMETERS_SEPARATOR) {
            sb.append(' ');
        }
        sb.append(text);
        if (impexSettings.SPACE_AFTER_PARAMETERS_SEPARATOR) {
            sb.append(' ');
        }
    }

    private static boolean isFirstFieldValueIsEmpty(final PsiElement element) {
        final PsiElement[] childrenOfType = getChildrenOfType(element, PsiElement.class);
        return childrenOfType != null && childrenOfType.length == 1;
    }

    private static void writeHeader(
        final PsiElement header,
        final int[] maxColumnWidth,
        final StringBuilder sb,
        final ImpexCodeStyleSettings impexSettings
    ) {
        final Collection<PsiElement> columns = findChildrenOfAnyType(
            header,
            ImpexAnyHeaderMode.class,
            ImpexFullHeaderParameter.class,
            ImpexHeaderTypeName.class,
            PsiElement.class
        );

        int i = 1;
        for (PsiElement column : columns) {
            if (isWhiteSpace(column)) {
                continue;
            }

            if (isParameterSeparator(column)) {
                addSeparator(impexSettings, column.getText(), sb);

            } else if (column instanceof ImpexAnyHeaderMode
                       || column instanceof ImpexHeaderTypeName
                       || column instanceof ImpexFullHeaderParameter) {

                if (column instanceof ImpexAnyHeaderMode
                    || column instanceof ImpexHeaderTypeName) {

                    sb.append(column.getText());
                    if (column instanceof ImpexAnyHeaderMode) {
                        sb.append(' ');
                    }
                } else {
                    final int width = maxColumnWidth[i];
                    if (i == maxColumnWidth.length - 1) {
                        sb.append(column.getText());
                    } else {
                        sb.append(StringUtils.rightPad(column.getText().trim(), width));
                    }
                    i++;
                }
            }
        }
    }

    private static int[] calculateMaxWidth(final Pair<PsiElement, PsiElement> table, @NotNull final PsiElement header) {
        final Collection<PsiElement> columns = findChildrenOfAnyType(
            header,
            ImpexAnyHeaderMode.class,
            ImpexFullHeaderParameter.class,
            ImpexHeaderTypeName.class,
            PsiWhiteSpace.class
        );

        final int[] maxColumnWidth = new int[header.getChildren().length - 1];

        int k = 0;
        for (PsiElement column : columns) {
            if (column.getPrevSibling() != null && isParameterSeparator(column.getPrevSibling())) {
                k++;
            }
            if (!(column instanceof PsiWhiteSpace)) {
                maxColumnWidth[k] += column.getText().replaceAll(";", "").length();
            }
        }


        PsiElement currentValueLine = getNextSiblingOfType(header, ImpexValueLine.class);
        boolean keepProcessing = true;
        while (keepProcessing) {
            if (currentValueLine == null) {
                break;
            }
            if (!isImpexValueGroup(currentValueLine.getFirstChild())) {
                final int textLength = currentValueLine.getFirstChild().getText().replace(";", "").length();
                maxColumnWidth[0] = max(textLength, maxColumnWidth[0]);
            }
            final PsiElement[] children = currentValueLine.getChildren();

            int i = 0;
            for (final PsiElement element : children) {
                if (i == header.getChildren().length) {
                    break;
                }
                i++;
                if (!isFirstFieldValueIsEmpty(element)) {
                    final int textLength = getChildOfType(element, ImpexValue.class).getTextLength();
                    maxColumnWidth[i] = max(textLength, maxColumnWidth[i]);
                }
            }

            if (table.second.equals(currentValueLine)) {
                keepProcessing = false;
                continue;
            }
            currentValueLine = getNextSiblingOfType(currentValueLine, ImpexValueLine.class);
        }
        return maxColumnWidth;
    }
}
