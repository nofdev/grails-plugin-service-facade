package org.nofdev.example.servicefacade

import org.nofdev.example.servicefacade.HelloWorldFacade
import org.nofdev.example.servicefacade.UserDTO
import org.nofdev.servicefacade.PagedList
import org.nofdev.servicefacade.Paginator

/**
 * Grails implementation of this facade service.
 */
class HelloWorldFacadeService implements HelloWorldFacade {

    def transactional = false

    def attendenceCache = []

    String sayHelloTo(UserDTO userDTO) {
        def adv = ''
        if(userDTO.age < 10) adv = 'little '
        else if(userDTO.age < 60) adv = ''
        else if(userDTO.age >= 60) adv = 'old '

        attendenceCache.push(userDTO)

        "Hello $adv$userDTO.name!"
    }

    PagedList<UserDTO> getAllAttendUsers(Paginator paginator) {
        PagedList.wrap(attendenceCache)
    }
}
