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

dependencies {
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.core))
    implementation(project(ProjectDependencies.Module.Domain.music))
    implementation(project(ProjectDependencies.Module.player))

    // Material
    implementation(ProjectDependencies.Android.MaterialComponents())

    //AndroidX
    implementation(ProjectDependencies.AndroidX.CardView())

    // Palette
    implementation(ProjectDependencies.Android.Palette())

    // Picasso
    implementation(ProjectDependencies.Picasso())
}