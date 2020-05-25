const val kotlinVersion = "1.3.72"

object BuildPlugins {

    private object Versions {
        const val buildToolsVersion = "4.0.0-rc01"
        const val navigation = "2.3.0-alpha06"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val navigationSafeArgsGradlePlugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
    const val kotlinKapt = "kotlin-kapt"
    const val safeArgs = "androidx.navigation.safeargs.kotlin"
}

object AndroidSdk {

    const val compileSdkVersion = 29
    const val minSdkVersion = 21
    const val targetSdkVersion = compileSdkVersion
}

object Libraries {

    private object Versions {
        const val kotlinCoroutines = "1.3.7"
        const val appCompat = "1.1.0"
        const val material = "1.2.0-alpha04"
        const val constraintLayout = "1.1.3"
        const val coreKtx = "1.1.0"
        const val lifecycleExtensions = "2.2.0"
        const val lifecycleLivedataKtx = "2.2.0"
        const val lifecycleViewModelKtx = "2.2.0"
        const val lifecycleCompiler = "2.2.0"
        const val lifecycleViewModelSavedState = "1.0.0"
        const val threeTenAbp = "1.2.2"
        const val koin = "2.1.5"
        const val timber = "4.7.1"
        const val okhttp = "4.4.1"
        const val okhttpLoggingInterceptor = "4.4.1"
        const val retrofit = "2.8.1"
        const val retrofitMoshiConverter = "2.8.1"
        const val moshi = "1.9.2"
        const val moshiKotlinCodegen = "1.9.2"
        const val room = "2.2.5"
        const val krate = "0.4.0"
        const val chucker = "3.2.0"
        const val leakCanary = "2.2"
        const val swipeRefresh = "1.1.0-rc01"
        const val glide = "4.11.0"
        const val navigation = "2.3.0-alpha06"
        const val fragment = "1.3.0-alpha04"
        const val mpAndroidChart = "v3.1.0"
        const val workManager = "2.3.4"
    }

    // jetbrains
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    const val kotlinCoroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
    const val kotlinCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"

    // google
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val lifecycleExtensions =
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}"
    const val lifecycleLivedataKtx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleLivedataKtx}"
    const val lifecycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewModelKtx}"
    const val lifecycleCompiler =
        "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycleCompiler}"
    const val lifecycleViewModelSavedState =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycleViewModelSavedState}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val swipeToRefresh =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefresh}"
    const val navigationFragment = "androidx.navigation:navigation-fragment:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui:${Versions.navigation}"
    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val fragment = "androidx.fragment:fragment:${Versions.fragment}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val workManager = "androidx.work:work-runtime:${Versions.workManager}"
    const val workManagerKtx = "androidx.work:work-runtime-ktx:${Versions.workManager}"

    // dependency injection
    const val koinAndroid = "org.koin:koin-android:${Versions.koin}"
    const val koinScope = "org.koin:koin-androidx-scope:${Versions.koin}"
    const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    const val koinFragment = "org.koin:koin-androidx-fragment:${Versions.koin}"

    // networking
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val okhttpLoggingInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.okhttpLoggingInterceptor}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitMoshiConverter =
        "com.squareup.retrofit2:converter-moshi:${Versions.retrofitMoshiConverter}"

    // json
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moshiKotlinCodegen =
        "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshiKotlinCodegen}"

    // logging
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    // date and time
    const val threeTenAbp = "com.jakewharton.threetenabp:threetenabp:${Versions.threeTenAbp}"

    // shared preferences wrapper
    const val krate = "hu.autsoft:krate:${Versions.krate}"
    const val krateMoshiCodegen = "hu.autsoft:krate-moshi-codegen:${Versions.krate}"

    // image loading and caching
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    // graphs, charts
    const val mpAndroidChart = "com.github.PhilJay:MPAndroidChart:${Versions.mpAndroidChart}"

    // tools
    const val chucker = "com.github.ChuckerTeam.Chucker:library:${Versions.chucker}"
    const val chuckerNoOp = "com.github.ChuckerTeam.Chucker:library-no-op:${Versions.chucker}"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
}

object TestLibraries {

    private object Versions {
        const val junit4 = "4.12"
        const val test = "1.1.1"
        const val espresso = "3.2.0"
        const val kotlinCoroutinesTest = "1.3.7"
        const val threeTenAbp = "1.2.2"
        const val kotest = "4.0.2"
        const val mockk = "1.9.3"
        const val room = "2.2.5"
        const val navigation = "2.3.0-alpha06"
        const val fragment = "1.3.0-alpha04"
    }

    const val junit4 = "junit:junit:${Versions.junit4}"
    const val testCore = "androidx.test:core:${Versions.test}"
    const val testRunner = "androidx.test:runner:${Versions.test}"
    const val testExtJUnit = "androidx.test.ext:junit:${Versions.test}"
    const val testRules = "androidx.test:rules:${Versions.test}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val kotlinCoroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutinesTest}"
    const val threeTenAbp = "org.threeten:threetenbp:${Versions.threeTenAbp}"
    const val kotestRunner = "io.kotest:kotest-runner-junit5-jvm:${Versions.kotest}"
    const val kotestAssertions = "io.kotest:kotest-assertions-core-jvm:${Versions.kotest}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val roomTesting = "androidx.room:room-testing:${Versions.room}"
    const val navigationTesting = "androidx.navigation:navigation-testing:${Versions.navigation}"
    const val fragmentTesting = "androidx.fragment:fragment-testing:${Versions.fragment}"
}