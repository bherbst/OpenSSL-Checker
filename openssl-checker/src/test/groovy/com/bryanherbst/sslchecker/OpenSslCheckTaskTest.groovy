package com.bryanherbst.sslchecker

import org.gradle.testfixtures.ProjectBuilder

class OpenSslCheckTaskTest extends GroovyTestCase {

    void testParseFileLine() {
        def project = ProjectBuilder.builder().build()
        def checkTask = project.task('openSslCheck', type: OpenSslCheckTask)

        def line = "/Users/badssl/openssl-1.0.1m/badapk"
        def (version, source) = checkTask.getOpenSslVersionAndSource(line)

        assertEquals "1.0.1m", version
        assertEquals "/Users/badssl/openssl-1.0.1m", source
    }

    void testParseUnkownLine() {
        def project = ProjectBuilder.builder().build()
        def checkTask = project.task('openSslCheck', type: OpenSslCheckTask)

        def line = "Yay we found OpenSSL 1.0.1m! Look at that!"
        def (version, source) = checkTask.getOpenSslVersionAndSource(line)

        assertEquals "1.0.1m", version
        assertEquals "unknown", source
    }

    void testParseLineWihoutOpenSsl() {
        def project = ProjectBuilder.builder().build()
        def checkTask = project.task('openSslCheck', type: OpenSslCheckTask)

        def line = "Nothing to see here"
        def (version, source) = checkTask.getOpenSslVersionAndSource(line)

        assertEquals null, version
        assertEquals null, source
    }

    void testVulnerable100Version() {
        def project = ProjectBuilder.builder().build()
        def checkTask = project.task('openSslCheck', type: OpenSslCheckTask)

        assertTrue checkTask.isVersionVulnerable("1.0.0a")
    }

    void testVulnerable101Version() {
        def project = ProjectBuilder.builder().build()
        def checkTask = project.task('openSslCheck', type: OpenSslCheckTask)

        assertTrue checkTask.isVersionVulnerable("1.0.1a")
    }

    void testGood101Version() {
        def project = ProjectBuilder.builder().build()
        def checkTask = project.task('openSslCheck', type: OpenSslCheckTask)

        assertFalse checkTask.isVersionVulnerable("1.0.1r")
    }

    void testVulnerable102Version() {
        def project = ProjectBuilder.builder().build()
        def checkTask = project.task('openSslCheck', type: OpenSslCheckTask)

        assertTrue checkTask.isVersionVulnerable("1.0.2a")
    }

    void testGood102Version() {
        def project = ProjectBuilder.builder().build()
        def checkTask = project.task('openSslCheck', type: OpenSslCheckTask)

        assertFalse checkTask.isVersionVulnerable("1.0.2f")
    }

    void testFailOnInsecureVersion() {
        def project = ProjectBuilder.builder().build()
        def checkTask = project.task('openSslCheck', type: OpenSslCheckTask)

        def versions = ["1.0.0a" : ["unknown"] ]

        shouldFailWithCause(InsecureSslVersionException) {
            checkTask.failOnSslVulnerabilityFound(versions)
        }
    }
}
