package com.bryanherbst.sslchecker;

import org.gradle.testfixtures.ProjectBuilder;

class OpenSslCheckerPluginTest extends GroovyTestCase {

    void testNotAndroidProject() {
        def project = ProjectBuilder.builder().build()

        shouldFail(UnsupportedOperationException) {
            new OpenSslCheckerPlugin().apply(project)
        }
    }

    void testAndroidProject() {
        def project = ProjectBuilder.builder().build()
        project.plugins.apply('com.android.application')

        new OpenSslCheckerPlugin().apply(project)
    }

    void testAndroidLibraryProject() {
        def project = ProjectBuilder.builder().build()
        project.plugins.apply('com.android.library')

        new OpenSslCheckerPlugin().apply(project)
    }

}