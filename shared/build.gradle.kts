import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    // Apply the necessary plugins for Kotlin Multiplatform, Android, Serialization, and other features
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.build.konfig)
    alias(libs.plugins.skie)
    alias(libs.plugins.multiplatform.resources)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_21)
                }
            }
        }
    }

    jvmToolchain(21)

    // Define iOS targets (X64, ARM64, and Simulator ARM64)
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"  // Name of the generated shared framework
            isStatic = false  // Generate a dynamic framework (not static)
            freeCompilerArgs += "-Xbinary=bundleId=com.finblue"  // Set the bundle ID for iOS
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }

        commonMain.dependencies {
            // Android-specific lifecycle and Ktor core client for networking
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.kotlinx.coroutines.core)

            // Ktor client and plugins for network handling and serialization
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.plugin.logging)
            implementation(libs.ktor.plugin.content.negotiation)
            implementation(libs.kotlinx.serialization)

            // Koin for dependency injection and Compose integration
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Image loading libraries for Compose and network integration
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            // Logging utility and resources management
            implementation(libs.napier)
            implementation(libs.moko.resources)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.android.driver)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.native.driver)
        }
    }
}

android {
    namespace = "com.finblue.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        // Minimum SDK version for Android
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        // Set compatibility to Java 21
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

buildkonfig {
    packageName = "com.finblue"  // Define package name for BuildKonfig

    defaultConfigs {
        // Get API_KEY from gradleLocalProperties and inject it into the build config
        val apiKey: String = gradleLocalProperties(rootDir, providers).getProperty("API_KEY")
        buildConfigField(FieldSpec.Type.STRING, "API_KEY", apiKey)  // Add API_KEY to BuildConfig
    }
}

skie {
    features {
        enableSwiftUIObservingPreview = true  // Enable SwiftUI Observing Preview for iOS
    }
}

multiplatformResources {
    resourcesPackage.set("com.finblue")  // Set the package name for resources
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.finblue.db")  // Define package for database classes
        }
    }
}