package org.nofdev.servicefacade.grails.exporter

import org.nofdev.utils.jvm.PackageUtil
import grails.converters.JSON
import grails.util.Environment


import java.lang.reflect.Type

class FacadeController {

    def grailsApplication

    def getCtx() { grailsApplication.mainContext }

    def index = {}

    def json = {
        final endl = System.properties.'line.separator'
        if (params.containsKey("wsdl")) return redirect(action: jsonWsdl, params: params.wsdl ? [lib: params.wsdl] : null)
        def result = [callId: UUID.randomUUID().toString(), val: null, err: null]
        def start = new Date()
        String methodName = ''
        String facadeName = ''
        String packageName = ''
        try {
            facadeName = params.facadeName + 'Facade'
            String facadeBeanName = "${facadeName[0].toLowerCase()}${facadeName[1..-1]}FacadeService"
            methodName = params.methodName
            packageName = params.packageName
            final interfaceName = packageName + '.' + facadeName
            log.debug("ask instance for interface($interfaceName)")
            Class<?> interfaceClazz = Class.forName(interfaceName, true, Thread.currentThread().contextClassLoader)
            def service = ctx.getBean(interfaceClazz)

            String rawParams = params.params.toString()
            log.debug("${endl}JSON facade call(callId:${result.callId}): $interfaceName.$facadeName.$methodName$rawParams")

            if (params.params) {
                Class<?> serviceClazz = service.getClass()
                def unproxiedServiceClazz = serviceClazz.methods.find{it.name.equals("getTargetClass")}?.invoke(service)?:serviceClazz
                Type[] paramTypes = unproxiedServiceClazz.methods.find {it.name.equals(methodName)}.getGenericParameterTypes()
                log.trace "paramTypes: ${paramTypes.inspect()}"

                List methodParams = deserializeParamList(rawParams, paramTypes)
                log.debug "deserialized to: ${methodParams.inspect()}"
                result.val = service."$methodName"(* methodParams)
            }
            else {
                result.val = service."$methodName"()
            }
        }
        catch (e) {
            log.warn("${endl}JSON facade call error: ${e.message}!", e)
            result.err = formatException(e)
        }
        def json = result as JSON
        def end = new Date()
        long millis = end.time - start.time
        def slow = ''
        if(millis > 500) slow = "${endl}SLOW!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!${endl}"
        log.debug("${endl}${packageName}.$facadeName.$methodName result($slow$millis ms$slow): $result ->${endl}$json")
        render(json)
    }

    private List deserializeParamList(String rawParams, Type[] paramTypes) {
        def deserializer = new SimpleJSONDeserializer()
        deserializer.paramTypes = paramTypes
        deserializer.deserialize(rawParams)
    }

    def jsonWsdl = {
        List<String> packageNames = grailsApplication.config.grails.plugin.servicefacade.scanlist
        log.debug "Generate JSON WSDL for package: $packageNames"
        //List<PackageMeta> packages = PackageScanner.Scan(packageNames)
        List<Class> classes = []
        packageNames.each { classes.addAll(PackageUtil.getClasses(it)) }
        def serviceInterfaces = classes.findAll {
            it.isInterface() && it.simpleName.endsWith('Facade')
        }
        response.addHeader("Cache-Control", "max-age=43200")
        [services: serviceInterfaces, baseUrl: request.contextPath]
    }

    private def formatException(Throwable throwable) {
        if (throwable == null) return null
        def pretty = [:]
        pretty.name = throwable.getClass().name
        pretty.msg = throwable.localizedMessage
        if (Environment.developmentMode) pretty.stack = throwable.stackTrace.toString()
        pretty.cause = formatException(throwable.cause)
        pretty
    }

    /**
     * returns a descriptor contains all Facade API information. eg)
     * [facades: [],
     * types: []]
     */
    private def getFacadeDescriptor() {

    }
}


