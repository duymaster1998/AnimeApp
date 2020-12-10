object Dependencies {

    object Logging {
        const val logger = "com.orhanobut:logger:${Versions.logger}"
        const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    }

    object Plugin {
        const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.gradle}"
        const val hiltGradlePlugin =
            "com.google.dagger:hilt-android-gradle-plugin:${Versions.ArchitectureComponents.hilt}"
        const val navGradlePlugin =
            "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.ArchitectureComponents.nav}"
    }

    object Testing {
        const val jUnit = "junit:junit:${Versions.Testing.jUnit}"
        const val extJUnit = "androidx.test.ext:junit:${Versions.Testing.extJUnit}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.Testing.espresso}"
        const val runner = "androidx.test:runner:${Versions.Testing.runner}"
    }

    object AndroidX {
        const val lifecycleCommonJava8 =
            "androidx.lifecycle:lifecycle-common-java8:${Versions.AndroidX.lifecycleCommonJava8}"
        const val viewBinding = "androidx.databinding:viewbinding:${Versions.AndroidX.viewBinding}"
        const val multidex = "androidx.multidex:multidex:${Versions.AndroidX.multidex}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.AndroidX.appcompat}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.AndroidX.coreKtx}"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${Versions.AndroidX.constraintLayout}"
        const val recyclerview =
            "androidx.recyclerview:recyclerview:${Versions.AndroidX.recyclerview}"
        const val swipeRefreshLayout =
            "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.AndroidX.swipeRefreshLayout}"
        const val viewPager2 =
            "androidx.viewpager2:viewpager2:${Versions.ArchitectureComponents.viewpager2}"
        const val viewModel =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.ArchitectureComponents.lifecycle}"
        const val lifecycle =
            "androidx.lifecycle:lifecycle-extensions:${Versions.ArchitectureComponents.lifecycle}"
        const val navFragment =
            "androidx.navigation:navigation-fragment-ktx:${Versions.ArchitectureComponents.nav}"
        const val navUI =
            "androidx.navigation:navigation-ui-ktx:${Versions.ArchitectureComponents.nav}"
        const val navDynamicFeatures =
            "androidx.navigation:navigation-dynamic-features-fragment:${Versions.ArchitectureComponents.nav}"
    }

    object Kotlin {
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:${Versions.kotlin}"
    }

    object Hilt {
        const val hilt = "com.google.dagger:hilt-android:${Versions.ArchitectureComponents.hilt}"
        const val hiltCompiler =
            "com.google.dagger:hilt-android-compiler:${Versions.ArchitectureComponents.hilt}"
        const val viewModel =
            "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.ArchitectureComponents.hiltViewModel}"
        const val viewModelCompiler =
            "androidx.hilt:hilt-compiler:${Versions.ArchitectureComponents.hiltViewModel}"
    }

    object Paging {
        const val runtime =
            "androidx.paging:paging-runtime:${Versions.ArchitectureComponents.paging}"
        const val rxJava3 =
            "androidx.paging:paging-rxjava3:${Versions.ArchitectureComponents.paging}"
    }

    object Room {
        const val runtime = "androidx.room:room-runtime:${Versions.ArchitectureComponents.room}"
        const val annotation = "androidx.room:room-compiler:${Versions.ArchitectureComponents.room}"
        const val ktx = "androidx.room:room-ktx:${Versions.ArchitectureComponents.room}"
        const val rxjava3 = "androidx.room:room-rxjava3:${Versions.ArchitectureComponents.room}"
    }

    object Glide {
        const val core = "com.github.bumptech.glide:glide:${Versions.Glide.core}"
        const val compiler = "com.github.bumptech.glide:compiler:${Versions.Glide.core}"
        const val transformations =
            "jp.wasabeef:glide-transformations:${Versions.Glide.transformations}"
        const val gpuImage = "jp.co.cyberagent.android:gpuimage:${Versions.Glide.gpuImage}"
    }

    object Gson {
        const val gson = "com.google.code.gson:gson:${Versions.Gson.gson}"
    }

    object Material {
        const val design = "com.google.android.material:material:${Versions.Material.design}"
        const val dialog = "com.shreyaspatil:MaterialDialog:${Versions.Material.dialog}"
    }

    object RxTools {
        const val rxJava = "io.reactivex.rxjava3:rxjava:${Versions.RxTools.rxJava}"
        const val rxAndroid = "io.reactivex.rxjava3:rxandroid:${Versions.RxTools.rxAndroid}"
        const val rxBinding = "com.jakewharton.rxbinding4:rxbinding:${Versions.RxTools.rxBinding}"
        const val rxBindingViewPager =
            "com.jakewharton.rxbinding4:rxbinding-viewpager2:${Versions.RxTools.rxBinding}"
        const val rxBindingCore =
            "com.jakewharton.rxbinding4:rxbinding-core:${Versions.RxTools.rxBinding}"
        const val rxBindingAppcompat =
            "com.jakewharton.rxbinding4:rxbinding-appcompat:${Versions.RxTools.rxBinding}"
        const val rxBindingRecyclerview =
            "com.jakewharton.rxbinding4:rxbinding-recyclerview:${Versions.RxTools.rxBinding}"
        const val rxBindingSwipeRefresh =
            "com.jakewharton.rxbinding4:rxbinding-swiperefreshlayout:${Versions.RxTools.rxBinding}"
        const val rxBindingMaterial =
            "com.jakewharton.rxbinding4:rxbinding-material:${Versions.RxTools.rxBinding}"
    }

    object Retrofit {
        const val core = "com.squareup.retrofit2:retrofit:${Versions.Retrofit.retrofit}"
        const val gsonConverter =
            "com.squareup.retrofit2:converter-gson:${Versions.Retrofit.retrofit}"
        const val rxJavaAdapter =
            "com.squareup.retrofit2:adapter-rxjava3:${Versions.Retrofit.retrofit}"
    }

    object OkHttp {
        const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.Retrofit.okHttp}"
        const val okHttpLogging =
            "com.squareup.okhttp3:logging-interceptor:${Versions.Retrofit.okHttp}"
        const val okHttpURLConnection =
            "com.squareup.okhttp3:okhttp-urlconnection:${Versions.Retrofit.okHttp}"
        const val cookie = "com.github.franmontiel:PersistentCookieJar:${Versions.Retrofit.cookie}"
        const val stetho = "com.facebook.stetho:stetho:${Versions.Retrofit.stetho}"
        const val stethoOkhttp = "com.facebook.stetho:stetho-okhttp3:${Versions.Retrofit.stetho}"
    }

    object Helper {
        const val flexibledivider =
            "com.yqritc:recyclerview-flexibledivider:${Versions.Helper.flexibledivider}"
        const val flexboxLayout = "com.google.android:flexbox:${Versions.Helper.flexboxLayout}"
        const val sdp = "com.intuit.sdp:sdp-android:${Versions.Helper.sdp}"
        const val ssp = "com.intuit.ssp:ssp-android:${Versions.Helper.ssp}"
        const val ktLint = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.Helper.ktLint}"
        const val Permissions =
            "org.permissionsdispatcher:permissionsdispatcher:${Versions.permissions}"
        const val PermissionsCompiler =
            "org.permissionsdispatcher:permissionsdispatcher-processor:${Versions.permissions}"
    }

    object Slider{
        const val indicator = "me.relex:circleindicator:${Versions.Slider.indicator}"
    }
}