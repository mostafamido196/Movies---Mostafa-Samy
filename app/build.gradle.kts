plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)


    //
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.samy.mostafasamy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.samy.mostafasamy"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    dataBinding {
        true
    }
    viewBinding {
        true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)



    // Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // navigation
    implementation( libs.androidx.navigation.fragment.ktx)
    implementation( libs.androidx.navigation.ui.ktx)

//    noinspection GradleDependency
//    implementation "androidx.multidex:multidex:2.0.1"

    //coroutines
    implementation (libs.kotlinx.coroutines.android)

    // Retrofit
    implementation (libs.retrofit2.retrofit)
    implementation (libs.logging.interceptor)
    implementation (libs.converter.gson)

    // For ViewModel
    implementation (libs.androidx.lifecycle.extensions)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)

    //ssp sdp
    implementation (libs.ssp.android)
    implementation (libs.sdp.android)

    //glide img downloader
    implementation (libs.github.glide)

    //room
    implementation (libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)



}

//hilt
kapt {
    correctErrorTypes = true
    useBuildCache = true
}