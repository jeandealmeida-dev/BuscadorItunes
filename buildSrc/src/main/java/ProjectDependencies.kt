import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

object ProjectDependencies {

    const val buildToolsVersion = "34.0.0"

    object Module {
        const val app = ":app"
        const val core = ":core"
        const val commons = ":commons"
        const val music = ":music"
        const val music_detail = ":music:detail"

        object UI {
            const val search = ":search:ui"
            const val playlist = ":playlist:ui"
            const val music = ":music:ui"
            const val favorite = ":favorite:ui"
        }

        object Domain {
            const val search = ":search:domain"
            const val playlist = ":playlist:domain"
            const val music = ":music:domain"
            const val favorite = ":favorite:domain"
        }

        object Data {
            const val search = ":search:data"
            const val playlist = ":playlist:data"
            const val music = ":music:data"
            const val favorite = ":favorite:data"
        }
    }

    // updated 10/12/2022
    object Android {
        fun BuildGradle() = "com.android.tools.build:gradle:8.4.1"
        fun MaterialComponents() =
            "com.google.android.material:material:1.6.0" // Stable (https://stackoverflow.com/questions/74191324/cant-determine-type-for-tag-macro-name-m3-comp-bottom-app-bar-container-colo)

        fun Multidex() = "com.android.support:multidex:2.0.1"

        fun Palette() = "com.android.support:palette-v7:31.0.0"
    }

    // updated 29/01/2022
    object Kotlin {
        val version = "1.7.10"
        fun GradlePlugin() = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        fun Stdlib() = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
    }

    // updated 10/12/2022
    object Retrofit {
        val version = "2.9.0"
        fun core() = "com.squareup.retrofit2:retrofit:$version"

        object Adapter {
            fun RxJava3() = "com.squareup.retrofit2:adapter-rxjava3:$version"
        }

        object Converter {
            fun Moshi() = "com.squareup.retrofit2:converter-moshi:$version"
            fun Gson() = "com.squareup.retrofit2:converter-gson:$version"
        }
    }

    // updated 30/01/2024
    object Moshi {
        val version = "1.15.1"
        fun Adapters() = "com.squareup.moshi:moshi-adapters:$version"
        fun Codegen() = "com.squareup.moshi:moshi-kotlin-codegen:$version"
        fun Kotlin() = "com.squareup.moshi:moshi-kotlin:$version"
    }

    // last version 29/05/2024
    object Room {
        val version = "2.6.1"
        fun RxJava3() = "androidx.room:room-rxjava3:$version"
        fun Ktx() = "androidx.room:room-ktx:$version"
        fun Compiler() = "androidx.room:room-compiler:$version"
        fun Runtime() = "androidx.room:room-runtime:$version"
    }

    // updated 10/12/2022
    object RxJava3 {
        fun RxKotlin() = "io.reactivex.rxjava3:rxkotlin:3.0.1"
        fun RxJava() = "io.reactivex.rxjava3:rxjava:3.1.5"
        fun RxAndroid() = "io.reactivex.rxjava3:rxandroid:3.0.2"
    }

    // updated 29/03/2024
    object Dagger {
        private val version = "2.51.1"

        fun core() = "com.google.dagger:dagger:$version"
        fun Compiler() = "com.google.dagger:dagger-compiler:$version"

        object Android {
            fun core() = "com.google.dagger:dagger-android:$version"
            fun Support() = "com.google.dagger:dagger-android-support:$version"
            fun Processor() = "com.google.dagger:dagger-android-processor:$version"
        }
    }


    // updated 10/12/2022
    object AndroidX {
        fun core() = "androidx.core:core-ktx:1.9.0"
        fun AppCompat() = "androidx.appcompat:appcompat:1.5.1"
        fun JUnit() = "androidx.test.ext:junit:1.1.4"
        fun Espresso() = "androidx.test.espresso:espresso-core:3.5.0"
        fun Paging() = "androidx.paging:paging-runtime:3.1.1"
        fun Fragment() = "androidx.fragment:fragment-ktx:1.5.5"
        fun CardView() = "androidx.cardview:cardview:1.0.0"
        fun RecyclerView() = "androidx.recyclerview:recyclerview:1.2.1"
        fun SwipeRefreshLayout() = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
        fun ConstraintLayout() = "androidx.constraintlayout:constraintlayout:2.1.4"
        fun ArchCoreTesting() = "androidx.arch.core:core-testing:2.1.0"

