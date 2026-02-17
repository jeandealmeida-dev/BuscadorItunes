plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.NavigationSafeArgs)
}

myOptions {
    jacoco {
        excludesUI()
    }
}

dependencies {
    implementationModules(
        ProjectDependencies.Module.commons,
        ProjectDependencies.Module.core,
        ProjectDependencies.Module.Domain.music,
        ProjectDependencies.Module.player
    )

    implementationPackLibraries {
        addDaggerDependencies(it)
        addRxJavaDependencies(it)
    }

    // UI
    implementation(libs.material)
    implementation(libs.palette)
    implementation(libs.picasso)

    //AndroidX
    implementation(libs.androidx.cardview)
}