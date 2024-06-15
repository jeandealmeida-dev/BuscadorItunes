package plugins

import Config
import ProjectDependencies
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import implementation
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import testImplementation

class ModulePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        // Apply Required Plugins.
        project.plugins.apply("kotlin-android")
        project.plugins.apply( "kotlin-kapt")

        // Configure common android build parameters.
        val androidExtension = project.extensions.getByName("android")
        if (androidExtension is BaseExtension) {
            androidExtension.apply {

                compileSdkVersion(Config.compileSdkVersion)

                // TODO trick to set namespace using project.group
                namespace = Config.namespace + project.path.replace(":", ".")

                buildFeatures.apply {
                    viewBinding = true
                }

                buildToolsVersion(Config.buildToolsVersion)

                defaultConfig {
                    //applicationId = Config.applicationId

                    minSdkVersion(Config.minSdkVersion)
                    targetSdkVersion(Config.targetSdkVersion)

                    versionCode = Config.versionCode
                    versionName = Config.versionName

                    multiDexEnabled = true

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                    // Configure proguard file settings
                    val proguardFile = "proguard-rules.pro"
                    when (this) {
                        is LibraryExtension -> defaultConfig {
                            consumerProguardFiles(proguardFile)
                        }
                        is AppExtension -> buildTypes {
                            getByName("debug") {
                                isDebuggable = true
                                isMinifyEnabled = false
                            }

                            getByName("release") {
                                isDebuggable = false
                                isMinifyEnabled = true
                                isShrinkResources = true
                                proguardFiles(
                                    getDefaultProguardFile("proguard-android-optimize.txt"),
                                    proguardFile
                                )
                            }
                        }
                    }

                    // Java
                    compileOptions {
                        sourceCompatibility = Config.Java.version
                        targetCompatibility = Config.Java.version
                    }
                    project.tasks.withType(KotlinCompile::class.java).configureEach {
                        kotlinOptions {
                            jvmTarget = Config.Java.version.majorVersion
                        }
                    }
                }
            }
        }


        // Adds required dependencies for all modules.
        project.dependencies {
            add(testImplementation, ProjectDependencies.JUnit())
            // Compose
        }

        // Config Jacoco for each module
        project.extensions.create<MyModuleExtension>("myOptions")

        // Read MyModuleExtension values in afterEvaluate block.
        project.afterEvaluate {
            project.extensions.getByType(MyModuleExtension::class.java).run {
                val jacocoOptions = this.jacoco
                if (jacocoOptions.isEnabled) {
                    // Setup jacoco tasks to generate coverage report for this module.
                    project.plugins.apply(JacocoPlugin::class.java)
                    project.plugins.all {
                        when (this) {
                            is LibraryPlugin -> {
                                project.extensions.getByType(LibraryExtension::class.java).run {
                                    configureJacoco(project, libraryVariants, jacocoOptions)
                                }
                            }
                            is AppPlugin -> {
                                project.extensions.getByType(AppExtension::class.java).run {
                                    configureJacoco(project, applicationVariants, jacocoOptions)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun configureJacoco(
        project: Project,
        variants: DomainObjectSet<out BaseVariant>,
        options: JacocoOptions
    ) {
        variants.all {
            val variantName = name
            val isDebuggable = this.buildType.isDebuggable
            if (!isDebuggable) {
                project.logger.info("Skipping Jacoco for $name because it is not debuggable.")
                return@all
            }

            project.tasks.register<JacocoReport>("jacoco${variantName.capitalize()}Report") {

                val javaClasses = project
                    .fileTree("${project.buildDir}/intermediates/javac/$variantName") {
                        setExcludes(options.excludes)
                    }

                val kotlinClasses = project
                    .fileTree("${project.buildDir}/tmp/kotlin-classes/$variantName") {
                        setExcludes(options.excludes)
                    }
            }
        }
    }
}

/// Ref: https://medium.com/wantedly-engineering/managing-android-multi-module-project-with-gradle-plugin-and-kotlin-4fcc126e7e49