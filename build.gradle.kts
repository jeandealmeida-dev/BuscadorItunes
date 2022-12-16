buildscript {
    //apply(from = "versions.gradle")

    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath(Depends.Android.BuildGradle())
        classpath(Depends.Kotlin.GradlePlugin())
        classpath(Depends.AndroidX.Navigation.SafeArgs())
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

//task clean(type: Delete) {
//    delete rootProject.buildDir
//}
