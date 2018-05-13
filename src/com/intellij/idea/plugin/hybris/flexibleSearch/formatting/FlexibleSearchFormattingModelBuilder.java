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

package com.intellij.idea.plugin.hybris.flexibleSearch.formatting;


import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.idea.plugin.hybris.flexibleSearch.FlexibleSearchLanguage;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlexibleSearchFormattingModelBuilder implements FormattingModelBuilder {

    @NotNull
    @Override
    public FormattingModel createModel(
        final PsiElement element, final CodeStyleSettings settings
    ) {
        final Block block = new FlexibleSearchBlock(
            element.getNode(),
            null,
            Alignment.createAlignment(),
            createSpaceBuilder(settings)
        );

        return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), block, settings);
    }

    private static SpacingBuilder createSpaceBuilder(final CodeStyleSettings settings) {

        return new SpacingBuilder(settings, FlexibleSearchLanguage.getInstance())
//            .after(FlexibleSearchTypes.COMP_OP)
//            .spaceIf(true)
//
//            .before(FlexibleSearchTypes.COMP_OP)
//            .spaceIf(true)
//
//            .after(FlexibleSearchTypes.LEFT_DOUBLE_BRACE)
//            .spaceIf(true)
//
//            .before(FlexibleSearchTypes.RIGHT_DOUBLE_BRACE)
//            .spaceIf(true)
//
//            .before(FlexibleSearchTypes.LEFT_BRACE)
//            .spaceIf(true)
//
//            .after(FlexibleSearchTypes.RIGHT_BRACE)
//            .spaceIf(true)
//
//            .before(FlexibleSearchTypes.LEFT_PAREN)
//            .spaceIf(true)
//
//            .after(FlexibleSearchTypes.RIGHT_PAREN)
//            .spaceIf(true)
//
//            .around(TokenSet.create(FlexibleSearchTypes.WHERE, FlexibleSearchTypes.WHERE_CLAUSE))
//            .spaceIf(true)
            
            .before(TokenSet.create(FlexibleSearchTypes.SELECT))
            .spaces(2, true)
            
            .around(TokenSet.create(FlexibleSearchTypes.QUERY_SPECIFICATION))
            .none()
            ;
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(
        final PsiFile file, final int offset, final ASTNode elementAtOffset
    ) {
        return null;
    }
}
