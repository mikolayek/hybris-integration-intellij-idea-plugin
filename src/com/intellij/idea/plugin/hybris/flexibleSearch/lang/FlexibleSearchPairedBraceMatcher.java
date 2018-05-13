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

package com.intellij.idea.plugin.hybris.flexibleSearch.lang;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LEFT_BRACE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LEFT_BRACKET;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LEFT_DOUBLE_BRACE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LEFT_PAREN;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.RIGHT_BRACE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.RIGHT_BRACKET;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.RIGHT_DOUBLE_BRACE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.RIGHT_PAREN;

public class FlexibleSearchPairedBraceMatcher implements PairedBraceMatcher {

    private static final BracePair[] PAIRS = new BracePair[]{
        new BracePair(LEFT_BRACE, RIGHT_BRACE, false),
        new BracePair(LEFT_BRACKET, RIGHT_BRACKET, false),
        new BracePair(LEFT_DOUBLE_BRACE, RIGHT_DOUBLE_BRACE, false),
        new BracePair(LEFT_PAREN, RIGHT_PAREN, false),
    };

    public BracePair[] getPairs() {
        return PAIRS;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(
        @NotNull final IElementType lbraceType, @Nullable final IElementType contextType
    ) {
        return true;
    }

    @Override
    public int getCodeConstructStart(final PsiFile file, final int openingBraceOffset) {
        return openingBraceOffset;
    }
}