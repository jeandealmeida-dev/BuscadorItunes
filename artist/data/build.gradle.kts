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
    implementation(project(ProjectDependencies.Module.core))
    implementation(project(ProjectDependencies.Module.commons))

    implementationPackLibraries {
        addDaggerDependencies(it)
        addRxJavaDependencies(it)
    }
}