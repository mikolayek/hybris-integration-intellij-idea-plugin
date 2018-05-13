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

package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTable;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableFormatter;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public class FormatTableOperation extends AbstractOperation {


    public FormatTableOperation(final ImpexTableEditor editor) {
        super(editor);
    }

    @Override
    protected void perform() {
        final Pair<PsiElement, PsiElement> table = getSelectedTable(editor);
        if (table == null || table.first == null || table.second == null) {
            return;
        }
        final ImpexTable formattedTable = ImpexTableFormatter.format(table);
        editor.getIdeaEditor().getDocument().replaceString(
            formattedTable.getStartOffset(),
            formattedTable.getEndOffset(),
            formattedTable.getContent()
        );
    }


}
