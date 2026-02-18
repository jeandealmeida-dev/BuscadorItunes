    dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}
rootProject.name = "Music Library"
include(
    ":app",
    ":core",
    ":ds",
    ":commons",
    ":player",

    ":music",
    ":music:ui",
    ":music:data",
    ":music:domain",

    ":search",
    ":search:ui",
    ":search:data",
    ":search:domain",

    ":favorite",
    ":favorite:ui",
    ":favorite:domain",
    ":favorite:data",

    ":settings",
    ":settings:ui",
    ":settings:domain",
    ":settings:data",
)
