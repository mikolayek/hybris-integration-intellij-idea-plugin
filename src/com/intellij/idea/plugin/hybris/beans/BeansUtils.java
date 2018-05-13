package com.intellij.idea.plugin.hybris.beans;

import com.intellij.idea.plugin.hybris.beans.model.Bean;
import com.intellij.idea.plugin.hybris.beans.model.Property;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ObjectUtils;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class BeansUtils {

    public static <T extends DomElement, V> GenericAttributeValue<V> expectDomAttributeValue(
        @NotNull final PsiElement element,
        @NotNull final Class<? extends T> domTagClass,
        @NotNull final Function<T, GenericAttributeValue<V>> domGetter
    ) {
        final DomManager domManager = DomManager.getDomManager(element.getProject());

        if (!(element instanceof XmlElement)) {
            return null;
        }

        final XmlAttribute xmlAttribute = PsiTreeUtil.getParentOfType(element, XmlAttribute.class, false);
        if (xmlAttribute == null) {
            return null;
        }

        final XmlTag xmlParentTag = PsiTreeUtil.getParentOfType(element, XmlTag.class, false);
        DomElement domParentTag = domManager.getDomElement(xmlParentTag);

        return Optional.ofNullable(domParentTag)
                       .map(o -> ObjectUtils.tryCast(o, domTagClass))
                       .map(domGetter)
                       .filter(val -> val == domManager.getDomElement(xmlAttribute))
                       .orElse(null);
    }

    public static PsiField resolveToPsiField(@NotNull final Property property) {
        return resolveToPsiField(property, property.getName().getStringValue());
    }

    public static PsiField resolveToPsiField(@NotNull final Property property, @Nullable final String name) {
        if (StringUtil.isEmptyOrSpaces(name)) {
            return null;
        }

        if (!(property.getParent() instanceof Bean)) {
            return null;
        }
        final Bean bean = (Bean) property.getParent();
        final GenericAttributeValue<String> clazz = bean.getClazz();
        if (!clazz.exists()) {
            return null;
        }

        final XmlAttributeValue xmlValue = clazz.getXmlAttributeValue();
        if (xmlValue == null) {
            return null;
        }
        return Arrays.stream(xmlValue.getReferences())
                     .map(PsiReference::resolve)
                     .map(o -> ObjectUtils.tryCast(o, PsiClass.class))
                     .filter(Objects::nonNull)
                     .map(nextClass -> nextClass.findFieldByName(name, false))
                     .filter(Objects::nonNull)
                     .findAny()
                     .orElse(null);
    }

}
