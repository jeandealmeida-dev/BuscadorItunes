plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.NavigationSafeArgs)
}

dependencies {
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.core))

    implementationPackLibraries {
        addRetrofitDependencies(it)
        addDaggerDependencies(it)
        addRoomDependencies(it)
    }
}