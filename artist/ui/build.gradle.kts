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
        ProjectDependencies.Module.ds,
        ProjectDependencies.Module.commons,
        ProjectDependencies.Module.core,
        ProjectDependencies.Module.player,
        ProjectDependencies.Module.Domain.artist
    )

    implementationPackLibraries {
        addDaggerDependencies(it)
        addRxJavaDependencies(it)
    }

    // Constraint Layout
    implementation(libs.androidx.constraintlayout)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
}