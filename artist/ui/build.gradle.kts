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

addDaggerDependencies()
addRxJavaDependencies()

dependencies {
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.core))
    implementation(project(ProjectDependencies.Module.player))

    implementation(project(ProjectDependencies.Module.Domain.artist))

    implementation(ProjectDependencies.Android.MaterialComponents())

    // Constraint Layout
    implementation(ProjectDependencies.AndroidX.ConstraintLayout())

    // Navigation
    implementation(ProjectDependencies.AndroidX.Navigation.Fragment())
    implementation(ProjectDependencies.AndroidX.Navigation.UI())
}