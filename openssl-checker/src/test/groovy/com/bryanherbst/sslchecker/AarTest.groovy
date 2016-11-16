package com.bryanherbst.sslchecker

import org.gradle.testfixtures.ProjectBuilder

class AarTest extends GroovyTestCase {

    void testVulnerableAar() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('checkSsl', type: OpenSslCheckTask)
        def aar = new File('./src/test/resources/lib-vulnerable.aar')

        task.findOpenSslVersionsInFile(aar)

        assert task.openSslVersions.containsKey("1.0.1c")
    }

    void testNoSslAar() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('checkSsl', type: OpenSslCheckTask)
        def aar = new File('./src/test/resources/lib-no-ssl.aar')

        task.findOpenSslVersionsInFile(aar)

        assert task.openSslVersions.size() == 0
    }

}