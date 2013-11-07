package org.nofdev.servicefacade.descriptor;

import java.util.Map;

/**
 * Meta data of a method in a service
 */
public class MethodMeta {
    private String name;

    private String description;

    private Map<String, NamedValueMeta> parameters;

    private ValueMeta returnValue;

    /**
     * name of the method
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * description of the method
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * parameters' meta data of this method, key is parameter name
     */
    public Map<String, NamedValueMeta> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, NamedValueMeta> parameters) {
        this.parameters = parameters;
    }

    /**
     * meta data of the return value
     */
    public ValueMeta getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(ValueMeta returnValue) {
        this.returnValue = returnValue;
    }
}
