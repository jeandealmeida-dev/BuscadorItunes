import com.android.build.gradle.internal.dsl.DataBindingOptions

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

subprojects {
    project.plugins.applyBaseConfig(project)
}

fun com.android.build.gradle.BaseExtension.baseConfig(){
    compileSdkVersion(Config.compileSdkVersion)

    buildFeatures.apply {
        viewBinding = true
        compose = true
    }

    defaultConfig.apply {
        minSdk = Config.minSdkVersion
        targetSdk = Config.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes.apply {
        //TODO
    }

    compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Depends.Compose.version
    }
}

//task clean(type: Delete) {
//    delete rootProject.buildDir
//}
