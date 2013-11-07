package service.facade

import org.nofdev.example.servicefacade.UserDTO

class HelloWorldFacadeServiceTest extends GroovyTestCase {
    def helloWorldFacadeService

    void testSayHelloTo() {
        assertEquals('Hello little Peter!', helloWorldFacadeService.sayHelloTo(new UserDTO(name: 'Peter', age: 3)))
    }

    void testGetAllAttendUsers() {
    }
}
