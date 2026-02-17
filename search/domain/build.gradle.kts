plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.NavigationSafeArgs)
}

dependencies {
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.core))
    implementation(project(ProjectDependencies.Module.Data.search))

    implementationPackLibraries {
        addDaggerDependencies(it)
        addRxJavaDependencies(it)
        addUnitTestDependencies(it)
    }

    // Android X
    implementation(libs.androidx.paging.runtime)
}