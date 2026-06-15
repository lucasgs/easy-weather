# EasyWeather

EasyWeather is an Android weather app built with Kotlin and Jetpack Compose.
It can fetch weather for the device location or a manually searched city, and presents current, hourly, and multi-day forecast details in a lightweight UI.

## Features
- Current weather for the device location
- Manual city search fallback when location is unavailable
- Hourly forecast
- Multi-day forecast
- Weather details including humidity, precipitation chance, sunrise, and sunset
- Pull-to-refresh
- Jetpack Compose UI
- Hilt dependency injection
- Retrofit-based network layer
- Coroutines and Flow for async/state handling

## Tech stack
- Kotlin
- Jetpack Compose
- Android ViewModel
- Lifecycle
- Dagger Hilt
- Coroutines / Flow
- Retrofit
- Open-Meteo API

## Project structure
- `app/src/main/java/com/dendron/easyweather/presentation` — Compose UI, screens, ViewModel
- `app/src/main/java/com/dendron/easyweather/domain` — domain models and contracts
- `app/src/main/java/com/dendron/easyweather/data` — location + remote data sources
- `app/src/test` — unit tests
- `app/src/androidTest` — instrumentation tests

## Requirements
- Android Studio Hedgehog or newer recommended
- JDK 17+
- Android SDK 34

## Build tooling
- Android Gradle Plugin 8.5.2
- Gradle 8.7
- Kotlin 1.9.24

## Getting started
1. Open the project in Android Studio.
2. Sync Gradle.
3. Run the `app` configuration on a device or emulator.
4. Grant location permission when prompted.

## Notes
- The app supports both location-based weather and manual city search.
- Internet access is required to fetch weather data.

## Roadmap
See `IMPROVEMENT_PLAN.md` for the proposed UX, UI, architecture, accessibility, and testing improvements.

## References
- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Android ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Android Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Retrofit](https://github.com/square/retrofit)
- [Open-Meteo](https://open-meteo.com/)
