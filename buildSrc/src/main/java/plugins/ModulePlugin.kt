package plugins

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.testing.jacoco.plugins.JacocoPlugin

class ModulePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        // Apply Required Plugins.
        PluginConfiguration.applyRequiredPlugins(project)
        project.rootProject.apply(mapOf("from" to "${project.rootDir}/config.gradle"))

        // Configure common android build parameters.
        AndroidBuildConfiguration.configureAndroidBuild(project)

        // Config Jacoco for each module.
        project.extensions.create<MyModuleExtension>("myOptions")
        project.afterEvaluate {
            project.extensions.getByType(MyModuleExtension::class.java).run {
                val jacocoOptions = this.jacoco
                if (jacocoOptions.isEnabled) {
                    project.plugins.apply(JacocoPlugin::class.java)
                    project.plugins.all {
                        when (this) {
                            is LibraryPlugin -> JacocoConfiguration.configureJacoco(
                                project,
                                project.extensions.getByType(LibraryExtension::class.java).libraryVariants,
                                jacocoOptions
                            )

                            is AppPlugin -> JacocoConfiguration.configureJacoco(
                                project,
                                project.extensions.getByType(AppExtension::class.java).applicationVariants,
                                jacocoOptions
                            )
                        }
                    }
                }
            }
        }
    }
}

/// Ref: https://medium.com/wantedly-engineering/managing-android-multi-module-project-with-gradle-plugin-and-kotlin-4fcc126e7e49