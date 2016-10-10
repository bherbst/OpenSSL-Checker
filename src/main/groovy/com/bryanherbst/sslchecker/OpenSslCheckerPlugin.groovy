package com.bryanherbst.sslchecker

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.BaseVariantOutput
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project

class OpenSslCheckerPlugin implements Plugin<Project> {
    void apply(Project project) {

        if (project.plugins.hasPlugin('com.android.application')) {
            applyAndroid(project, (DomainObjectCollection<BaseVariant>) project.android.applicationVariants);
        } else if (project.plugins.hasPlugin('com.android.test')) {
            applyAndroid(project, (DomainObjectCollection<BaseVariant>) project.android.applicationVariants);
        } else if (project.plugins.hasPlugin('com.android.library')) {
            applyAndroid(project, (DomainObjectCollection<BaseVariant>) project.android.libraryVariants);
        } else {
            throw new IllegalArgumentException('OpenSSL Checker plugin requires the Android plugin to be configured');
        }

    }

    private static void applyAndroid(Project project, DomainObjectCollection<BaseVariant> variants) {
        variants.all { variant ->
            variant.outputs.each { output ->
                applyToVariantOutput(project, variant, output)
            }
        }
    }

    private static void applyToVariantOutput(Project project, BaseVariant variant, BaseVariantOutput output) {
        def slug = variant.name.capitalize()

        def task = project.tasks.create("check${slug}OpenSSL", OpenSslCheckTask)
        task.description = "Checks for OpenSSL vulnerabilities in ${variant.name}."
        task.group = 'Verification'
        task.androidOutput = output

        // Dexcount tasks require that assemble has been run...
        task.dependsOn(variant.assemble)
        task.mustRunAfter(variant.assemble)
    }
}