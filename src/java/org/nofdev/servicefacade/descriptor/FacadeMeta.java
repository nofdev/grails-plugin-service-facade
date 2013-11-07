package org.nofdev.servicefacade.descriptor;

import java.util.Map;

/**
 * Meta data that describe a facade service
 */
public class FacadeMeta {
    private String name;

    private String description;

    private Map<String, MethodMeta> methods;

    /**
     * name of the service
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * description of the service
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * the methods of this service, key is method name
     */
    public Map<String, MethodMeta> getMethods() {
        return methods;
    }

    public void setMethods(Map<String, MethodMeta> methods) {
        this.methods = methods;
    }
}
