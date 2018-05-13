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

package com.intellij.idea.plugin.hybris.common.services;

import com.intellij.idea.plugin.hybris.project.tasks.TaskProgressProcessor;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created 11:40 PM 10 February 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface VirtualFileSystemService {

    void removeAllFiles(@NotNull Collection<File> files) throws IOException;

    VirtualFile getByUrl(@NotNull String url);

    @Nullable
    File findFileByNameInDirectory(
        @NotNull File directory,
        @NotNull String fileName,
        @Nullable TaskProgressProcessor<File> progressListenerProcessor
    ) throws InterruptedException;

    boolean fileContainsAnother(@NotNull File parent, @NotNull File child);

    boolean fileDoesNotContainAnother(@NotNull File parent, @NotNull File child);

    boolean pathContainsAnother(@NotNull String parent, @NotNull String child);

    boolean pathDoesNotContainAnother(@NotNull String parent, @NotNull String child);

    @Nonnull
    String getRelativePath(@NotNull File parent, @NotNull File child);

    @Nonnull
    String getRelativePath(@NotNull String parent, @NotNull String child);
}
