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

package com.intellij.idea.plugin.hybris.flexibleSearch.completion.provider;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.completion.analyzer.FSKeywordTableClauseAnalyzer;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Function;

public class FSKeywordCompletionProvider extends CompletionProvider<CompletionParameters> {

    @NotNull
    private final Set<String> keywords;

    @NotNull
    private final Function<String, LookupElement> func;

    public FSKeywordCompletionProvider(
        @NotNull final Set<String> keywords,
        @NotNull final Function<String, LookupElement> func
    ) {
        this.keywords = keywords;
        this.func = func;
    }

    @Override
    protected void addCompletions(
        @NotNull final CompletionParameters parameters,
        final ProcessingContext processingContext,
        @NotNull final CompletionResultSet completionResultSet
    ) {
        final FSKeywordTableClauseAnalyzer keywordAnalyzer = FSKeywordTableClauseAnalyzer.INSTANCE;
        keywordAnalyzer.analyzeKeyword(parameters, completionResultSet);
    }
}
