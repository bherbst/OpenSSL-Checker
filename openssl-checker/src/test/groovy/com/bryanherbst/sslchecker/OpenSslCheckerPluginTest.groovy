package com.bryanherbst.sslchecker

import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

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

    void testAddsTaskForVariant() {
        def project = ProjectBuilder.builder().build()
        project.plugins.apply('com.android.application')

        def outputs = [mock(BaseVariantOutput.class)]
        def variant = mock(ApplicationVariant.class)
        when(variant.getOutputs()).thenReturn(outputs)
        when(variant.getName()).thenReturn("variantOne")
        when(variant.getAssemble()).thenReturn(mock(Task.class))


        project.android.addVariant(variant)

        new OpenSslCheckerPlugin().apply(project)

        assert project.tasks.getNames().contains("checkVariantOneOpenSsl")
    }

    void testAddsTasksForMultipleVariants() {
        def project = ProjectBuilder.builder().build()
        project.plugins.apply('com.android.application')

        def outputsOne = [mock(BaseVariantOutput.class)]
        def variantOne = mock(ApplicationVariant.class)
        when(variantOne.getOutputs()).thenReturn(outputsOne)
        when(variantOne.getName()).thenReturn("variantOne")
        when(variantOne.getAssemble()).thenReturn(mock(Task.class))

        def outputsTwo = [mock(BaseVariantOutput.class)]
        def variantTwo = mock(ApplicationVariant.class)
        when(variantTwo.getOutputs()).thenReturn(outputsTwo)
        when(variantTwo.getName()).thenReturn("variantTwo")
        when(variantTwo.getAssemble()).thenReturn(mock(Task.class))


        project.android.addVariant(variantOne)
        project.android.addVariant(variantTwo)

        new OpenSslCheckerPlugin().apply(project)

        assert project.tasks.getNames().contains("checkVariantOneOpenSsl")
        assert project.tasks.getNames().contains("checkVariantTwoOpenSsl")
    }

}