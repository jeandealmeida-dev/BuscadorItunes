plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.NavigationSafeArgs)
}

addDaggerDependencies()
addRxJavaDependencies()

dependencies {
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.core))

    implementation(project(ProjectDependencies.Module.Domain.playlist))
    implementation(project(ProjectDependencies.Module.Domain.favorite))

    implementation(project(ProjectDependencies.Module.UI.favorite))

    implementation(ProjectDependencies.Android.MaterialComponents())

    // Android X
    implementation(ProjectDependencies.AndroidX.SwipeRefreshLayout())

    implementation(ProjectDependencies.AndroidX.Navigation.Fragment())
    implementation(ProjectDependencies.AndroidX.Navigation.UI())
}