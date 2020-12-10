plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(Versions.Android.sdk)
    buildToolsVersion(Versions.Android.version)

    defaultConfig {
        minSdkVersion(Versions.Android.minSdk)
        targetSdkVersion(Versions.Android.sdk)
        versionCode = Versions.App.versionCode
        versionName = Versions.App.versionName

        mapOf(
            testInstrumentationRunner to "androidx.test.runner.AndroidJUnitRunner",
            consumerProguardFiles to "consumer-rules.pro"
        )
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // Kotlin
    api(Dependencies.Kotlin.stdLib)
    // Android
    api(Dependencies.AndroidX.appCompat)
    api(Dependencies.AndroidX.coreKtx)
    api(Dependencies.AndroidX.constraintLayout)
    api(Dependencies.AndroidX.swipeRefreshLayout)
    api(Dependencies.AndroidX.recyclerview)
    api(Dependencies.AndroidX.viewPager2)
    api(Dependencies.AndroidX.multidex)
    api(Dependencies.AndroidX.viewBinding)
    api(Dependencies.AndroidX.lifecycleCommonJava8)
    // Logging
    api(Dependencies.Logging.logger)
    api(Dependencies.Logging.timber)
    // Material Design
    api(Dependencies.Material.design)
    api(Dependencies.Material.dialog)
    // Retrofit
    api(Dependencies.Retrofit.core)
    api(Dependencies.Retrofit.gsonConverter)
    api(Dependencies.Retrofit.rxJavaAdapter)
    // Gson
    api(Dependencies.Gson.gson)
    // OkHttp
    api(Dependencies.OkHttp.okHttp)
    api(Dependencies.OkHttp.okHttpLogging)
    api(Dependencies.OkHttp.okHttpURLConnection)
    api(Dependencies.OkHttp.stetho)
    api(Dependencies.OkHttp.stethoOkhttp)
    // Architecture Components
    api(Dependencies.AndroidX.viewModel)
    api(Dependencies.AndroidX.lifecycle)
    // Paging
    api(Dependencies.Paging.runtime)
    api(Dependencies.Paging.rxJava3)
    // RxTools
    api(Dependencies.RxTools.rxJava)
    api(Dependencies.RxTools.rxAndroid)
    api(Dependencies.RxTools.rxBinding)
    api(Dependencies.RxTools.rxBindingCore)
    api(Dependencies.RxTools.rxBindingAppcompat)
    api(Dependencies.RxTools.rxBindingRecyclerview)
    api(Dependencies.RxTools.rxBindingSwipeRefresh)
    api(Dependencies.RxTools.rxBindingViewPager)
    api(Dependencies.RxTools.rxBindingMaterial)
    // Helper
    api(Dependencies.Helper.flexboxLayout)
    api(Dependencies.Helper.sdp)
    api(Dependencies.Helper.ssp)
    api(Dependencies.Helper.flexibledivider)
    // Testing
    testApi(Dependencies.Testing.jUnit)
    androidTestApi(Dependencies.Testing.extJUnit)
    androidTestApi(Dependencies.Testing.espresso)
    androidTestApi(Dependencies.Testing.runner)
}