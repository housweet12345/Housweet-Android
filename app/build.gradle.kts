import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
    kotlin("kapt")
}

val properties = Properties().apply {
    load(FileInputStream("${rootDir}/local.properties"))
}

val kakaoApiKey = properties["KAKAO_API_KEY"] as? String ?: ""

android {
    namespace = "com.housweet.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.housweet.app"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildFeatures {
            compose = true
        }

//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.3"  // Compose 컴파일러 버전
//    }
        buildConfigField("String", "Kakao_API_KEY", "\"$kakaoApiKey\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.storage)

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    implementation("com.google.firebase:firebase-analytics")

    //Jetpack Compose
    implementation("androidx.compose.ui:ui:1.5.3")
    implementation("androidx.compose.material:material:1.5.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.3")
    implementation("androidx.activity:activity-compose:1.6.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")
    implementation("androidx.navigation:navigation-compose:2.5.3")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Socket.IO
    implementation("io.socket:socket.io-client:1.0.0") {
        exclude(group = "org.json", module = "json")
    }

    // Kotlin Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    // Kakao
    implementation(libs.v2.user)

    //Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}