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

    implementation(project(ProjectDependencies.Module.Domain.favorite))
//    implementation(project(ProjectDependencies.Module.UI.music))

    implementation(ProjectDependencies.Android.MaterialComponents())
    implementation(ProjectDependencies.Picasso())

    // Android X
    implementation(ProjectDependencies.AndroidX.core())
    implementation(ProjectDependencies.AndroidX.RecyclerView())
    implementation(ProjectDependencies.AndroidX.SwipeRefreshLayout())

    implementation(ProjectDependencies.AndroidX.Navigation.Fragment())
    implementation(ProjectDependencies.AndroidX.Navigation.UI())
}