// Generated on Fri Nov 17 20:45:54 CET 2017
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.beans.model;

import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;

/**
 * null:abstractPojos interface.
 */
public interface AbstractPojos extends DomElement {

    /**
     * Returns the list of bean children.
     *
     * @return the list of bean children.
     */
    @NotNull
    java.util.List<Bean> getBeans();

    /**
     * Adds new child to the list of bean children.
     *
     * @return created child
     */
    Bean addBean();


    /**
     * Returns the list of enum children.
     *
     * @return the list of enum children.
     */
    @NotNull
    java.util.List<Enum> getEnums();

    /**
     * Adds new child to the list of enum children.
     *
     * @return created child
     */
    Enum addEnum();


}
