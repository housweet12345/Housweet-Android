import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

val properties = Properties().apply {
    load(FileInputStream("${rootDir}/local.properties"))
}

val kakaoRedirectUri = properties["kakaoLogin_Redirect_Uri"] ?: ""

android {
    namespace = "com.housweet.presentation"
    compileSdk = 35

    defaultConfig {
        manifestPlaceholders += mapOf()
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
        consumerProguardFiles("consumer-rules.pro")
        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["Kakao_Redirect_URI"] = kakaoRedirectUri as String
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
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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

    //Navigation (Compose용)
    implementation("androidx.navigation:navigation-compose:2.7.7")

    //Hilt (의존성 주입)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //테스트
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Compose
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Kotlin Coroutine
    implementation(libs.kotlinx.coroutines.android)

    // Kakao
    implementation(libs.v2.user)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    // Naver Map
    implementation (libs.naver.map.compose)
}

kapt {
    correctErrorTypes = true
}