plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
}

addDaggerDependencies()
addRxJavaDependencies()

dependencies {

    // Base
    implementation(ProjectDependencies.AndroidX.core())
    implementation(ProjectDependencies.AndroidX.AppCompat())
    implementation(ProjectDependencies.Android.MaterialComponents())
    testImplementation(ProjectDependencies.JUnit())
    androidTestImplementation(ProjectDependencies.AndroidX.JUnit())
    androidTestImplementation(ProjectDependencies.AndroidX.Espresso())

    implementation(ProjectDependencies.AndroidX.SwipeRefreshLayout())

}