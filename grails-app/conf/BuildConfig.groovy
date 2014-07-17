grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    // configure settings for the run-app JVM
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        mavenRepo "http://nexus.sp.cn/nexus/content/groups/public/"
        grailsRepo "http://nexus.sp.cn/nexus/content/repositories/plugins.grails.org/"

        grailsCentral()
        mavenLocal()
        mavenCentral()
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        // runtime 'mysql:mysql-connector-java:5.1.24'
        test 'org.jetbrains.groovy.grails.rt:intellij-grails-rt:13.1'//解决intellij在fork环境下无法运行测试的问题
    }

    plugins {
        build ":release:3.0.1"
        build(":rest-client-builder:1.0.3",
              ":tomcat:7.0.42"
        ) {
            export = false
        }

        runtime(":resources:1.2.1") {
            export = false
        }


        compile ":joda-time:1.4"

        runtime ":jquery:1.10.2"
    }

    grails.project.repos.default = "myRepo"
    grails.project.repos.myRepo.url = "http://nexus.sp.cn/nexus/content/repositories/grails-plugin/"
    grails.project.repos.myRepo.type = "maven"
    grails.project.repos.myRepo.username = "admin"
    grails.project.repos.myRepo.password = "admin123"
}
