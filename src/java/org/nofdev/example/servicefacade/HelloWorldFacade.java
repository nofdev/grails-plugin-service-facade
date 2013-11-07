package org.nofdev.example.servicefacade;

import org.nofdev.servicefacade.PagedList;
import org.nofdev.servicefacade.Paginator;
import org.nofdev.servicefacade.FacadeApi;

/**
 * This service is a service facade demonstration.
 */
public interface HelloWorldFacade {
    /**
     * Say hello to a user based on their age.
     * @param userDTO user info
     * @return greeting
     */
    @FacadeApi(params = {"userDTO"})
    String sayHelloTo(UserDTO userDTO);

    /**
     * Get all attend users in current instance.
     * @param paginator the pager
     * @return paged list of users
     */
    @FacadeApi(params = {"paginator"})
    PagedList<UserDTO> getAllAttendUsers(Paginator paginator);
}
