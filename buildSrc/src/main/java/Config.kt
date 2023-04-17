object Config {
    const val groupId = "com.jeanpaulo.buscador_itunes"
    const val applicationId = groupId
    const val dexguardApplicationId = "br.com.xp.carteira"
    const val XPAppScheme = "appxpinvestimentos"

    const val core = "core"

    const val compileSdkVersion = 33
    const val minSdkVersion = 21
    const val targetSdkVersion = 31

    const val versionName = "1.0"
    const val versionCode = 2022112100
    const val buildToolsVersion = "32.0.0"

    object Versions {
        const val versionFeed = "Android-Repositories"
        const val versionEnviroment = "xpinvestimentos"
        const val versionBranch = "develop"
        const val versionPath = "versions.gradle"
        const val versionType = "Branch"

        fun getVersionUrl(environment: String, token: String) =
            "curl https://$environment:$token@dev.azure.com/xpinvestimentos/Projetos/_apis/git/repositories/XpInc.Gradle.Android/items?path=$versionPath&versionDescriptor.version=$versionBranch&versionDescriptor.versionType=$versionType"
    }
}
