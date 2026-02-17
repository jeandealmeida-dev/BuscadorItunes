import org.gradle.api.JavaVersion

object Config {

    const val compileSdkVersion = 35
    const val minSdkVersion = 24
    const val targetSdkVersion = 35
    const val namespace = "com.jeanpaulo.musiclibrary"

    const val versionName = "1.0"
    const val versionCode = 2024060400 //YYYYMMDD

    const val buildToolsVersion = "35.0.0"

    object Java {
        val version = JavaVersion.VERSION_17
    }
}
