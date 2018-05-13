package com.intellij.idea.plugin.hybris.project.utils;

import com.google.common.collect.Lists;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class FileUtils {

    private FileUtils() throws IllegalAccessException {
        throw new IllegalAccessException("Util class should never be instantiated.");
    }

    public static boolean isFileUnder(@NotNull final File child, @NotNull final File parent) {
        return Paths.get(child.getAbsolutePath()).startsWith(parent.getAbsolutePath());
    }

    @NotNull
    public static List<String> getPathToParentDirectoryFrom(
        @NotNull final File file,
        @NotNull final File parentDirectory
    ) throws IOException {
        if (!FileUtils.isFileUnder(file, parentDirectory)) {
            throw new IOException("File '" + file + "' is not under '" + parentDirectory + "'");
        }

        final List<String> path = new ArrayList<>();
        File currentDirectory = file.getParentFile();

        while (!FileUtil.filesEqual(currentDirectory, parentDirectory)) {
            path.add(currentDirectory.getName());
            currentDirectory = currentDirectory.getParentFile();
        }

        return Lists.reverse(path);
    }
}
