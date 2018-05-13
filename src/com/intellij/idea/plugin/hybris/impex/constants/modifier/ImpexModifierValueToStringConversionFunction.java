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

import com.google.common.base.Function;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

/**
 * Created 20:34 14 May 2016
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexModifierValueToStringConversionFunction implements Function<ImpexModifierValue, String> {

    private static final Function<ImpexModifierValue, String> INSTANCE = new ImpexModifierValueToStringConversionFunction();

    public static Function<ImpexModifierValue, String> getInstance() {
        return INSTANCE;
    }

    protected ImpexModifierValueToStringConversionFunction() {
    }

    @SuppressWarnings("StandardVariableNames")
    @Nullable
    @Override
    public String apply(@Nullable final ImpexModifierValue f) {
        Validate.notNull(f);

        return f.getModifierValue();
    }
}
