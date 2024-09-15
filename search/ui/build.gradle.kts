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

addUnitTestDependencies()

dependencies {
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.player))
    implementation(project(ProjectDependencies.Module.core))
    implementation(project(ProjectDependencies.Module.Domain.search))
    implementation(project(ProjectDependencies.Module.Domain.favorite))

    implementation(project(ProjectDependencies.Module.UI.music))
    implementation(project(ProjectDependencies.Module.UI.artist))

    // Material
    implementation(ProjectDependencies.Android.MaterialComponents())

    // AndroidX
    implementation(ProjectDependencies.AndroidX.CardView())
    implementation(ProjectDependencies.AndroidX.RecyclerView())

    // Picasso
    implementation(ProjectDependencies.Picasso())

    // Pagging
    implementation(ProjectDependencies.AndroidX.Paging())
    implementation(ProjectDependencies.AndroidX.PagingRxJava())

    // UI Test
    testImplementation(ProjectDependencies.AndroidX.ArchCoreTesting())

    // Navigation
    implementation(ProjectDependencies.AndroidX.Navigation.Fragment())
    implementation(ProjectDependencies.AndroidX.Navigation.UI())
}