plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.jclemus.androidsamples"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jclemus.ui_app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin { jvmToolchain(17) }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // -------- Compose --------
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.core)
    implementation(libs.bundles.compose.ui)
    debugImplementation(libs.compose.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
    androidTestImplementation(libs.compose.ui.test.junit4)

    // -------- Views (legacy) --------
    // implementation(libs.bundles.views.core)

    // -------- Utilities --------
    implementation(libs.coil.compose)
    implementation(libs.coil.core)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.kotlinx.serialization.json)

    // Retrofit + Kotlinx Serialization
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // -------- Test --------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
}