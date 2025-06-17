<h1 align="center">
    <a href="https://github.com/KarelHudera/FinBlue">
        <img src="/docs/media/AppIcon.png" alt="AppIcon" width="150">
    </a>
</h1>

[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=plastic)](https://www.apache.org/licenses/LICENSE-2.0)
[![FinBlue Android](https://img.shields.io/github/actions/workflow/status/KarelHudera/FinBlue/FinBlue-Android.yml?branch=main&logo=Android&style=plastic)](https://github.com/KarelHudera/FinBlue/blob/main/.github/workflows/FinBlue-Android.yml)
[![FinBlue iOS](https://img.shields.io/github/actions/workflow/status/KarelHudera/FinBlue/FinBlue-iOS.yml?branch-main&logo=Apple&style=plastic)](https://github.com/KarelHudera/FinBlue/blob/main/.github/workflows/FinBlue-iOS.yml)

## About

**FinBlue** is a personal finance and portfolio management application designed to provide users with a clear and structured overview of their investments across various asset classes. Whether you’re managing traditional assets like stocks and fiat currencies or alternative investments such as art, gold, and other commodities, FinBlue helps you stay organized and in control.

Key benefits include:

- **Comprehensive Asset Tracking**: Monitor a wide range of assets, including equities, cryptocurrencies, fiat currencies, precious metals, commodities, and alternative assets like art and collectibles.
    
- **Multi-Portfolio Support**: Manage multiple portfolios, each associated with different brokers or banks, allowing for organized tracking of diversified holdings.
    
- **Detailed Transaction Logging**: Record transactions with specifics such as type (buy/sell), quantity, price, currency, and notes, providing a thorough history of your investment activities.
    
- **Structured Data Management**: Utilize a well-defined database schema that categorizes assets and transactions effectively, enhancing data clarity and retrieval.
    
- **Privacy-Focused**: All data is stored locally on your device, ensuring that your financial information remains private and secure.
    

FinBlue is designed to be flexible and user-centric, making it suitable for both personal finance enthusiasts and advanced portfolio managers seeking a unified view of their diverse investments.

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

* [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) - Enables cross-platform development while maintaining a single codebase.
* [Ktor Client](https://ktor.io/docs/welcome.html) - An asynchronous, multiplatform HTTP client for making requests and handling responses.
* [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - Reflectionless, multiplatform serialization for Kotlin.
* [Koin](https://github.com/InsertKoinIO/koin) - A lightweight dependency injection framework for Kotlin and Kotlin Multiplatform.
* [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) - A framework for writing asynchronous, concurrent code in Kotlin.
* [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) - A state-holder observable flow that emits updates to its collectors.
* [Coil](https://coil-kt.github.io/coil/compose/) - An image loading library for Jetpack Compose powered by Kotlin Coroutines.
* [SDWebImageSwiftUI](https://github.com/SDWebImage/SDWebImageSwiftUI) - A SwiftUI image loading framework for iOS that supports caching and remote image loading.
* [SKIE](https://github.com/touchlab/SKIE) - A tool that improves Swift API interoperability with Kotlin Multiplatform.
* [SQLDelight](https://github.com/sqldelight/sqldelight) - A Kotlin library that generates type-safe APIs from SQL statements.
* [moko-resources](https://github.com/icerockdev/moko-resources) - Provides a way to access resources across multiple platforms, supporting system localization.
* [BuildKonfig](https://github.com/yshrsmz/BuildKonfig) - A configuration management tool for Kotlin Multiplatform projects.
* [Napier](https://github.com/AAkira/Napier) - A Kotlin Multiplatform logging library.  