// Generated on Fri Nov 17 20:45:54 CET 2017
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.beans.model;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.Required;
import org.jetbrains.annotations.NotNull;

/**
 * null:bean interface.
 */
public interface Bean extends DomElement, AbstractPojo {

    /**
     * Returns the value of the extends child.
     *
     * @return the value of the extends child.
     */
    @NotNull
    GenericAttributeValue<String> getExtends();


    /**
     * Returns the value of the type child.
     * <pre>
     * <h3>Attribute null:type documentation</h3>
     * Defines the type of bean. Allowed are 'bean' or 'event'.
     * </pre>
     *
     * @return the value of the type child.
     */
    @NotNull
    GenericAttributeValue<String> getType();


    /**
     * Returns the value of the deprecated child.
     * <pre>
     * <h3>Attribute null:deprecated documentation</h3>
     * Marks bean as deprecated. Allows defining a message.
     * </pre>
     *
     * @return the value of the deprecated child.
     */
    @NotNull
    GenericAttributeValue<String> getDeprecated();


    /**
     * Returns the value of the abstract child.
     *
     * @return the value of the abstract child.
     */
    @NotNull
    GenericAttributeValue<Boolean> getAbstract();


    /**
     * Returns the value of the superEquals child.
     *
     * @return the value of the superEquals child.
     */
    @NotNull
    GenericAttributeValue<Boolean> getSuperEquals();


    /**
     * Returns the value of the class child.
     *
     * @return the value of the class child.
     */
    @NotNull
    @Attribute("class")
    @Required
    GenericAttributeValue<String> getClazz();


    /**
     * Returns the value of the template child.
     *
     * @return the value of the template child.
     */
    @NotNull
    GenericAttributeValue<String> getTemplate();


    /**
     * Returns the value of the description child.
     *
     * @return the value of the description child.
     */
    @NotNull
    GenericDomValue<String> getDescription();


    /**
     * Returns the list of import children.
     *
     * @return the list of import children.
     */
    @NotNull
    java.util.List<Import> getImports();

    /**
     * Adds new child to the list of import children.
     *
     * @return created child
     */
    Import addImport();


    /**
     * Returns the value of the annotations child.
     *
     * @return the value of the annotations child.
     */
    @NotNull
    Annotations getAnnotations();


    /**
     * Returns the list of property children.
     *
     * @return the list of property children.
     */
    @NotNull
    java.util.List<Property> getProperties();

    /**
     * Adds new child to the list of property children.
     *
     * @return created child
     */
    Property addProperty();


}
