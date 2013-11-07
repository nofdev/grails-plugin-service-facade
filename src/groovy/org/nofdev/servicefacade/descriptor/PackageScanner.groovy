package org.nofdev.servicefacade.descriptor

import groovy.transform.CompileStatic

@CompileStatic
class PackageScanner {
    static List<PackageMeta> Scan(List<String> packageNames) {
        def packages = [:]
//        List<Class> classes = []
//        packageNames.each { classes.addAll(PackageUtil.getClasses(it)) }
//        def facadeInterfaces = classes.findAll {
//            it.isInterface() && it.simpleName.endsWith('Facade')
//        }
//        def dtos = []
        for(String p : packageNames) {

        }
        return packages.values()
    }
}
