rootProject.name = "MusicLibrary"
include(
    ":app",
    ":core",
    ":commons",
    ":player",

    ":artist",
    ":artist:ui",
    ":artist:data",
    ":artist:domain",

    ":playlist",
    ":playlist:ui",
    ":playlist:data",
    ":playlist:domain",

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
