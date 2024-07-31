package plugins

import org.gradle.api.Project
import org.gradle.api.DomainObjectSet
import com.android.build.gradle.api.BaseVariant
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.util.Locale

object JacocoConfiguration {
    fun configureJacoco(project: Project, variants: DomainObjectSet<out BaseVariant>, options: JacocoOptions) {
        variants.all {
            val isDebuggable = this.buildType.isDebuggable
            if (!isDebuggable) {
                project.logger.info("Skipping Jacoco for $name because it is not debuggable.")
                return@all
            }

            val variantName = name.replaceFirstChar { it.titlecase(Locale.getDefault()) }
            project.tasks.register<JacocoReport>("jacocoTestReport") {
                val unitTests = "test${variantName}UnitTest"
                val androidTests = "connected${variantName}AndroidTest"

                dependsOn(listOf(unitTests))

                group = "Reporting"
                description = "Execute UI and unit tests, generate and combine Jacoco coverage report"

                reports {
                    csv.required.set(false)
                    xml.required.set(true)
                    html.required.set(true)
                    html.outputLocation.set(project.file("${project.buildDir}/reports/jacocoHtml"))
                }

                val sourceDir = "${project.projectDir}/src/main/java"
                sourceDirectories.setFrom(sourceDir)
                additionalSourceDirs.setFrom(sourceDir)

                classDirectories.setFrom(
                    project.fileTree("${project.buildDir}/intermediates/javac/$variantName") { setExcludes(options.excludes) },
                    project.fileTree("${project.buildDir}/tmp/kotlin-classes/$variantName") { setExcludes(options.excludes) }
                )

                val execFile = "jacoco/test${variantName}UnitTest.exec"
                executionData.setFrom(project.fileTree("${project.buildDir}") { setIncludes(listOf(execFile)) })

                setOnlyIf { executionData.files.any { it.exists() } }
            }
        }
    }
}
