plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.NavigationSafeArgs)
}

dependencies {
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.core))

    implementationPackLibraries {
        addRetrofitDependencies(it)
        addDaggerDependencies(it)
        addUnitTestDependencies(it)
    }

    // Pagging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.rxjava)

    // Convert Coroutines in RxJava
    implementation(libs.kotlinx.coroutines.rx3)

    // Test using Coroutines
    testImplementation(libs.kotlinx.coroutines.test)
}