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

    // Base
    implementation(ProjectDependencies.AndroidX.core())
    implementation(ProjectDependencies.AndroidX.AppCompat())
    implementation(ProjectDependencies.Android.MaterialComponents())
    testImplementation(ProjectDependencies.JUnit())
    androidTestImplementation(ProjectDependencies.AndroidX.JUnit())
    androidTestImplementation(ProjectDependencies.AndroidX.Espresso())

    // Android X
    implementation(ProjectDependencies.AndroidX.SwipeRefreshLayout())
    // → Navigation
    implementation(ProjectDependencies.AndroidX.Navigation.Fragment())
    implementation(ProjectDependencies.AndroidX.Navigation.UI())

    // Testing
    testImplementation(ProjectDependencies.Mockk())
    testImplementation(ProjectDependencies.AndroidX.ArchCoreTesting())
}