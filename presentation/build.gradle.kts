plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
    kotlin("kapt")
}

android {
    namespace = "com.housweet.presentation"
    compileSdk = 35

    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
        consumerProguardFiles("consumer-rules.pro")
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    //내부 모듈
    implementation(project(":domain"))

    //Android 기본 구성
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.storage)

    //Jetpack Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material)
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation(libs.coil.kt.coil.compose)

    //Navigation (Compose용)
    implementation("androidx.navigation:navigation-compose:2.7.7")

    //Hilt (의존성 주입)
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation(libs.androidx.ui.tooling.preview.android)
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    //테스트
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
}