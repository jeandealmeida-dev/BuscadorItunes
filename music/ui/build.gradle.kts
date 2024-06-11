plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.NavigationSafeArgs)
}

addDaggerDependencies()
addRxJavaDependencies()

dependencies {
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.core))
    implementation(project(ProjectDependencies.Module.Domain.music))

    // Base
    implementation(ProjectDependencies.AndroidX.core())
    implementation(ProjectDependencies.AndroidX.AppCompat())
    implementation(ProjectDependencies.Android.MaterialComponents())
    testImplementation(ProjectDependencies.JUnit())
    androidTestImplementation(ProjectDependencies.AndroidX.JUnit())
    androidTestImplementation(ProjectDependencies.AndroidX.Espresso())

    //AndroidX
    implementation(ProjectDependencies.Compose.Activity())

    // Palette
    implementation(ProjectDependencies.Android.Palette())

    // Picasso
    implementation(ProjectDependencies.Picasso())

    // Compose
    debugImplementation(ProjectDependencies.Compose.UI.Tooling())
    implementation(ProjectDependencies.Compose.UI.ToolingPreview())

    implementation(ProjectDependencies.Compose.Compiler())

    // Compose -> Runtime
    implementation(ProjectDependencies.Compose.Runtime.core())
    implementation(ProjectDependencies.Compose.Runtime.RxJava3())

    implementation(ProjectDependencies.Compose.UI.core())
    implementation(ProjectDependencies.Compose.Foundation.core())
    implementation(ProjectDependencies.Compose.Foundation.Layout())
    implementation(ProjectDependencies.Compose.Material.core())
    implementation(ProjectDependencies.Compose.Material.IconsExtended())
    implementation(ProjectDependencies.Compose.UI.Text())
    implementation(ProjectDependencies.Compose.CustomView.core())
    implementation(ProjectDependencies.Compose.CustomView.PoolingContainer())
}
repositories {
    mavenCentral()
}