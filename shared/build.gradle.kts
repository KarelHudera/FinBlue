import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
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

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = false
            freeCompilerArgs += "-Xbinary=bundleId=com.finblue"
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
            implementation(libs.androidx.lifecycle.viewmodel)

            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.plugin.logging)
            implementation(libs.ktor.plugin.content.negotiation)
            implementation(libs.kotlinx.serialization)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

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
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

buildkonfig {
    packageName = "com.finblue"

    defaultConfigs {
        val apiKey: String = gradleLocalProperties(rootDir, providers).getProperty("API_KEY")
        buildConfigField(FieldSpec.Type.STRING, "API_KEY", apiKey)
    }
}

skie {
    features {
        enableSwiftUIObservingPreview = true
    }
}

multiplatformResources {
    resourcesPackage.set("com.finblue")
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.finblue.db")
        }
    }
}