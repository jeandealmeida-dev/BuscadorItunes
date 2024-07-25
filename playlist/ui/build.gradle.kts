plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.NavigationSafeArgs)
}

myOptions {
    jacoco {
        excludesUI()
    }
}

addDaggerDependencies()
addRxJavaDependencies()

//Tests
addUnitTestDependencies()

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

    // UI Test
    testImplementation(ProjectDependencies.AndroidX.ArchCoreTesting())
}