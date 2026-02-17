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
    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.core))

    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    
    implementation(libs.skeleton)
    implementation(libs.picasso)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.lifecycle.extensions)
}
