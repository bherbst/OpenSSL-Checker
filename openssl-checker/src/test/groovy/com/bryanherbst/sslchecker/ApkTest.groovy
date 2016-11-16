package com.bryanherbst.sslchecker

import org.gradle.testfixtures.ProjectBuilder

class ApkTest extends GroovyTestCase {

    void testVulnerableApk() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('checkSsl', type: OpenSslCheckTask)
        def aar = new File('./src/test/resources/app-vulnerable.apk')

        task.findOpenSslVersionsInFile(aar)

        assert task.openSslVersions.containsKey("1.0.1c")
    }

    void testNoSslApk() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('checkSsl', type: OpenSslCheckTask)
        def aar = new File('./src/test/resources/app-no-ssl.apk')

        task.findOpenSslVersionsInFile(aar)

        assert task.openSslVersions.size() == 0
    }

}