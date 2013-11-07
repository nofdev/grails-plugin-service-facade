package org.nofdev.servicefacade.grails.exporter

import org.nofdev.servicefacade.grails.exporter.SimpleJSONDeserializer
import org.nofdev.servicefacade.PagedList
import grails.converters.JSON

import java.lang.reflect.Type

class FacadeControllerTests extends GroovyTestCase {

    void testForTicket144() {
        def list = new PagedList(
                pageSize: 10,
                currentPage: 2,
                totalCount: 1000,
                totalPage: 100
        )
        list.list = [1, 2, 3, 5, 7]

        assertTrue((list as JSON).toString().contains("list"))
    }

    public void methodWithGenericParams(List<String> strings) {}

    void testForTicket185() {
        def json = "[[\"1\"]]"
        def deserializer = new SimpleJSONDeserializer()
        Type[] paramTypes = FacadeControllerTests.class.methods.find {
            it.name.equals("methodWithGenericParams")
        }.getGenericParameterTypes()
        println paramTypes
        deserializer.paramTypes = paramTypes
        ArrayList obj = deserializer.deserialize(json)

        assertEquals(1, obj[0].size())
        assertEquals("1", obj[0][0])
    }

}
