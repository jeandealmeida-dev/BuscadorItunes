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

dependencies {
    implementationModules(
        ProjectDependencies.Module.commons,
        ProjectDependencies.Module.ds,
        ProjectDependencies.Module.core,
        ProjectDependencies.Module.Domain.playlist,
        ProjectDependencies.Module.Domain.favorite,
        ProjectDependencies.Module.UI.favorite
    )

    implementationPackLibraries {
        addDaggerDependencies(it)
        addRxJavaDependencies(it)
        addUnitTestDependencies(it)
    }

    // UI
    implementation(libs.material)
    implementation(libs.androidx.swiperefreshlayout)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // UI Test
    //testImplementation(ProjectDependencies.AndroidX.ArchCoreTesting())
}