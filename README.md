<h1 align="center">
  <br>
    [![AppIcon](/docs/media/AppIcon.png)](https://github.com/KarelHudera/FinBlue)
  <br>
   [![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=plastic)](https://www.apache.org/licenses/LICENSE-2.0)
   [![FinBlue Android](https://img.shields.io/github/actions/workflow/status/KarelHudera/FinBlue/FinBlue-Android.yml?branch=main&logo=Android&style=plastic)](https://github.com/KarelHudera/FinBlue/blob/main/.github/workflows/FinBlue-Android.yml)
   [![FinBlue iOS](https://img.shields.io/github/actions/workflow/status/KarelHudera/FinBlue/FinBlue-iOS.yml?branch-main&logo=Apple&style=plastic)](https://github.com/KarelHudera/FinBlue/blob/main/.github/workflows/FinBlue-iOS.yml)
  <br>
</h1>

This is a Kotlin Multiplatform project targeting Android, iOS.

* `/androidApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - `commonMain` is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the
      folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for
  your project.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the
  platform-specific folders here too.

Learn more
about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

## Git

For information on commit message guidelines, refer
to [Git conventional messages](https://www.conventionalcommits.org/en/v1.0.0/)

## Prerequisites

Add your API key in the `local.properties` file:

```
API_KEY=api_key
```

## Design

[![Figma](/docs/media/BadgeFigma.svg)](https://www.figma.com/design/nKAdZ3InF55K4M7v27El0G/finance-mockup)

## Libraries

* [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) - Simplify the development
  of cross-platform projects and reduce the time spent writing and maintaining the same code for
  different platforms.
* [Ktor Client](https://ktor.io/docs/welcome.html) - Ktor includes a multiplatform asynchronous HTTP
  client, which allows you to make requests and handle responses.
* [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - Kotlin multiplatform /
  multi-format reflectionless serialization
* [Koin](https://github.com/InsertKoinIO/koin) - is a lightweight dependency injection framework for
  Kotlin & Kotlin Multiplatform.
* [Kotlin coroutines](https://developer.android.com/kotlin/coroutines) - Executing code
  asynchronously.
* [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) - is a
  state-holder observable flow that emits the current and new state updates to its collectors.
* [Coil-compose](https://coil-kt.github.io/coil/compose/) - An image loading library for Android
  backed by Kotlin Coroutines.
* [SKIE](https://github.com/touchlab/SKIE) - A tool for Kotlin Multiplatform development that
  enhances the Swift API published from Kotlin.
* [SQLDelight](https://github.com/sqldelight/sqldelight) - generates typesafe Kotlin APIs from your
  SQL statements.
* [moko-resources](https://github.com/icerockdev/moko-resources) - provides access to the resources
  on macOS, iOS, Android the JVM and JS/Browser with the support of the default system localization.
* [BuildKonfig](https://github.com/yshrsmz/BuildKonfig) - BuildConfig for Kotlin Multiplatform
  Project.
* [Napier](https://github.com/AAkira/Napier) - logger library for Kotlin Multiplatform project.