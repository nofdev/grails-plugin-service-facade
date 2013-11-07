package org.nofdev.servicefacade.descriptor;

import java.util.Map;

/**
 * Meta data for a package or namespace
 */
public class PackageMeta {
    private String name;

    private Map<String, FacadeMeta> facades;

    /**
     * name of the package
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * All services contained in this package, key is facade name
     */
    public Map<String, FacadeMeta> getFacades() {
        return facades;
    }

    public void setFacades(Map<String, FacadeMeta> facades) {
        this.facades = facades;
    }
}
