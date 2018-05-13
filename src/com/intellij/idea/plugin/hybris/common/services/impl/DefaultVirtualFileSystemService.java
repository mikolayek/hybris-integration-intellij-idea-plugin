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

package com.intellij.idea.plugin.hybris.common.services.impl;

import com.intellij.idea.plugin.hybris.common.services.VirtualFileSystemService;
import com.intellij.idea.plugin.hybris.project.tasks.TaskProgressProcessor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static com.intellij.openapi.util.io.FileUtil.normalize;

/**
 * Created 1:51 AM 13 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultVirtualFileSystemService implements VirtualFileSystemService {

    @Override
    public void removeAllFiles(@NotNull final Collection<File> files) throws IOException {
        Validate.notNull(files);

        if (files.isEmpty()) {
            return;
        }

        final LocalFileSystem localFileSystem = LocalFileSystem.getInstance();

        for (File file : files) {
            final VirtualFile virtualFile = localFileSystem.findFileByIoFile(file);

            if (null != virtualFile) {
                ApplicationManager.getApplication().runWriteAction(new RemoveFileComputable(virtualFile));
            } else {
                FileUtil.delete(file);
            }
        }
    }

    private static class RemoveFileComputable implements ThrowableComputable<Void, IOException> {

        private final VirtualFile virtualFile;

        public RemoveFileComputable(@NotNull final VirtualFile virtualFile) {
            Validate.notNull(virtualFile);

            this.virtualFile = virtualFile;
        }

        @Override
        public Void compute() throws IOException {
            this.virtualFile.delete(this);

            return null;
        }
    }

    @Override
    public VirtualFile getByUrl(@NotNull final String url) {
        Validate.notNull(url);
        return VirtualFileManager.getInstance().findFileByUrl(url);
    }

    @Override
    @Nullable
    public File findFileByNameInDirectory(
        @NotNull final File directory,
        @NotNull final String fileName,
        @Nullable final TaskProgressProcessor<File> progressListenerProcessor
    ) throws InterruptedException {
        Validate.notNull(directory);
        Validate.isTrue(directory.isDirectory());
        Validate.notNull(fileName);

        final Ref<File> result = Ref.create();
        final Ref<Boolean> interrupted = Ref.create(false);

        FileUtil.processFilesRecursively(directory, file -> {
            if (progressListenerProcessor != null && !progressListenerProcessor.shouldContinue(directory)) {
                interrupted.set(true);
                return false;
            }
            if (StringUtils.endsWith(file.getAbsolutePath(), fileName)) {
                result.set(file);
                return false;
            }
            return true;
        });

        if (interrupted.get()) {
            throw new InterruptedException("Modules scanning has been interrupted.");
        }
        return result.get();
    }

    @Override
    public boolean fileContainsAnother(@NotNull final File parent, @NotNull final File child) {
        Validate.notNull(parent);
        Validate.notNull(child);

        return this.pathContainsAnother(parent.getAbsolutePath(), child.getAbsolutePath());
    }

    @Override
    public boolean fileDoesNotContainAnother(@NotNull final File parent, @NotNull final File child) {
        Validate.notNull(parent);
        Validate.notNull(child);

        return !(this.fileContainsAnother(parent, child));
    }

    @Override
    public boolean pathContainsAnother(@NotNull final String parent, @NotNull final String child) {
        Validate.notBlank(parent);
        Validate.notBlank(child);

        return StringUtils.startsWith(normalize(child), normalize(parent));
    }

    @Override
    public boolean pathDoesNotContainAnother(@NotNull final String parent, @NotNull final String child) {
        Validate.notBlank(parent);
        Validate.notBlank(child);

        return !(this.pathContainsAnother(parent, child));
    }

    @Nonnull
    @Override
    public String getRelativePath(@NotNull final File parent, @NotNull final File child) {
        Validate.notNull(parent);
        Validate.notNull(child);

        return this.getRelativePath(parent.getPath(), child.getPath());
    }

    @Nonnull
    @Override
    public String getRelativePath(@NotNull final String parent, @NotNull final String child) {
        Validate.notBlank(parent);
        Validate.notBlank(child);

        return normalize(child).substring(normalize(parent).length());
    }
}
