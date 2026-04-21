# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Native Android app ("Tadeos2") for pet care tracking, written in Kotlin with Jetpack Compose. UI copy, validation, and error messages are in Spanish — match that language when adding user-facing strings.

- `applicationId` / `namespace`: `com.tadeos.app`
- `minSdk` 24, `targetSdk` 36, `compileSdk` 36.1, JVM target 11
- Single Gradle module: `:app`

## Commands

Gradle wrapper is checked in. On Windows use `gradlew.bat`; on bash (this environment) use `./gradlew`.

- Build debug APK: `./gradlew :app:assembleDebug`
- Install to connected device/emulator: `./gradlew :app:installDebug`
- Unit tests: `./gradlew :app:testDebugUnitTest` (run a single test: add `--tests "com.tadeos.app.SomeTest.method"`)
- Instrumented tests: `./gradlew :app:connectedDebugAndroidTest`
- Lint: `./gradlew :app:lintDebug`
- Full clean: `./gradlew clean`

### Debug-build quirk (important)

The project sits inside a OneDrive-synced path, which caused Android Studio to install stale screens. [app/build.gradle.kts](app/build.gradle.kts) compensates with custom tasks that run on every debug build:

- `preDebugBuild` → deletes `app/build/` and the legacy `app/build/outputs/apk/debug/app-debug.apk` before compiling.
- `packageDebug` / `assembleDebug` → mirrors the freshly built APK back into `app/build/outputs/apk/debug/` so tools that look at the legacy path pick up the new binary.

Do not remove these tasks unless the project is moved off OneDrive; doing so reintroduces the stale-install bug. If a debug build seems to ignore source changes, first confirm these tasks ran.

## Configuration that must exist locally

Neither file is committed; builds will fail or fall back to placeholders without them.

- `app/google-services.json` — real Firebase config. See [app/google-services.example.json](app/google-services.example.json) for shape. `.gitignore` explicitly allows only the `.example` variant.
- `local.properties` — may define `FACEBOOK_APP_ID` and `FACEBOOK_CLIENT_TOKEN`. `app/build.gradle.kts` reads them via `localOrGradleProperty(...)` and wires them into `resValue` strings (`facebook_app_id`, `facebook_client_token`, `fb_login_protocol_scheme`). If missing, fallbacks are used but Facebook login will not work.

Google Sign-In additionally requires `default_web_client_id` to be present in generated resources (provided by `google-services.json` when Google auth is enabled in Firebase, with SHA-1/SHA-256 registered). [SocialAuthRepository.kt](app/src/main/java/com/example/tadeos/data/repository/SocialAuthRepository.kt) surfaces a Spanish error message when this is missing.

## Architecture

Single-Activity Compose app. [MainActivity.kt](app/src/main/java/com/example/tadeos/MainActivity.kt) hosts `AppNavigation()` inside `TadeosTheme`, and forwards legacy `onActivityResult` to `FacebookAuthBridge` (Facebook SDK still uses the deprecated callback).

### Navigation

All routes and nav-arg keys are centralized in [AppRoutes.kt](app/src/main/java/com/example/tadeos/navigation/AppRoutes.kt); the `NavHost` and all screen wiring live in [AppNavigation.kt](app/src/main/java/com/example/tadeos/navigation/AppNavigation.kt). When adding a screen, add the route object there and register the `composable` in the same file rather than scattering route strings. Screens receive plain lambdas for navigation — they do not take `NavController` directly.

Screen packages under `ui/screens/`:
- `auth/` — Login, Register
- `home/` — Home dashboard
- `pets/` — list / detail / new-pet
- `health/` — pet selector → HealthMenu → Exam / Diet / Mood / Medication entry forms (each takes a `petId` nav arg)
- `profile/`, `legal/`

Shared Compose building blocks live in [ui/components/](app/src/main/java/com/example/tadeos/ui/components/) (`ScreenContainer`, `TadeosHeader`, `RemoteImages`). Reuse them to keep header proportions and image loading consistent — a recurring concern in recent commits.

### Data layer

No coroutines/Flow, no ViewModel layer, no DI framework. Screens call repository singletons directly and hold UI state with `remember`/`mutableStateOf`. Keep new code consistent with this style unless the user asks to introduce a new pattern.

- [TadeosFirebaseRepository.kt](app/src/main/java/com/example/tadeos/data/repository/TadeosFirebaseRepository.kt) — `object` singleton wrapping FirebaseAuth, Firestore, and Storage. All async APIs are callback-based `(Boolean, String?) -> Unit` or `(T?, String?) -> Unit`. Observers return `ListenerRegistration` that the caller must `remove()`.
  - Firestore shape: `users/{uid}` document, with subcollection `users/{uid}/pets/{petId}`. Matches [firebase/firestore.rules](firebase/firestore.rules) — the user can only read/write their own tree.
  - Storage paths: `users/{uid}/profile/profile.jpg`, `users/{uid}/pets/{petId}/profile.jpg`.
  - Every write/upload is wrapped in a 20 s timeout (`FIREBASE_TIMEOUT_MS`) via `guardedBooleanCompletion` / the handler in `uploadImage`, because Firestore/Storage not being enabled in the console would otherwise hang the UI forever. Preserve this pattern when adding new Firebase calls.
  - `friendlyFirebaseMessage` translates common Firebase exceptions (permission denied, API not enabled, network, bucket missing) into Spanish user-facing strings. Route new error paths through it instead of surfacing raw exception text.
- [SocialAuthRepository.kt](app/src/main/java/com/example/tadeos/data/repository/SocialAuthRepository.kt) — Google (Credential Manager + `GetSignInWithGoogleOption`) and Facebook sign-in, exposing `suspend` functions that return `SocialAuthResult`.
- [FacebookAuthBridge.kt](app/src/main/java/com/example/tadeos/data/repository/FacebookAuthBridge.kt) — the `onActivityResult` shim required by the Facebook SDK.
- [data/model/](app/src/main/java/com/example/tadeos/data/model/) — plain `data class`es (`Pet`, `UserProfile`, `HealthRecord`) with defaulted fields so Firestore `toObject`-style mapping stays forgiving. The `Pet` ↔ Firestore mapping is done by hand in `toPet` / `toFirestoreMap` inside the repository; when you add a field to `Pet`, update both functions.
- [data/mock/](app/src/main/java/com/example/tadeos/data/mock/) — sample data used by previews / offline flows; not persisted.

### Health records

Health screens currently persist only the `petId` nav argument and save via `onSaveClick = { navController.popBackStack() }` — the repository does not yet expose a `saveHealthRecord` API. If asked to wire persistence, add methods to `TadeosFirebaseRepository` following the existing callback-with-timeout pattern; do not invent a new async style.
