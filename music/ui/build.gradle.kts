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

    buildFeatures {
        dataBinding = false
        viewBinding = true
        compose = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    composeOptions {
        //kotlinCompilerVersion = Depends.Kotlin.version
        kotlinCompilerExtensionVersion = Depends.Compose.version
    }
}

dependencies {
    implementation(project(Depends.Module.commons))
    implementation(project(Depends.Module.core))
    implementation(project(Depends.Module.Domain.music))

    // Base
    implementation(Depends.AndroidX.core())
    implementation(Depends.AndroidX.AppCompat())
    implementation(Depends.Android.MaterialComponents())
    testImplementation(Depends.JUnit())
    androidTestImplementation(Depends.AndroidX.JUnit())
    androidTestImplementation(Depends.AndroidX.Espresso())

    //AndroidX
    implementation(Depends.AndroidX.Compose())

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

    // Palette
    implementation(Depends.Android.Palette())

    // Picasso
    implementation(Depends.Picasso())

    // Compose
    debugImplementation(Depends.Compose.UI.Tooling())
    implementation(Depends.Compose.UI.ToolingPreview())

    //implementation(Depends.Compose.Compiler())
    implementation(Depends.Compose.Runtime())
    implementation(Depends.Compose.UI.core())
    implementation(Depends.Compose.Foundation.core())
    implementation(Depends.Compose.Foundation.Layout())
    implementation(Depends.Compose.Material.core())
    implementation(Depends.Compose.Material.IconsExtended())
    implementation(Depends.Compose.UI.Text())
    implementation(Depends.Compose.CustomView.core())
    implementation(Depends.Compose.CustomView.PoolingContainer())
}
repositories {
    mavenCentral()
}