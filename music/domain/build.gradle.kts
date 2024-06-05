plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.NavigationSafeArgs)
}

addDaggerDependencies()

dependencies {
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.core))
    implementation(project(ProjectDependencies.Module.Data.music))
    implementation(project(ProjectDependencies.Module.Data.favorite))

    // Base
    implementation(ProjectDependencies.AndroidX.core())
    implementation(ProjectDependencies.AndroidX.AppCompat())
    implementation(ProjectDependencies.Android.MaterialComponents())
    testImplementation(ProjectDependencies.JUnit())
    androidTestImplementation(ProjectDependencies.AndroidX.JUnit())
    androidTestImplementation(ProjectDependencies.AndroidX.Espresso())

    // RxJava
    implementation(ProjectDependencies.RxJava3.RxAndroid())
    implementation(ProjectDependencies.RxJava3.RxJava())
    implementation(ProjectDependencies.RxJava3.RxKotlin())
}