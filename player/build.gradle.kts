plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.Parcelize) // required to use parcelize
}

myOptions {
    jacoco {
        isEnabled = false
    }
}

dependencies {
    // Project
    implementation(project(ProjectDependencies.Module.commons))

    implementation(ProjectDependencies.Android.MaterialComponents())

    implementation(ProjectDependencies.Picasso())
}