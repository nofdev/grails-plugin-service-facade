package org.nofdev.servicefacade.descriptor;

import java.util.List;

/**
 * Type detail info, including properties.
 */
public class TypeDetail {
    private String name;

    private String description;

    private Boolean isGeneric;

    private List<String> generics;

    /**
     * type name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * type description
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * is or not a generic type
     */
    public Boolean getGeneric() {
        return isGeneric;
    }

    public void setGeneric(Boolean generic) {
        isGeneric = generic;
    }

    /**
     * generics
     * eg.) ["T", "K"]
     */
    public List<String> getGenerics() {
        return generics;
    }

    public void setGenerics(List<String> generics) {
        this.generics = generics;
    }
}
