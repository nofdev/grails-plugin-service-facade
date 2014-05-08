package org.nofdev.servicefacade.grails

import grails.gorm.PagedResultList
import org.nofdev.servicefacade.PagedList
import org.nofdev.servicefacade.Paginator

@Category(PagedResultList)
class PagedResultListCategory {
    public <E> PagedList<E> asPage(Paginator paginator, Closure toDto = null) {
        new PagedList<E>(this.getTotalCount(), paginator, toDto ? this.collect(toDto) : this)
    }
}
