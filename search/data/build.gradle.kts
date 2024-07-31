plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.NavigationSafeArgs)
}

addRetrofitDependencies()
addDaggerDependencies()

addUnitTestDependencies()

dependencies {
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.core))

    // Android X
    implementation(ProjectDependencies.AndroidX.Paging())

    // Convert Coroutines in RxJava
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3:1.6.0")

    //Test using Coroutines
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
}