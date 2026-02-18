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
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.core))
    implementation(project(ProjectDependencies.Module.Domain.settings))
    implementation(project(ProjectDependencies.Module.Domain.favorite))

    implementationPackLibraries {
        addDaggerDependencies(it)
        addRxJavaDependencies(it)
        addUnitTestDependencies(it)
    }

    implementation(libs.material)
    implementation(libs.androidx.preference)

    // UI Test
    // testImplementation(ProjectDependencies.AndroidX.ArchCoreTesting())
}