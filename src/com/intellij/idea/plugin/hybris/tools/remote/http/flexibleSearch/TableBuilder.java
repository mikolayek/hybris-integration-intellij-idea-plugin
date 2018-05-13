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

package com.intellij.idea.plugin.hybris.tools.remote.http.flexibleSearch;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class TableBuilder {

    private List<String[]> rows = new LinkedList<String[]>();

    public void addRow(String... cols) {
        rows.add(cols);
    }

    private int[] colWidths() {
        int cols = -1;

        for (String[] row : rows)
            cols = Math.max(cols, row.length);

        int[] widths = new int[cols];

        for (String[] row : rows) {
            for (int colNum = 0; colNum < row.length; colNum++) {
                widths[colNum] =
                    Math.max(
                        widths[colNum],
                        StringUtils.length(row[colNum])
                    );
            }
        }

        return widths;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();

        int[] colWidths = colWidths();

        for (String[] row : rows) {
            for (int colNum = 0; colNum < row.length; colNum++) {
                buf.append(
                    StringUtils.rightPad(
                        StringUtils.defaultString(
                            row[colNum]), colWidths[colNum]));
                buf.append("| ");
            }

            buf.append('\n');
        }

        return buf.toString();
    }

}
