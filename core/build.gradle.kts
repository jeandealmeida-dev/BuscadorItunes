plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.Parcelize) // required to use parcelize
}

android {
    buildTypes {
        debug {
            buildConfigField(
                "long",
                "DEFAULT_DELAY",
                "${project.rootProject.extra["DEFAULT_DELAY_DEBUG"]}"
            )
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"${project.rootProject.extra["API_BASE_URL"]}\""
            )
        }

        release {
            buildConfigField(
                "long",
                "DEFAULT_DELAY",
                "${project.rootProject.extra["DEFAULT_DELAY_RELEASE"]}"
            )
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"${project.rootProject.extra["API_BASE_URL"]}\""
            )
        }
    }
}

dependencies {
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.player))
    implementation(libs.androidx.recyclerview)

    implementationPackLibraries {
        addUnitTestDependencies(it)
        addDaggerDependencies(it)
        addRoomDependencies(it)
        addRetrofitDependencies(it)
        addMoshiDependencies(it)
    }
}