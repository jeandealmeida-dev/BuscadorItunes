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
