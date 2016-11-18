package com.bryanherbst.sslchecker

import com.android.build.gradle.api.BaseVariantOutput
import org.gradle.testfixtures.ProjectBuilder

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class OpenSslCheckTaskTest extends GroovyTestCase {

    void testTaskFailsWhenNotApkOrAar() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('checkSsl', type: OpenSslCheckTask)

        def file = mock(File.class)
        when(file.getName()).thenReturn("foo.bar")

        def output = mock(BaseVariantOutput.class)
        when(output.getOutputFile()).thenReturn(file)


        task.androidOutput = output

        shouldFailWithCause(IllegalArgumentException) {
            task.checkSsl()
        }
    }

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

    void testRegisterUnknownFirst() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('openSslCheck', type: OpenSslCheckTask)

        task.registerVersion("1.0.1r", "unknown")
        def sources = task.openSslVersions.get("1.0.1r")

        assert sources.size() == 1
        assert sources.contains("unknown")
    }

    void testRealSourceReplacesUnknown() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('openSslCheck', type: OpenSslCheckTask)

        task.registerVersion("1.0.1r", "unknown")
        task.registerVersion("1.0.1r", "real_source")
        def sources = task.openSslVersions.get("1.0.1r")

        assert sources.size() == 1
        assert sources.contains("real_source")
    }

    void testUnknownDoesNotReplaceRealSource() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('openSslCheck', type: OpenSslCheckTask)

        task.registerVersion("1.0.1r", "real_source")
        task.registerVersion("1.0.1r", "unknown")
        def sources = task.openSslVersions.get("1.0.1r")

        assert sources.size() == 1
        assert sources.contains("real_source")
    }

    void testMultipleRealSources() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('openSslCheck', type: OpenSslCheckTask)

        task.registerVersion("1.0.1r", "real_source")
        task.registerVersion("1.0.1r", "other_real_source")
        def sources = task.openSslVersions.get("1.0.1r")

        assert sources.size() == 2
        assert sources.contains("real_source")
        assert sources.contains("other_real_source")
    }
}
