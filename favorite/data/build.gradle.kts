plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.NavigationSafeArgs)
}

dependencies {
    implementationModules(
        ProjectDependencies.Module.commons,
        ProjectDependencies.Module.core
    )

    implementationPackLibraries {
        addDaggerDependencies(it)
        addRetrofitDependencies(it)
        addRoomDependencies(it)
        addUnitTestDependencies(it)
    }
}