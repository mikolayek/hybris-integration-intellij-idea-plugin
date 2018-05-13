package com.intellij.idea.plugin.hybris.type.system.meta;

import com.intellij.idea.plugin.hybris.type.system.model.EnumValue;
import org.jetbrains.annotations.Nullable;

/**
 * @author Eugene.Kudelevsky
 */
public interface TSMetaEnumValue {
    @Nullable
    String getName();

    @Nullable
    EnumValue retrieveDom();
}
