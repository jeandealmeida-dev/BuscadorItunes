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
    implementation(project(ProjectDependencies.Module.player))

    implementation(project(ProjectDependencies.Module.Domain.favorite))

    implementation(ProjectDependencies.Android.MaterialComponents())
    implementation(ProjectDependencies.Picasso())

    // Android X
    implementation(ProjectDependencies.AndroidX.core())
    implementation(ProjectDependencies.AndroidX.RecyclerView())
    implementation(ProjectDependencies.AndroidX.SwipeRefreshLayout())

    implementation(ProjectDependencies.AndroidX.Navigation.Fragment())
    implementation(ProjectDependencies.AndroidX.Navigation.UI())

    // UI Test
    testImplementation(ProjectDependencies.AndroidX.ArchCoreTesting())

    // Skeleton
    implementation(ProjectDependencies.Skeleton.core() )
    implementation(ProjectDependencies.Skeleton.ShimmerLayout())

    // Constraint Layout
    implementation(ProjectDependencies.AndroidX.ConstraintLayout())
}