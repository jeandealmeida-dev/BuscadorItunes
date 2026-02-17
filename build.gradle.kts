buildscript {
    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath(libs.build.gradle)
        classpath(libs.navigation.plugin)
        classpath(libs.kotlin.gradle.plugin)
    }
}