plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
    id("dagger.hilt.android.plugin")
    id("org.jlleitschuh.gradle.ktlint")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(Versions.Android.sdk)
    buildToolsVersion(Versions.Android.version)

    defaultConfig {
        applicationId = Versions.App.id
        minSdkVersion(Versions.Android.minSdk)
        targetSdkVersion(Versions.Android.sdk)
        multiDexEnabled = true
        versionCode = Versions.App.versionCode
        versionName = Versions.App.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                // Refer https://developer.android.com/jetpack/androidx/releases/room#compiler-options
                arguments(
                    mapOf(
                        "room.schemaLocation" to "$projectDir/schemas",
                        "room.incremental" to "true",
                        "room.expandProjection" to "true"
                    )
                )
            }
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    flavorDimensions("default")
    productFlavors {
        create("prod") {
            applicationId = Versions.App.id
        }
        create("dev") {
            applicationId = Versions.App.devId
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
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

    testOptions {
        unitTests.isIncludeAndroidResources = true
        animationsDisabled = true
    }

    kapt {
        useBuildCache = true
        javacOptions {
            // Increase the max count of errors from annotation processors.
            // Default is 100.
            option("-Xmaxerrs", 500)
        }
    }
    testBuildType = "debug"

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

allprojects {
    repositories {
        maven { setUrl("https://oss.jfrog.org/libs-snapshot") }
        maven { setUrl("https://jitpack.io") }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":base"))
    implementation(Dependencies.OkHttp.cookie)
    implementation("com.google.android.gms:play-services-auth:18.1.0")
    implementation("com.facebook.android:facebook-android-sdk:5.0.0")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:chromecast-sender:0.23")

    // Room
    implementation(Dependencies.Room.runtime)
    implementation(Dependencies.Room.ktx)
    implementation(Dependencies.Room.rxjava3)
    implementation(files("libs\\YouTubeAndroidPlayerApi.jar"))
    implementation("androidx.navigation:navigation-fragment-ktx:2.2.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.2.2")
    androidTestImplementation("junit:junit:4.12")
    kapt(Dependencies.Room.annotation)
    // Glide
    implementation(Dependencies.Glide.transformations)
    implementation(Dependencies.Glide.gpuImage)
    implementation(Dependencies.Glide.core)
    kapt(Dependencies.Glide.compiler)
    // Navigation Component
    implementation(Dependencies.AndroidX.navFragment)
    implementation(Dependencies.AndroidX.navUI)
    implementation(Dependencies.AndroidX.navDynamicFeatures)
    // Hilt
    implementation(Dependencies.Hilt.hilt)
    kapt(Dependencies.Hilt.hiltCompiler)
    implementation(Dependencies.Hilt.viewModel)
    kapt(Dependencies.Hilt.viewModelCompiler)
    // Permissions Dispatcher
    implementation(Dependencies.Helper.Permissions)
    kapt(Dependencies.Helper.PermissionsCompiler)
    //Slider
    implementation(Dependencies.Slider.indicator)
    //Load PDF
    implementation(Dependencies.LoadPDF.pdf)
    implementation(Dependencies.LoadPDF.loader)
}

ktlint {
    android.set(true)
    outputColorName.set("RED")
}