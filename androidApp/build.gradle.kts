plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.multiplatform.resources)
}

android {
    namespace = "com.finblue"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.finblue"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" // Exclude license files from resources
        }
    }
    buildTypes {
        getByName("release") {
            // set to true for obfuscation
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    // Compose Libraries
    implementation(compose.preview)
    implementation(compose.material3)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.ui)
    implementation(compose.components.resources)
    implementation(compose.components.uiToolingPreview)

    // AndroidX Libraries
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Koin Libraries (Dependency Injection)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)

    // Networking & Serialization
    implementation(libs.ktor.client.okhttp)
    implementation(libs.kotlinx.serialization)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor)

    // Multiplatform Resources
    implementation(libs.moko.resources)

    // Shared Module Dependency
    implementation(projects.shared)
    implementation(libs.androidx.navigation.common.android)

    // Debugging Tools
    debugImplementation(compose.uiTooling)

    implementation(libs.kotlinx.datetime)

    // Navigation
    implementation(libs.androidx.navigation)
}