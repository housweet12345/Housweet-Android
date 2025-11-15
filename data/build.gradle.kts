import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("plugin.serialization") version "2.0.21"
    kotlin("kapt")
}

val properties = Properties().apply {
    load(FileInputStream("${rootDir}/local.properties"))
}

//val baseUrl = properties["base_url"] ?: ""
val baseUrl = properties["base_url"]?.let { "\"$it\"" } ?: "\"https://example.com\""
val userBaseUrl = properties["user_base_url"]?.let { "\"$it\"" } ?: "\"https://example.com\""
//val naverClientId = properties["naver_client_id"] ?: ""

android {
    namespace = "com.housweet.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

//      buildConfigField("String", "BASE_URL", baseUrl.toString())
        buildConfigField("String", "BASE_URL", baseUrl)
        buildConfigField("String", "USER_BASE_URL", userBaseUrl)
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("Boolean", "IS_DEBUG", "true")
        }
        release {
            buildConfigField("Boolean", "IS_DEBUG", "false")
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
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // JSON converter (예: kotlinx.serialization 사용 시)
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    //okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    //ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)

    // Serialization
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)

    //socket.io
    implementation("io.socket:socket.io-client:1.0.0")

    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //DataStore
    implementation(libs.androidx.datastore.preferences)

    // Kotlin Coroutine
    implementation(libs.kotlinx.coroutines.android)

    // Compose Runtime
    implementation(libs.androidx.runtime)

    //test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.runner)
}