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
    implementation(project(ProjectDependencies.Module.Domain.search))
    implementation(project(ProjectDependencies.Module.Domain.favorite))

    implementation(project(ProjectDependencies.Module.UI.music))

    // Base
    implementation(ProjectDependencies.AndroidX.core())
    implementation(ProjectDependencies.AndroidX.AppCompat())
    implementation(ProjectDependencies.Android.MaterialComponents())
    testImplementation(ProjectDependencies.JUnit())
    androidTestImplementation(ProjectDependencies.AndroidX.JUnit())
    androidTestImplementation(ProjectDependencies.AndroidX.Espresso())

    // AndroidX
    implementation(ProjectDependencies.AndroidX.CardView())
    implementation(ProjectDependencies.AndroidX.RecyclerView())

    // Picasso
    implementation(ProjectDependencies.Picasso())

    // Pagging
    implementation(ProjectDependencies.AndroidX.Paging())
}