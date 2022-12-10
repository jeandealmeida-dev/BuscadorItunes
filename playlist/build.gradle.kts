plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = Config.compileSdkVersion

    buildFeatures {
        dataBinding = false
        viewBinding = true
    }

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Project
    implementation(project(Depends.Module.playlist_create))
    implementation(project(Depends.Module.playlist_detail))
    implementation(project(Depends.Module.commons))
    implementation(project(Depends.Module.core))

    // Base
    implementation(Depends.AndroidX.core())
    implementation(Depends.AndroidX.AppCompat())
    implementation(Depends.Android.MaterialComponents())
    testImplementation(Depends.JUnit())
    androidTestImplementation(Depends.AndroidX.JUnit())
    androidTestImplementation(Depends.AndroidX.Espresso())

    // Android X
    implementation(Depends.AndroidX.SwipeRefreshLayout())
    // --> Navigation
    implementation(Depends.AndroidX.Navigation.Fragment())
    implementation(Depends.AndroidX.Navigation.UI())

    // Dagger
    implementation(Depends.Dagger.core())
    kapt(Depends.Dagger.Compiler())
    implementation(Depends.Dagger.Android.core())
    implementation(Depends.Dagger.Android.Support())
    kapt(Depends.Dagger.Android.Support())
    kapt(Depends.Dagger.Android.Processor())

    // RxJava
    implementation(Depends.RxJava3.RxAndroid())
    implementation(Depends.RxJava3.RxJava())
    implementation(Depends.RxJava3.RxKotlin())

    // Retrofit
    implementation(Depends.Retrofit.core())
    implementation(Depends.Retrofit.Converter.Moshi())
    implementation(Depends.Retrofit.Adapter.RxJava3())
    implementation(Depends.Retrofit.Converter.Gson())

    // Room
    implementation(Depends.Room.Runtime())
    kapt(Depends.Room.Compiler())
    implementation(Depends.Room.Ktx())
    implementation(Depends.Room.RxJava3())
}