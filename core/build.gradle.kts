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

addDaggerDependencies()
addRoomDependencies()
addRetrofitDependencies()
addMoshiDependencies()

// Tests
addUnitTestDependencies()

dependencies {
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.player))

    implementation(ProjectDependencies.Android.MaterialComponents())
    implementation(ProjectDependencies.AndroidX.ConstraintLayout())
    implementation(ProjectDependencies.AndroidX.RecyclerView())

    implementation(ProjectDependencies.Skeleton.core())
    implementation(ProjectDependencies.Skeleton.ShimmerLayout())

    // Picasso
    implementation(ProjectDependencies.Picasso())
}