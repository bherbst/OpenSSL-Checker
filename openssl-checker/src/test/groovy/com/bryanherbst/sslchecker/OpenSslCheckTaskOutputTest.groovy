package com.bryanherbst.sslchecker

import org.gradle.testfixtures.ProjectBuilder

class OpenSslCheckTaskOutputTest extends GroovyTestCase {
    def outStream = new ByteArrayOutputStream()

    void setUp() {
        System.setOut(new PrintStream(outStream))
    }

    void tearDown() {
        System.setOut(null)
    }

    void testMultipleRealSources() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('openSslCheck', type: OpenSslCheckTask)

        def versions = ["1.0.1r" : ["source1", "source2"]]

        task.reportOpenSslVersions(versions)


        def output = outStream.toString()
        def lines = output.readLines()

        assert lines.get(0).equals("Found OpenSSL version 1.0.1r in:")
        assert lines.get(1).equals("\t- source1")
        assert lines.get(2).equals("\t- source2")
    }
}
