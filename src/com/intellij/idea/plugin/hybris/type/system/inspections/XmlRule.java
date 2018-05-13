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

package com.intellij.idea.plugin.hybris.type.system.inspections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface XmlRule {

    enum Priority {
        HIGH, MEDIUM, LOW;

        @Nullable
        public static Priority fromAcronym(final String acronym) {
            switch (acronym) {
                case "H":
                    return HIGH;
                case "M":
                    return MEDIUM;
                case "L":
                    return LOW;
                default:
                    return null;
            }
        }

    }

    @NotNull
    String getID();

    @NotNull
    String getDescription();

    @NotNull
    Priority getPriority();

    @Nullable
    String getSelectionXPath();

    @Nullable
    String getNameXPath();

    @Nullable
    String getTestXPath();

    boolean isFailOnTestQuery();

}
