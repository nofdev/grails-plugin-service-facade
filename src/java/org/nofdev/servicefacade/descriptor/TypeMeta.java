package org.nofdev.servicefacade.descriptor;

import java.util.List;

/**
 * Represent a concrete type info
 */
public class TypeMeta {
    private String name;

    private Boolean isTKV;

    private List<TypeMeta> generics;

    /**
     * type name
     * eg.) ArrayList
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * indicate this type meta represent a type argument of a generic type
     */
    public Boolean getTKV() {
        return isTKV;
    }

    public void setTKV(Boolean TKV) {
        isTKV = TKV;
    }

    /**
     * generic parameters if any
     * eg.) [{name: "String"}, {name: "Map"}]
     */
    public List<TypeMeta> getGenerics() {
        return generics;
    }

    public void setGenerics(List<TypeMeta> generics) {
        this.generics = generics;
    }
}
