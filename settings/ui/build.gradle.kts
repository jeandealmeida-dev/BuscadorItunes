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

    implementation(project(ProjectDependencies.Module.Domain.settings))
    implementation(project(ProjectDependencies.Module.Domain.playlist))

    implementation(ProjectDependencies.Android.MaterialComponents())

    // Android X
    implementation(ProjectDependencies.AndroidX.Preference())
}