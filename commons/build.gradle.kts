plugins {
    id(ProjectPlugins.Library)
    id(ProjectPlugins.MyPlugin)
}

myOptions {
    jacoco {
        isEnabled = false
    }
}

dependencies {
    implementationPackLibraries {
        addDaggerDependencies(it)
        addRxJavaDependencies(it)
    }

    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
}