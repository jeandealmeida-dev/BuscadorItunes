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
import org.gradle.language.nativeplatform.internal.BuildType
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.JacocoReportsContainer
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import testImplementation
import java.util.Locale

class ModulePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        // Apply Required Plugins.
        project.plugins.apply("kotlin-android")
        project.plugins.apply("kotlin-kapt")

        // Configure common android build parameters.
        val androidExtension = project.extensions.findByName("android") as? BaseExtension
        if (androidExtension != null) {
            androidExtension.apply {

                compileSdkVersion(Config.compileSdkVersion)

                // trick to set namespace using project.group
                namespace = Config.namespace + project.path.replace(":", ".")

                buildFeatures.apply {
                    viewBinding = true
                }

                buildToolsVersion(Config.buildToolsVersion)

                defaultConfig {
                    minSdkVersion(Config.minSdkVersion)
                    targetSdkVersion(Config.targetSdkVersion)

                    versionCode = Config.versionCode
                    versionName = Config.versionName

                    multiDexEnabled = true

                    vectorDrawables.useSupportLibrary = true

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                    // Configure proguard file settings
                    val defaultProguardFilePath = "proguard-rules.pro"
                    when (this) {
                        is LibraryExtension -> defaultConfig {
                            consumerProguardFiles(defaultProguardFilePath)
                        }

                        is AppExtension -> buildTypes {
                            getByName(BuildType.DEBUG.name) {
                                isDebuggable = true
                                isMinifyEnabled = false
                                enableUnitTestCoverage = true
                                isTestCoverageEnabled = true
                            }

                            getByName(BuildType.RELEASE.name) {
                                isDebuggable = false
                                isMinifyEnabled = true
                                isShrinkResources = true
                                proguardFiles(
                                    getDefaultProguardFile("proguard-android-optimize.txt"),
                                    defaultProguardFilePath
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

        project.dependencies {
            // Adds shared required dependencies for all modules.     
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
            val isDebuggable = this.buildType.isDebuggable
            if (!isDebuggable) {
                project.logger.info("Skipping Jacoco for $name because it is not debuggable.")
                return@all
            }

            // Extract variant name and capitalize the first letter
            val variantName = name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }

            project.tasks.register<JacocoReport>("jacocoTestReport") {
                // Depend on unit tests and Android tests tasks
                //dependsOn(listOf(unitTests, androidTests))
                val unitTests = "test${variantName}UnitTest"
                val androidTests = "connected${variantName}AndroidTest"

                dependsOn(listOf(unitTests))

                // Set task grouping and description
                group = "Reporting"
                description = "Execute UI and unit tests, generate and combine Jacoco coverage report"

                // report format
                reports {
                    csv.required.set(false)
                    xml.required.set(true)
                    html.required.set(true)
                    html.outputLocation.set(project.file("${project.buildDir}/reports/jacocoHtml"))
                }

                val sourceDir = "${project.projectDir}/src/main/java"

                // Set source directories to the main source directory
                sourceDirectories.setFrom(sourceDir)
                additionalSourceDirs.setFrom(sourceDir)

                // Set class directories to compiled Java and Kotlin classes, excluding specified exclusions
                classDirectories.setFrom(
                    project.fileTree("${project.buildDir}/intermediates/javac/$variantName") {
                        setExcludes(options.excludes)
                    },
                    project.fileTree("${project.buildDir}/tmp/kotlin-classes/$variantName") {
                        setExcludes(options.excludes)
                    }
                )

                // Using the default Jacoco exec file output path.
                val execFile = "jacoco/test${variantName}UnitTest.exec"
                executionData.setFrom(
                    project.fileTree("${project.buildDir}") {
                        setIncludes(listOf(execFile))
                    }
                )

                // Do not run task if there's no execution data.
                setOnlyIf { executionData.files.any { it.exists() } }
            }
        }
    }
}

/// Ref: https://medium.com/wantedly-engineering/managing-android-multi-module-project-with-gradle-plugin-and-kotlin-4fcc126e7e49