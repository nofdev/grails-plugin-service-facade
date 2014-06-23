package org.nofdev.utils.jvm

import spock.lang.Specification

/**
 * Created by Qiang on 6/18/14.
 */
class PackageUtilSpec extends Specification{
    def setup() {
    }

    def cleanup() {
    }

    void "简单测试对org.nofdev.utils.jvm.scantest包进行扫描"(){
        expect:
        PackageUtil.getClasses("org.nofdev.utils.jvm.scantest").size()==2
    }
}
