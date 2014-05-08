package org.nofdev.servicefacade.grails.exporter

import grails.converters.JSON
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONElement
import org.codehaus.groovy.grails.web.json.JSONObject
import org.joda.time.DateTime

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType
import javax.transaction.NotSupportedException

class SimpleJSONDeserializer {

    protected static final Log log = LogFactory.getLog(SimpleJSONDeserializer)

    Type[] paramTypes

    def handelJSONElement(JSONElement element, Type type) {
        log.trace("handelJSONElement:<$element> of type:<$type>")

        if (element instanceof JSONArray) {
            log.trace("element is JSONArray")
            handelJSONArray(element as JSONArray, type)
        }
        else if (element instanceof JSONObject) {
            log.trace("element is JSONObject")
            handelJSONObject(element as JSONObject, type as Class)
        }
    }

    def handelJSONPart(Object part, Type type) {
        log.trace("handelJSONPart:<$part> of type:<$type>")
        if (part instanceof JSONElement) {
            log.trace("instance of JSONElement")
            handelJSONElement(part as JSONElement, type)
        } else if (part instanceof JSONObject.Null) {
            log.trace('element is JSONObject.Null')
            null
        } else if (type.enum) {
            log.trace('element is an instance of enum')
            if (part instanceof String) {
                if (((String)part).trim() == "")
                    return null
                type.valueOf(part)
            }
            else
                throw new NotSupportedException('Cannot convert to enum except String value!')
        } else if (type == DateTime) {
            log.trace('element is jodatime.DateTime')
            if (part instanceof String) {
                if (((String)part).trim() == "")
                    return null
                DateTime.parse(part)
            }
            else
                throw new NotSupportedException('Cannot convert to jodatime.DateTime except String value!')
        } else {
            log.trace("return $part")
            part
        }
    }

    def handelJSONArray(JSONArray array, Type type) {
        log.trace("handelJSONArray:<$array> of type:<$type>")
        if (!type instanceof ParameterizedType) throw new NotSupportedException("Must specify generic type param of List in DTO!")
        Type elementType = (type as ParameterizedType).actualTypeArguments[0]
        if (elementType instanceof WildcardType) throw new NotSupportedException("Generic WildcardType is not supported!")
        array.collect { handelJSONPart it, elementType }
    }

    def handelJSONObject(JSONObject obj, Class type) {
        log.trace("handelJSONObject:<$obj> and creating instance of $type")
        def newInstance = type.newInstance()
        obj.entrySet().each { entry ->
            if (entry.key != "class") {
                Type fieldType = type.getDeclaredField(entry.key).genericType
                log.trace("copy fields- name:$entry.key type:$fieldType value:$entry.value")
                newInstance."${entry.key}" = handelJSONPart(entry.value, fieldType)
            }
        }
        log.trace("return instance $newInstance")
        newInstance
    }

    def deserialize(String rawParams) {
        JSONArray methodParams = JSON.parse(rawParams)

        List params = new ArrayList()
        methodParams.eachWithIndex { param, i ->
            params.add(handelJSONPart(param, paramTypes[i]))
        }
        log.debug("methodParams transformed to: ${params}")

        params
    }
}
