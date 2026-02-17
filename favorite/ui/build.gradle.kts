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
        ProjectDependencies.Module.ds,
        ProjectDependencies.Module.commons,
        ProjectDependencies.Module.core,
        ProjectDependencies.Module.player,
        ProjectDependencies.Module.Domain.favorite
    )

    implementationPackLibraries {
        addDaggerDependencies(it)
        addRxJavaDependencies(it)
        addUnitTestDependencies(it)
    }

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // UI Test
    testImplementation(ProjectDependencies.AndroidX.ArchCoreTesting())

    // UI
    implementation(libs.material)
    implementation(libs.skeleton)
    implementation(libs.picasso)

    // Android X
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.swiperefreshlayout)
}