package org.nofdev.servicefacade.descriptor;

/**
 * a named value
 */
public class NamedValueMeta extends ValueMeta {
    private String name;

    /**
     * the name of value
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
