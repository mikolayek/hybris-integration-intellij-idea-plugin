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

package com.intellij.idea.plugin.hybris.impex.constants.modifier;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.intellij.idea.plugin.hybris.impex.constants.ImpexConstants.ModifierCommonValues.BOOLEAN;
import static com.intellij.idea.plugin.hybris.impex.constants.ImpexConstants.ModifierCommonValues.NONE;

/**
 * https://wiki.hybris.com/pages/viewpage.action?title=ImpEx+Syntax&spaceKey=release5
 */
public enum AttributeModifier implements ImpexModifier {
    UNIQUE("unique", BOOLEAN),
    ALLOW_NULL("allownull", BOOLEAN),
    FORCE_WRITE("forceWrite", BOOLEAN),
    IGNORE_KEY_CASE("ignoreKeyCase", BOOLEAN),
    IGNORE_NULL("ignorenull", BOOLEAN),
    VIRTUAL("virtual", BOOLEAN),
    MODE("mode", ModeModifierValue.values()),
    ALIAS("alias", NONE),
    CELL_DECORATOR("cellDecorator", NONE),
    COLLECTION_DELIMITER("collection-delimiter", NONE),
    DATE_FORMAT("dateformat", NONE),
    DEFAULT("default", NONE),
    KEY_2_VALUE_DELIMITER("key2value-delimiter", NONE),
    LANG("lang", NONE),
    MAP_DELIMITER("map-delimiter", NONE),
    NUMBER_FORMAT("numberformat", NONE),
    PATH_DELIMITER("path-delimiter", NONE),
    POS("pos", NONE),
    TRANSLATOR("translator", NONE);

    private final String modifierName;
    private final List<String> modifierValues;

    private static final Map<String, ImpexModifier> ELEMENTS_MAP = new HashMap<String, ImpexModifier>(
        AttributeModifier.values().length
    );

    static {
        for (ImpexModifier impexModifier : AttributeModifier.values()) {
            ELEMENTS_MAP.put(impexModifier.getModifierName(), impexModifier);
        }
    }

    @Nullable
    public static ImpexModifier getByModifierName(@NotNull final String modifierName) {
        Validate.notEmpty(modifierName);

        return ELEMENTS_MAP.get(modifierName);
    }

    AttributeModifier(
        @NotNull final String modifierName,
        @NotNull final ImpexModifierValue[] modifierValues
    ) {
        Validate.notEmpty(modifierName);
        Validate.notNull(modifierValues);

        this.modifierName = modifierName;

        if (ArrayUtils.isEmpty(modifierValues)) {
            this.modifierValues = Collections.emptyList();
        } else {
            this.modifierValues = Lists.transform(
                Arrays.asList(modifierValues), ImpexModifierValueToStringConversionFunction.getInstance()
            );
        }
    }

    @NotNull
    @Override
    public String getModifierName() {
        return this.modifierName;
    }

    @NotNull
    @Override
    public List<String> getModifierValues() {
        return this.modifierValues;
    }
}
