plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.Parcelize) // required to use parcelize
}

addDaggerDependencies()
addRoomDependencies()
addRetrofitDependencies()
addMoshiDependencies()

// Tests
addUnitTestDependencies()

dependencies {
    implementation(project(ProjectDependencies.Module.commons))

    // Base
//    implementation(ProjectDependencies.AndroidX.core())
//    implementation(ProjectDependencies.AndroidX.AppCompat())
//    testImplementation(ProjectDependencies.JUnit())
//    androidTestImplementation(ProjectDependencies.AndroidX.JUnit())
//    androidTestImplementation(ProjectDependencies.AndroidX.Espresso())

    implementation(ProjectDependencies.Android.MaterialComponents())
    implementation(ProjectDependencies.AndroidX.ConstraintLayout())
    implementation(ProjectDependencies.AndroidX.RecyclerView())

    implementation(ProjectDependencies.Skeleton.core())
    implementation(ProjectDependencies.Skeleton.ShimmerLayout())

    // Picasso
    implementation(ProjectDependencies.Picasso())
}