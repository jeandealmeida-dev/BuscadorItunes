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
        ProjectDependencies.Module.player,
        ProjectDependencies.Module.core,
        ProjectDependencies.Module.Domain.favorite,
        ProjectDependencies.Module.Domain.search,
        ProjectDependencies.Module.UI.music,
    )

    implementationPackLibraries {
        addDaggerDependencies(it)
        addRxJavaDependencies(it)
        addUnitTestDependencies(it)
    }

    // UI
    implementation(libs.material)
    implementation(libs.picasso)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.lifecycle.extensions)

    // Pagging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.rxjava)

    // UI Test
    //testImplementation(ProjectDependencies.AndroidX.ArchCoreTesting())

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
}