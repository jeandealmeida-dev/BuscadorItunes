plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.Parcelize) // required to use parcelize
}

addDaggerDependencies()
addRoomDependencies()
addRetrofitDependencies()
addMoshiDependencies()

dependencies {
    implementation(project(ProjectDependencies.Module.commons))

    // Base
    implementation(ProjectDependencies.AndroidX.core())
    implementation(ProjectDependencies.AndroidX.AppCompat())
    implementation(ProjectDependencies.Android.MaterialComponents())
    testImplementation(ProjectDependencies.JUnit())
    androidTestImplementation(ProjectDependencies.AndroidX.JUnit())
    androidTestImplementation(ProjectDependencies.AndroidX.Espresso())

    // Picaso
    implementation(ProjectDependencies.Picasso())
}