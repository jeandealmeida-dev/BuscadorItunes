val kotlin_version: String by extra
plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}
apply {
    plugin("kotlin-android")
}

android {
    compileSdk = Config.compileSdkVersion

    defaultConfig {
        minSdk = Config.minSdkVersion
        targetSdk = Config.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Base
    implementation(Depends.AndroidX.core())
    implementation(Depends.AndroidX.AppCompat())
    implementation(Depends.Android.MaterialComponents())
    testImplementation(Depends.JUnit())
    androidTestImplementation(Depends.AndroidX.JUnit())
    androidTestImplementation(Depends.AndroidX.Espresso())

    // Room
    implementation(Depends.Room.Runtime())
    kapt(Depends.Room.Compiler())
    implementation(Depends.Room.Ktx())
    implementation(Depends.Room.RxJava3())

    // Dagger
    implementation(Depends.Dagger.core())
    kapt(Depends.Dagger.Compiler())
    implementation(Depends.Dagger.Android.core())
    implementation(Depends.Dagger.Android.Support())
    kapt(Depends.Dagger.Android.Support())
    kapt(Depends.Dagger.Android.Processor())

    // Retrofit
    implementation(Depends.Retrofit.core())
    implementation(Depends.Retrofit.Converter.Moshi())
    implementation(Depends.Retrofit.Adapter.RxJava3())
    implementation(Depends.Retrofit.Converter.Gson())

    // Moshi
    implementation(Depends.Moshi.Kotlin())
    kapt(Depends.Moshi.Codegen())
    implementation(Depends.Moshi.Adapters())
}
repositories {
    mavenCentral()
}