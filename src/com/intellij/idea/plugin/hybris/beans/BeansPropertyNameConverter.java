package com.intellij.idea.plugin.hybris.beans;

import com.intellij.idea.plugin.hybris.beans.model.Property;
import com.intellij.psi.PsiField;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.ResolvingConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public class BeansPropertyNameConverter extends ResolvingConverter<PsiField> {

    @Nullable
    @Override
    public PsiField fromString(@Nullable final String text, final ConvertContext context) {
        DomElement host = context.getInvocationElement();
        if (host instanceof GenericAttributeValue<?>) {
            DomElement domProperty = host.getParent();
            return domProperty instanceof Property ? BeansUtils.resolveToPsiField((Property) domProperty, text) : null;
        }
        return null;
    }

    @NotNull
    @Override
    public Collection<? extends PsiField> getVariants(final ConvertContext context) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public String toString(@Nullable final PsiField psiField, final ConvertContext context) {
        return psiField == null ? "" : psiField.getName();
    }
}
