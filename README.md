# XXHash Android

[![Maven Central](https://img.shields.io/maven-central/v/io.github.limuyang2/xxhash.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.limuyang2/xxhash)
[![中文文档](https://img.shields.io/badge/%E6%96%87%E6%A1%A3-%E4%B8%AD%E6%96%87-blue)](https://github.com/limuyang2/fast-xxhash-android/blob/main/README_CN.md)

An Android JNI wrapper for [xxHash](https://github.com/Cyan4973/xxHash/), compiled directly from the original C source code, delivering extremely fast hash computation.

**[中文文档](https://github.com/limuyang2/fast-xxhash-android/blob/main/README_CN.md)**

## Features

- **Native C Implementation** — Compiled directly from xxHash C source via JNI, far outperforming pure Java/Kotlin implementations
- **Full Algorithm Support** — XXH32, XXH64, XXH3-64, XXH3-128
- **16KB Page Size Aligned** — Compatible with Android 15+ 16KB page size requirements
- **Multi-Architecture** — armeabi-v7a, arm64-v8a, x86, x86_64
- **Optimized Compilation** — `-O3` optimization, statically linked xxHash, no runtime dependencies
- **Java Friendly** — `@JvmStatic` annotations, callable as `XXHash.xxh64(...)` from Java
- **Kotlin Friendly** — Extension functions for `String`, `ByteArray`, and `ByteBuffer`

## Performance

xxHash is one of the fastest non-cryptographic hash algorithms available:

| Algorithm | Speed (GB/s) | Hash Bits |
|-----------|-------------|-----------|
| XXH3-64   | 31.5        | 64        |
| XXH3-128  | 28.2        | 128       |
| XXH64     | 19.3        | 64        |
| XXH32     | 9.8         | 32        |
| MD5       | 0.65        | 128       |
| SHA-256   | 0.42        | 256       |

> Source: [xxHash Benchmark](https://github.com/Cyan4973/xxHash/#benchmark)

This library compiles directly from the original C source, delivering performance identical to the native C version with zero overhead.

## Requirements

- minSdk: 21 (Android 5.0)
- compileSdk: 36

## Installation

Add the dependency in your module's `build.gradle.kts`:

[![Maven Central](https://img.shields.io/maven-central/v/io.github.limuyang2/xxhash.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.limuyang2/xxhash)

```kotlin
dependencies {
    implementation("io.github.limuyang2:xxhash:<latest-version>")
}
```

> Check the latest version on [Maven Central](https://central.sonatype.com/artifact/io.github.limuyang2/xxhash)

## Usage

### Kotlin

This library provides extension functions for `String`, `ByteArray`, and `ByteBuffer`:

```kotlin
// String extensions
val h32 = "Hello, World!".xxh32()
val h64 = "Hello, World!".xxh64()
val h3 = "Hello, World!".xxh3As64()
val h128 = "Hello, World!".xxh3As128()  // LongArray[2]

// With seed
val h32s = "Hello, World!".xxh32(seed = 42)
val h64s = "Hello, World!".xxh64(seed = 42)
val h3s = "Hello, World!".xxh3As64(seed = 42)
val h128s = "Hello, World!".xxh3As128(seed = 42)

// ByteArray extensions
val data = "Hello, World!".toByteArray()
data.xxh64()
data.xxh3As128()

// ByteArray slice — no array copy needed
data.xxh32(offset = 7, length = 6)  // hash of data[7..13)

// ByteBuffer extensions (does not modify buffer position)
val buffer = ByteBuffer.wrap(data)
buffer.xxh64()
```

### Java

```java
byte[] data = "Hello, World!".getBytes();

// XXH32
long h32 = XXHash.xxh32(data, 0);

// XXH64
long h64 = XXHash.xxh64(data, 0);

// XXH3 64-bit
long h3_64 = XXHash.xxh3_64bits(data);

// XXH3 64-bit with seed
long h3_64s = XXHash.xxh3_64bitsWithSeed(data, 42);

// XXH3 128-bit — returns long[2], [0]=low64, [1]=high64
long[] h128 = XXHash.xxh3_128bits(data);

// XXH3 128-bit with seed
long[] h128s = XXHash.xxh3_128bitsWithSeed(data, 42);

// Array slice
byte[] full = "Hello, World!".getBytes();
long hash = XXHash.xxh32Bytes(full, 7, 6, 0); // full[7..13) = "World!"
```

### API Reference

| Method | Parameters | Returns | Description |
|--------|-----------|---------|-------------|
| `xxh32(input, seed)` | `ByteArray, Int` | `Long` | XXH32 hash |
| `xxh32Bytes(input, offset, length, seed)` | `ByteArray, Int, Int, Int` | `Long` | XXH32 hash (slice) |
| `xxh64(input, seed)` | `ByteArray, Long` | `Long` | XXH64 hash |
| `xxh64Bytes(input, offset, length, seed)` | `ByteArray, Int, Int, Long` | `Long` | XXH64 hash (slice) |
| `xxh3_64bits(input)` | `ByteArray` | `Long` | XXH3-64 hash |
| `xxh3_64bitsWithSeed(input, seed)` | `ByteArray, Long` | `Long` | XXH3-64 hash (with seed) |
| `xxh3_128bits(input)` | `ByteArray` | `LongArray[2]` | XXH3-128 hash |
| `xxh3_128bitsWithSeed(input, seed)` | `ByteArray, Long` | `LongArray[2]` | XXH3-128 hash (with seed) |

> **Note**: All return values are Kotlin `Long` (signed 64-bit). xxHash produces unsigned hash values. When the high bit is set, `Long.toHexString()` outputs the unsigned hexadecimal representation.


## License

MIT License

This library includes [xxHash](https://github.com/Cyan4973/xxHash/) source code, which is licensed under the BSD 2-Clause License.
