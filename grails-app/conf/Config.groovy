import grails.gorm.PagedResultList
import org.nofdev.servicefacade.grails.PagedResultListCategory

// configuration for plugin testing - will not be included in the plugin zip

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    appenders {
        console name: 'stdout',
                layout: pattern(conversionPattern: '%d %p - %c.%M(%L) - %m%n')
    }


    root {
        all 'stdout'
    }

    error stdout: 'StackTrace'

    error 'Digester',
            'org.codehaus.groovy.grails.web.binding',  //  controllers
            'org.codehaus.groovy.grails.web.servlet',        // controllers
            'org.codehaus.groovy.grails.web.pages',          // GSP
            'org.codehaus.groovy.grails.web.sitemesh',       // layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping',        // URL mapping
            'org.codehaus.groovy.grails.commons',            // core / classloading
            'org.codehaus.groovy.grails.plugins',            // plugins
            'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
            'net.sf.ehcache',
            'org.apache',
            'notexistsforend'

//    warn   'org.mortbay.log'

    debug 'notexistsforend',
//            'org.springframework',
            'org.hibernate.SQL',
            'notexistsforend'

    trace 'notexistsforend',
//          'org.hibernate.type',
            'notexistsforend'

    environments {
        production {
            root {
                error 'stdout'
            }
            debug 'notexistsforend',
//                    'org.springframework',
                    'org.hibernate.SQL',
                    'grails.app.controllers.org.nofdev.grailsservice.exporter.FacadeController'
            'notexistsforend'
        }
//            development {
//                all    'grails',
//                       'org.hibernate',
//                       'hibernate'
//
//                error  'Digester'
//            }
    }
}

grails.views.javascript.library="jquery"
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"

grails.plugin.servicefacade.scanlist = ['org.nofdev.example.servicefacade']

PagedResultList.mixin PagedResultListCategory