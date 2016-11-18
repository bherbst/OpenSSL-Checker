package com.bryanherbst.sslchecker

import com.android.build.gradle.api.BaseVariantOutput;
import org.gradle.testfixtures.ProjectBuilder

import static org.mockito.Mockito.*;

class ApkTest extends GroovyTestCase {

    void testTaskRunsOnApk() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('checkSsl', type: OpenSslCheckTask)
        def apk = new File('./src/test/resources/app-no-ssl.apk')

        def output = mock(BaseVariantOutput.class)
        when(output.getOutputFile()).thenReturn(apk)

        task.androidOutput = output
        task.checkSsl()
    }

    void testVulnerableApk() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('checkSsl', type: OpenSslCheckTask)
        def apk = new File('./src/test/resources/app-vulnerable.apk')

        task.findOpenSslVersionsInFile(apk)

        assert task.openSslVersions.containsKey("1.0.1c")
    }

    void testNoSslApk() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('checkSsl', type: OpenSslCheckTask)
        def apk = new File('./src/test/resources/app-no-ssl.apk')

        task.findOpenSslVersionsInFile(apk)

        assert task.openSslVersions.size() == 0
    }

}