        object Navigation {
            val version = "2.5.3"
            fun SafeArgs() = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
            fun UI() = "androidx.navigation:navigation-ui-ktx:$version"
            fun Fragment() = "androidx.navigation:navigation-fragment-ktx:$version"

            fun Compose() = "androidx.navigation:navigation-compose:$version"
        }

        object Lyfecycle {
            fun Compiler() = "androidx.lifecycle:lifecycle-compiler:2.2.0"
            fun Extensions() = "androidx.lifecycle:lifecycle-extensions:2.2.0"
            fun ViewModel() = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1"
            fun LiveData() = "androidx.lifecycle:lifecycle-livedata-ktx:2.5.1"
        }
    }

    object Compose {
        const val version = "1.5.2"

        fun Activity() = "androidx.activity:activity-compose:1.6.1"

        //fun Compiler() = "androidx.compose.compiler:compiler:1.3.2"
        object CustomView {
            fun core() = "androidx.customview:customview:1.1.0"
            fun PoolingContainer() = "androidx.customview:customview-poolingcontainer:1.0.0"
        }

        object UI {
            fun core() = "androidx.compose.ui:ui:$version"
            fun Tooling() = "androidx.compose.ui:ui-tooling:$version"
            fun ToolingPreview() = "androidx.compose.ui:ui-tooling-preview:$version"
            fun ToolingPreviewAndroid() = "androidx.compose.ui:ui-tooling-preview-android:1.6.7"

            fun Text() = "androidx.compose.ui:ui-text:$version"
        }

        // update 29/05/2024
        object Runtime {
            private val version = "1.6.7"
            fun core() = "androidx.compose.runtime:runtime:$version"
            fun RxJava3() = "androidx.compose.runtime:runtime-rxjava3:$version"
        }

        object Foundation {
            fun core() = "androidx.compose.foundation:foundation:$version"
            fun Layout() = "androidx.compose.foundation:foundation-layout:$version"
        }

        object Material {
            fun core() = "androidx.compose.material:material:$version"
            fun IconsExtended() = "androidx.compose.material:material-icons-extended:$version"
        }

        fun Material3() = "androidx.compose.material3:material3:1.2.1"

        fun Compiler() = "androidx.compose.compiler:compiler:$version"
    }

    fun Mockk() = "io.mockk:mockk:1.13.5"

    fun Picasso() = "com.squareup.picasso:picasso:2.5.2"
    fun SLF4J() = "org.slf4j:slf4j-api:2.0.5"
    fun Logback() = "com.github.tony19:logback-android:2.0.0"
    fun Timber() = "com.jakewharton.timber:timber:5.0.1"
    fun Stetho() = "com.facebook.stetho:stetho:1.6.0"
    fun JUnit() = "junit:junit:4.13.2"
}

val implementation = "implementation"
val kapt = "kapt"
val testImplementation = "testImplementation"

fun Project.addDaggerDependencies() {
    dependencies {
        add(implementation, ProjectDependencies.Dagger.core())
        add(kapt, ProjectDependencies.Dagger.Compiler())
        add(implementation, ProjectDependencies.Dagger.Android.core())
        add(implementation, ProjectDependencies.Dagger.Android.Support())
        add(kapt, ProjectDependencies.Dagger.Android.Support())
        add(kapt, ProjectDependencies.Dagger.Android.Processor())
    }
}

fun Project.addRoomDependencies() {
    dependencies {
        add(implementation, ProjectDependencies.Room.Runtime())
        add(kapt, ProjectDependencies.Room.Compiler())
        add(implementation, ProjectDependencies.Room.Ktx())
        add(implementation, ProjectDependencies.Room.RxJava3())
    }
}

fun Project.addRxJavaDependencies() {
    dependencies {
        add(implementation, ProjectDependencies.RxJava3.RxAndroid())
        add(implementation, ProjectDependencies.RxJava3.RxJava())
        add(implementation, ProjectDependencies.RxJava3.RxKotlin())
    }
}

fun Project.addRetrofitDependencies() {
    dependencies {
        add(implementation, ProjectDependencies.Retrofit.core())
        add(implementation, ProjectDependencies.Retrofit.Converter.Moshi())
        add(implementation, ProjectDependencies.Retrofit.Adapter.RxJava3())
        add(implementation, ProjectDependencies.Retrofit.Converter.Gson())
    }
}

fun Project.addMoshiDependencies(){
    dependencies {
        add(implementation, ProjectDependencies.Moshi.Kotlin())
        add(kapt, ProjectDependencies.Moshi.Codegen())
        add(implementation, ProjectDependencies.Moshi.Adapters())
    }
}