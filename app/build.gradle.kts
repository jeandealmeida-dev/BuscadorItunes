plugins {
    id(ProjectPlugins.Application)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.NavigationSafeArgs)
}

dependencies {
    implementation(ProjectDependencies.Kotlin.Stdlib())

    // Project
    // → Search
    implementation(project(ProjectDependencies.Module.UI.search))
    implementation(project(ProjectDependencies.Module.Domain.search))
    implementation(project(ProjectDependencies.Module.Data.search))

    // → Favorite
    implementation(project(ProjectDependencies.Module.UI.favorite))
    implementation(project(ProjectDependencies.Module.Domain.favorite))
    implementation(project(ProjectDependencies.Module.Data.favorite))

    // → Music
    implementation(project(ProjectDependencies.Module.UI.music))
    implementation(project(ProjectDependencies.Module.Domain.music))
    implementation(project(ProjectDependencies.Module.Data.music))

    // → Settings
    implementation(project(ProjectDependencies.Module.UI.settings))
    implementation(project(ProjectDependencies.Module.Domain.settings))
    implementation(project(ProjectDependencies.Module.Data.settings))

    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.core))
    implementation(project(ProjectDependencies.Module.player))
    implementation(project(ProjectDependencies.Module.ds))

    // VersionCatalog
    implementationPackLibraries {
        addDaggerDependencies(it)
        addRetrofitDependencies(it)
        addRxJavaDependencies(it)
    }

    // Android
    implementation(libs.multidex)

    // UI
    implementation(libs.design)
    implementation(libs.material)

    // Android X
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.localbroadcastmanager)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)

    // Lifecycle
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.extensions)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Pagging
    implementation(libs.androidx.paging.rxjava)
    implementation(libs.androidx.paging.runtime)

    // Others
    implementation(libs.stetho)

    // Picasso
    implementation(libs.picasso)
    implementation(libs.shimmerlayout)
}