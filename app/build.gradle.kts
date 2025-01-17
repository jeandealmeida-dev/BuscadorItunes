plugins {
    id(ProjectPlugins.Application)
    id(ProjectPlugins.MyPlugin)
    id(ProjectPlugins.NavigationSafeArgs)
}

addDaggerDependencies()
addRxJavaDependencies()
addRetrofitDependencies()

dependencies {
    implementation(ProjectDependencies.Kotlin.Stdlib())

    // Project
    // → Search
    implementation(project(ProjectDependencies.Module.UI.search))
    implementation(project(ProjectDependencies.Module.Domain.search))
    implementation(project(ProjectDependencies.Module.Data.search))

    // → Playlist
    implementation(project(ProjectDependencies.Module.UI.playlist))
    implementation(project(ProjectDependencies.Module.Domain.playlist))
    implementation(project(ProjectDependencies.Module.Data.playlist))

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

    // → Artist
    implementation(project(ProjectDependencies.Module.UI.artist))
    implementation(project(ProjectDependencies.Module.Domain.artist))
    implementation(project(ProjectDependencies.Module.Data.artist))

    implementation(project(ProjectDependencies.Module.commons))
    implementation(project(ProjectDependencies.Module.core))
    implementation(project(ProjectDependencies.Module.player))

    // Base
    implementation(ProjectDependencies.AndroidX.Design())
    implementation(ProjectDependencies.AndroidX.core())
    implementation(ProjectDependencies.AndroidX.AppCompat())
    implementation(ProjectDependencies.Android.MaterialComponents())
    testImplementation(ProjectDependencies.JUnit())
    androidTestImplementation(ProjectDependencies.AndroidX.JUnit())
    androidTestImplementation(ProjectDependencies.AndroidX.Espresso())

    // Android
    implementation(ProjectDependencies.Android.Multidex())

    // Android X
    implementation(ProjectDependencies.AndroidX.Paging())
    implementation(ProjectDependencies.AndroidX.ConstraintLayout())
    implementation(ProjectDependencies.AndroidX.Fragment())
    implementation(ProjectDependencies.AndroidX.SwipeRefreshLayout())
    implementation(ProjectDependencies.AndroidX.CardView())
    implementation(ProjectDependencies.AndroidX.RecyclerView())
    implementation(ProjectDependencies.AndroidX.Preference())

    // → Lifecycle
    implementation(ProjectDependencies.AndroidX.Lifecycle.LiveData())
    implementation(ProjectDependencies.AndroidX.Lifecycle.ViewModel())
    implementation(ProjectDependencies.AndroidX.Lifecycle.Extensions())
    // → Navigation
    implementation(ProjectDependencies.AndroidX.Navigation.Fragment())
    implementation(ProjectDependencies.AndroidX.Navigation.UI())

    // Others
    implementation(ProjectDependencies.Stetho())

    // Picasso
    implementation(ProjectDependencies.Picasso())
}