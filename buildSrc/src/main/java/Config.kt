import org.gradle.api.JavaVersion

object Config {

    const val compileSdkVersion = 34
    const val minSdkVersion = 24
    const val targetSdkVersion = 34
    const val namespace = "com.jeanpaulo.musiclibrary"

    const val versionName = "1.0"
    const val versionCode = 2024060400 //YYYYMMDD

    const val buildToolsVersion = "34.0.0"

    object Java {
        val version = JavaVersion.VERSION_17
    }
}
