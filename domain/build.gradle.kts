plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.stdlib)

    // Kotlin Coroutine
    implementation(libs.kotlinx.coroutines.core)
}