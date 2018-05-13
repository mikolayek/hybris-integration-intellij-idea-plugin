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

package com.intellij.idea.plugin.hybris.ant;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by Martin Zdarsky-Jones on 16/2/17.
 */
public class AntGenResult implements Serializable {

    private static final long serialVersionUID = 45234594386590854L;

    private final String platformDir;
    private final HashSet<String> extensionsToAdd = new HashSet<>();
    private final HashSet<String> extensionsToRemove = new HashSet<>();

    public AntGenResult(final String platformDir) {
        this.platformDir = platformDir;
    }

    public HashSet<String> getExtensionsToAdd() {
        return extensionsToAdd;
    }

    public HashSet<String> getExtensionsToRemove() {
        return extensionsToRemove;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final AntGenResult that = (AntGenResult) o;

        return platformDir.equals(that.platformDir);
    }

    @Override
    public int hashCode() {
        return platformDir.hashCode();
    }
}
