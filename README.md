# XXHash Kotlin Multiplatform

[![Build](https://github.com/limuyang2/fast-xxhash-android/actions/workflows/build.yml/badge.svg)](https://github.com/limuyang2/fast-xxhash-android/actions/workflows/build.yml)
[![Test](https://github.com/limuyang2/fast-xxhash-android/actions/workflows/android-test.yml/badge.svg)](https://github.com/limuyang2/fast-xxhash-android/actions/workflows/android-test.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.limuyang2/xxhash.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.limuyang2/xxhash)
[![中文文档](https://img.shields.io/badge/%E6%96%87%E6%A1%A3-%E4%B8%AD%E6%96%87-blue)](https://github.com/limuyang2/fast-xxhash-android/blob/main/README_CN.md)

Kotlin Multiplatform bindings for [xxHash](https://github.com/Cyan4973/xxHash/) with support for Android, JVM, iOS, JS, and Wasm.

The core library is in `:lib`. This repository also includes demo apps for Android, iOS, and Web, plus a shared Compose UI module.

## Status

- Algorithms: `XXH32`, `XXH64`, `XXH3_64bits`, `XXH3_128bits`
- Targets: Android, JVM, iOS Arm64, iOS Simulator Arm64, JS, WasmJS
- Android uses JNI + the original C implementation
- iOS uses the same original C source implementation as Android, exposed through Kotlin/Native cinterop
- JS and WasmJS currently use a pure Kotlin implementation maintained in `webMain`

## Modules

- `lib`: publishable KMP library
- `lib_android_native`: Android JNI/native packaging module
- `commonApp`: shared Compose demo UI
- `androidApp`: Android demo app
- `iosApp`: iOS demo app
- `webApp`: JS/Wasm browser demo app

## Platform implementation notes

- Android: JNI wrapper over `lib_android_native/src/main/cpp/xxhash.c`
- iOS: same implementation strategy as Android, using the original `xxhash` C sources through Kotlin/Native cinterop
- JVM: JVM-side implementation backed by `org.lz4:lz4-java` and `net.openhft:zero-allocation-hashing`
- Web: pure Kotlin implementation in `lib/src/webMain/kotlin/io/github/limuyang2/xxhash/lib`

This means the public API is shared, but the backend differs by target.

## Installation

For consumers, use the root KMP coordinate:

[![Maven Central](https://img.shields.io/maven-central/v/io.github.limuyang2/xxhash.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.limuyang2/xxhash)
```kotlin
dependencies {
    implementation("io.github.limuyang2:xxhash:<latest-version>")
}
```

Maven Central:

- `io.github.limuyang2:xxhash`
- `io.github.limuyang2:xxhash-android`
- `io.github.limuyang2:xxhash-jvm`
- `io.github.limuyang2:xxhash-js`
- `io.github.limuyang2:xxhash-wasm-js`

The root `xxhash` artifact is the normal entry point for KMP projects.

## Usage

### Kotlin

```kotlin
import io.github.limuyang2.xxhash.lib.xxh32
import io.github.limuyang2.xxhash.lib.xxh64
import io.github.limuyang2.xxhash.lib.xxh3As64
import io.github.limuyang2.xxhash.lib.xxh3As128

val text = "Hello, World!"

val h32 = text.xxh32()
val h64 = text.xxh64()
val h3_64 = text.xxh3As64()
val h3_128 = text.xxh3As128() // longArrayOf(low64, high64)

val bytes = text.encodeToByteArray()
val slice = bytes.xxh32(offset = 7, length = 6)
val seeded = bytes.xxh64(seed = 42)
```

### Java

```java
byte[] data = "Hello, World!".getBytes();

long h32 = XXHash.xxh32(data, 0);
long h64 = XXHash.xxh64(data, 0);
long h3_64 = XXHash.xxh3_64bits(data);
long h3_64_seed = XXHash.xxh3_64bitsWithSeed(data, 42);

long[] h3_128 = XXHash.xxh3_128bits(data); // [low64, high64]
long[] h3_128_seed = XXHash.xxh3_128bitsWithSeed(data, 42);
```

## Public API

| Method | Returns | Notes |
| --- | --- | --- |
| `xxh32(input, seed)` | `Long` | 32-bit hash in a Kotlin `Long` |
| `xxh32Bytes(input, offset, length, seed)` | `Long` | slice hashing |
| `xxh64(input, seed)` | `Long` | 64-bit hash |
| `xxh64Bytes(input, offset, length, seed)` | `Long` | slice hashing |
| `xxh3_64bits(input)` | `Long` | XXH3 64-bit |
| `xxh3_64bitsWithSeed(input, seed)` | `Long` | XXH3 64-bit with seed |
| `xxh3_128bits(input)` | `LongArray` | `[low64, high64]` |
| `xxh3_128bitsWithSeed(input, seed)` | `LongArray` | `[low64, high64]` |

All values are exposed as signed Kotlin `Long`. Treat them as unsigned when formatting or comparing against canonical xxHash hex output.

## Demo apps

Android demo:

```bash
./gradlew :androidApp:installDebug
```

iOS demo:

```bash
./gradlew :commonApp:embedAndSignAppleFrameworkForXcode
```

Web demo:

```bash
./gradlew :webApp:jsBrowserDevelopmentWebpack
./gradlew :webApp:wasmJsBrowserDevelopmentWebpack
```

`commonApp` contains the shared Compose UI used by Android, iOS, and Web.

## Build and test

Library compile checks:

```bash
./gradlew :lib:compileKotlinJs
./gradlew :lib:compileKotlinWasmJs
./gradlew :lib:compileTestKotlinJs
```

Selected tests:

```bash
./gradlew :lib:jvmTest
./gradlew :lib:iosSimulatorArm64Test
./gradlew :lib:jsNodeTest
./gradlew :lib:wasmJsNodeTest
```

Some web tasks depend on the local Gradle/Node toolchain and your environment setup.

## Repository structure

Useful paths:

- `lib/src/commonMain/kotlin/io/github/limuyang2/xxhash/lib`
- `lib/src/webMain/kotlin/io/github/limuyang2/xxhash/lib`
- `lib/src/iosMain/kotlin/io/github/limuyang2/xxhash/lib`
- `lib/src/androidMain/kotlin/io/github/limuyang2/xxhash/lib`
- `lib_android_native/src/main/cpp`

## License

MIT License

This repository includes [xxHash](https://github.com/Cyan4973/xxHash/) source code. xxHash itself is distributed under the BSD 2-Clause License.
