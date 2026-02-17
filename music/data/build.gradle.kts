plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.NavigationSafeArgs)
}

dependencies {
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.core))

    implementationPackLibraries {
        addDaggerDependencies(it)
        addRetrofitDependencies(it)
        addRoomDependencies(it)
        addUnitTestDependencies(it)
    }
}