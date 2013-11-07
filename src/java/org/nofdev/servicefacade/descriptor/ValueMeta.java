package org.nofdev.servicefacade.descriptor;

/**
 * Represent a value of a type
 */
public class ValueMeta {
    private String description;

    private TypeMeta type;

    /**
     * description of this value
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * type info of this value
     */
    public TypeMeta getType() {
        return type;
    }

    public void setType(TypeMeta type) {
        this.type = type;
    }
}
