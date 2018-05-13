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

package com.intellij.idea.plugin.hybris.impex.folding.smart;

import com.intellij.idea.plugin.hybris.impex.constants.modifier.AttributeModifier;
import com.intellij.idea.plugin.hybris.impex.folding.ImpexFoldingPlaceholderBuilder;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexAttribute;
import com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils;
import com.intellij.psi.PsiElement;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * Created 23:16 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class SmartImpexFoldingPlaceholderBuilder implements ImpexFoldingPlaceholderBuilder {

    public static final String IMPEX_PARAMETERS_PLACEHOLDER = "()";
    private static final Pattern QUOTES_PATTERN = Pattern.compile("[\"\']");

    @NotNull
    @Override
    public String getPlaceholder(@NotNull final PsiElement psiElement) {
        Validate.notNull(psiElement);

        if (ImpexPsiUtils.isImpexAttribute(psiElement)) {
            final ImpexAttribute attribute = (ImpexAttribute) psiElement;

            return this.getPlaceholder(attribute);
        }

        if (ImpexPsiUtils.isImpexParameters(psiElement)) {
            return IMPEX_PARAMETERS_PLACEHOLDER;
        }

        return psiElement.getText();
    }

    @NotNull
    @Contract(pure = true)
    private String getPlaceholder(@NotNull final ImpexAttribute impexAttribute) {
        Validate.notNull(impexAttribute);

        if (this.isUniqueAttribute(impexAttribute)) {

            return impexAttribute.getAnyAttributeName().getText();

        } else if (this.isDefaultAttribute(impexAttribute)) {

            return this.getValueText(impexAttribute);

        } else if (this.isLangAttribute(impexAttribute)) {

            return this.getValueText(impexAttribute);

        } else if (this.isDateFormatAttribute(impexAttribute)) {

            return this.getValueText(impexAttribute);

        } else if (this.isModeAttribute(impexAttribute)) {

            return this.getValueText(impexAttribute);

        } else if (this.isNumberFormatAttribute(impexAttribute)) {

            return this.getValueText(impexAttribute);

        } else if (this.isTranslatorAttribute(impexAttribute)) {

            return this.getClassNameFromValue(impexAttribute);

        } else if (this.isCellDecoratorAttribute(impexAttribute)) {

            return this.getClassNameFromValue(impexAttribute);

        } else if (this.isVirtualAttribute(impexAttribute)) {

            return impexAttribute.getAnyAttributeName().getText();

        } else if (this.isAllowNullAttribute(impexAttribute)) {

            return impexAttribute.getAnyAttributeName().getText();

        } else if (this.isForceWriteAttribute(impexAttribute)) {

            return impexAttribute.getAnyAttributeName().getText();

        } else if (this.isIgnoreNullAttribute(impexAttribute)) {

            return impexAttribute.getAnyAttributeName().getText();

        } else if (this.isIgnoreKeyCaseAttribute(impexAttribute)) {

            return impexAttribute.getAnyAttributeName().getText();
        }

        return StringUtils.EMPTY;
    }

    @NotNull
    @Contract(pure = true)
    private String getClassNameFromValue(final @NotNull ImpexAttribute impexAttribute) {
        Validate.notNull(impexAttribute);

        if (null == impexAttribute.getAnyAttributeValue() || (StringUtils.isBlank(impexAttribute.getAnyAttributeValue()
                                                                                                .getText()))) {
            return impexAttribute.getText();
        }

        final String clearedString = QUOTES_PATTERN.matcher(impexAttribute.getAnyAttributeValue().getText()).replaceAll(
            StringUtils.EMPTY
        );

        final String className = StringUtils.substringAfterLast(clearedString, ".");

        return StringUtils.isBlank(className) ? impexAttribute.getAnyAttributeValue().getText() : className;
    }

    @NotNull
    @Contract(pure = true)
    private String getValueText(final @NotNull ImpexAttribute impexAttribute) {
        Validate.notNull(impexAttribute);

        if (null == impexAttribute.getAnyAttributeValue() || (StringUtils.isBlank(impexAttribute.getAnyAttributeValue()
                                                                                                .getText()))) {
            return impexAttribute.getText();
        }

        return impexAttribute.getAnyAttributeValue().getText();
    }

    @Contract(pure = true)
    private boolean isUniqueAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, AttributeModifier.UNIQUE.getModifierName());
    }

    @Contract(pure = true)
    private boolean isVirtualAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, AttributeModifier.VIRTUAL.getModifierName());
    }

    @Contract(pure = true)
    private boolean isAllowNullAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, AttributeModifier.ALLOW_NULL.getModifierName());
    }

    @Contract(pure = true)
    private boolean isForceWriteAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, AttributeModifier.FORCE_WRITE.getModifierName());
    }

    @Contract(pure = true)
    private boolean isIgnoreNullAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, AttributeModifier.IGNORE_NULL.getModifierName());
    }

    @Contract(pure = true)
    private boolean isIgnoreKeyCaseAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, AttributeModifier.IGNORE_KEY_CASE.getModifierName());
    }

    @Contract(pure = true)
    private boolean isDateFormatAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(
            AttributeModifier.DATE_FORMAT.getModifierName(), impexAttribute.getAnyAttributeName().getText()
        );
    }

    @Contract(pure = true)
    private boolean isDefaultAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(
            AttributeModifier.DEFAULT.getModifierName(), impexAttribute.getAnyAttributeName().getText()
        );
    }

    @Contract(pure = true)
    private boolean isLangAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(
            AttributeModifier.LANG.getModifierName(), impexAttribute.getAnyAttributeName().getText()
        );
    }

    @Contract(pure = true)
    private boolean isTranslatorAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(
            AttributeModifier.TRANSLATOR.getModifierName(), impexAttribute.getAnyAttributeName().getText()
        );
    }

    @Contract(pure = true)
    private boolean isNumberFormatAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(
            AttributeModifier.NUMBER_FORMAT.getModifierName(), impexAttribute.getAnyAttributeName().getText()
        );
    }

    @Contract(pure = true)
    private boolean isModeAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(
            AttributeModifier.MODE.getModifierName(), impexAttribute.getAnyAttributeName().getText()
        );
    }

    @Contract(pure = true)
    private boolean isCellDecoratorAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(
            AttributeModifier.CELL_DECORATOR.getModifierName(), impexAttribute.getAnyAttributeName().getText()
        );
    }

    @Contract(pure = true)
    private boolean isNameEqualsAndAndValueIsTrue(
        @NotNull final ImpexAttribute impexAttribute,
        @NotNull final String name
    ) {
        Validate.notNull(impexAttribute);
        Validate.notNull(name);

        if (!this.quoteAwareStringEquals(name, impexAttribute.getAnyAttributeName().getText())) {
            return false;
        }

        if (null == impexAttribute.getAnyAttributeValue()) {
            return false;
        }

        final String value = impexAttribute.getAnyAttributeValue().getText();

        if (null == value) {
            return false;
        }

        final String trimmedValue = value.trim();

        return this.quoteAwareStringEquals("true", trimmedValue);
    }

    @Contract(pure = true)
    private boolean quoteAwareStringEquals(@Nullable final String quotedString, @Nullable final String value) {
        return (null == quotedString) == (null == value)
               && (null == quotedString
                   || quotedString.equals(value)
                   || ('\'' + quotedString + '\'').equals(value)
                   || ('"' + quotedString + '"').equals(value));

    }
}
