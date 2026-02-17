plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.NavigationSafeArgs)
}

dependencies {
    implementation(project(ProjectDependencies.Module.Data.artist))
    implementation(project(ProjectDependencies.Module.core))

    implementationPackLibraries {
        addDaggerDependencies(it)
        addRxJavaDependencies(it)
    }
}