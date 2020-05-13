plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.safeArgs)
}

android {
    compileSdkVersion(AndroidSdk.compileSdkVersion)

    defaultConfig {
        applicationId = "dev.jakal.pandemicwatch"
        minSdkVersion(AndroidSdk.minSdkVersion)
        targetSdkVersion(AndroidSdk.targetSdkVersion)
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs +=
            "-Xuse-experimental=" +
                    "kotlin.Experimental," +
                    "kotlinx.coroutines.ExperimentalCoroutinesApi," +
                    "kotlinx.coroutines.FlowPreview"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {

    // jetbrains
    implementation(Libraries.kotlinStdLib)

    // google
    implementation(Libraries.appCompat)
    implementation(Libraries.material)
    implementation(Libraries.coreKtx)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.lifecycleExtensions)
    implementation(Libraries.lifecycleLivedataKtx)
    implementation(Libraries.lifecycleViewModelKtx)
    implementation(Libraries.lifecycleViewModelSavedState)
    kapt(Libraries.lifecycleCompiler)
    implementation(Libraries.roomRuntime)
    kapt(Libraries.roomCompiler)
    implementation(Libraries.roomKtx)
    implementation(Libraries.swipeToRefresh)
    implementation(Libraries.navigationFragment)
    implementation(Libraries.navigationUi)
    implementation(Libraries.navigationFragmentKtx)
    implementation(Libraries.navigationUiKtx)
    implementation(Libraries.fragment)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.workManager)
    implementation(Libraries.workManagerKtx)

    // dependency injection
    implementation(Libraries.koinAndroid)
    implementation(Libraries.koinScope)
    implementation(Libraries.koinViewModel)
    implementation(Libraries.koinFragment)

    //networking
    implementation(Libraries.okhttp)
    implementation(Libraries.okhttpLoggingInterceptor)
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitMoshiConverter)

    // json
    implementation(Libraries.moshi)
    kapt(Libraries.moshiKotlinCodegen)

    // logging
    implementation(Libraries.timber)

    // date and time
    api(Libraries.threeTenAbp)

    // shared preferences wrapper
    implementation(Libraries.krate)
    implementation(Libraries.krateMoshiCodegen)

    // image loading and caching
    implementation(Libraries.glide)
    kapt(Libraries.glideCompiler)

    // graphs, charts
    implementation(Libraries.mpAndroidChart)

    // tools
    debugImplementation(Libraries.chucker)
    releaseImplementation(Libraries.chuckerNoOp)
    debugImplementation(Libraries.leakCanary)

    // test
    testImplementation(TestLibraries.junit4)
    testImplementation(TestLibraries.kotlinCoroutinesTest)
    testImplementation(TestLibraries.threeTenAbp)
    testImplementation(TestLibraries.kotest)
    testImplementation(TestLibraries.mockk)
    testImplementation(TestLibraries.roomTesting)
    androidTestImplementation(TestLibraries.testRunner)
    androidTestImplementation(TestLibraries.espresso)
    androidTestImplementation(TestLibraries.navigationTesting)
    debugImplementation(TestLibraries.fragmentTesting)
}