object Depends {

    object Module {
        const val app = ":app"
        const val core = ":core"
        const val commons = ":commons"
        const val favorite = ":favorite"
        const val playlist = ":playlist"
        const val playlist_create = ":playlist:create"
        const val playlist_detail = ":playlist:detail"
    }

    // updated 10/12/2022
    object Android {
        fun BuildGradle() = "com.android.tools.build:gradle:2.3.0"
        fun MaterialComponents() = "com.google.android.material:material:1.6.0" // Stable (https://stackoverflow.com/questions/74191324/cant-determine-type-for-tag-macro-name-m3-comp-bottom-app-bar-container-colo)
        fun Multidex() = "com.android.support:multidex:2.0.1"

        //${CustomVersions.kt.gradle_plugin_version}
        fun Palette() = "com.android.support:palette-v7:31.0.0"
    }

    // updated 10/12/2022
    object Kotlin {
        val version = "1.7.22"
        fun GradlePlugin() = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        fun Stdlib() = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.7.22"
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

    // updated 10/12/2022
    object Moshi {
        val version = "1.14.0"
        fun Adapters() = "com.squareup.moshi:moshi-adapters:$version"
        fun Codegen() = "com.squareup.moshi:moshi-kotlin-codegen:$version"
        fun Kotlin() = "com.squareup.moshi:moshi-kotlin:$version"
    }

    // updated 10/12/2022
    object Room {
        val version = "2.4.3"
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

    // updated 10/12/2022
    object Dagger {
        private val version = "2.44.2"

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

        object Navigation {
            val version = "2.5.3"
            fun SafeArgs() = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
            fun UI() = "androidx.navigation:navigation-ui-ktx:$version"
            fun Fragment() = "androidx.navigation:navigation-fragment-ktx:$version"
        }

        object Lyfecycle {
            fun Compiler() = "androidx.lifecycle:lifecycle-compiler:2.2.0"
            fun Extensions() = "androidx.lifecycle:lifecycle-extensions:2.2.0"
            fun ViewModel() = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1"
            fun LiveData() = "androidx.lifecycle:lifecycle-livedata-ktx:2.5.1"
        }
    }

    fun Picasso() = "com.squareup.picasso:picasso:2.5.2"
    fun SLF4J() = "org.slf4j:slf4j-api:2.0.5"
    fun Logback() = "com.github.tony19:logback-android:2.0.0"
    fun Timber() = "com.jakewharton.timber:timber:5.0.1"
    fun Stetho() = "com.facebook.stetho:stetho:1.6.0"
    fun JUnit() = "junit:junit:4.13.2"
}