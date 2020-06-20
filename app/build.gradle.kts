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

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/AL2.0")
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/LGPL2.1")
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/LICENSE-notice.md")
    }

    sourceSets {
        getByName("androidTest") {
            java.srcDir("src/sharedTest/java")
        }
        getByName("test") {
            java.srcDir("src/sharedTest/java")
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {

    // jetbrains
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.kotlinCoroutines)
    implementation(Libraries.kotlinCoroutinesAndroid)

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
    implementation(Libraries.security)

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
    debugImplementation(Libraries.leakCanaryPlumber)

    // test
    testImplementation(TestLibraries.junit4)
    testImplementation(TestLibraries.kotlinCoroutinesTest)
    testImplementation(TestLibraries.threeTenAbp)
    testImplementation(TestLibraries.kotestRunner)
    testImplementation(TestLibraries.kotestAssertions)
    testImplementation(TestLibraries.kotestProperty)
    testImplementation(TestLibraries.mockk)
    testImplementation(TestLibraries.roomTesting)
    testImplementation(TestLibraries.flowTestObserver)
    androidTestImplementation(TestLibraries.testCore)
    androidTestImplementation(TestLibraries.testRunner)
    androidTestImplementation(TestLibraries.testExtJUnit)
    androidTestImplementation(TestLibraries.testRules)
    androidTestImplementation(TestLibraries.kotlinCoroutinesTest)
    androidTestImplementation(TestLibraries.espresso)
    androidTestImplementation(TestLibraries.kotestRunner)
    androidTestImplementation(TestLibraries.kotestAssertions)
    androidTestImplementation(TestLibraries.kotestProperty)
    androidTestImplementation(TestLibraries.navigationTesting)
    androidTestImplementation(TestLibraries.flowTestObserver)
    androidTestImplementation(TestLibraries.workManagerTesting)
    androidTestImplementation(TestLibraries.koinTest)
    androidTestImplementation(TestLibraries.mockkAndroid)
    debugImplementation(TestLibraries.fragmentTesting)
}