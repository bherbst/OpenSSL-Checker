package com.bryanherbst.sslchecker

import com.android.build.gradle.api.BaseVariantOutput
import org.gradle.testfixtures.ProjectBuilder

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class AarTest extends GroovyTestCase {

    void testTaskRunsOnAar() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('checkSsl', type: OpenSslCheckTask)
        def aar = new File('./src/test/resources/lib-no-ssl.aar')

        def output = mock(BaseVariantOutput.class)
        when(output.getOutputFile()).thenReturn(aar)

        task.androidOutput = output
        task.checkSsl()
    }

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