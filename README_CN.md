# XXHash Android

[![zread](https://img.shields.io/badge/Ask_Zread-_.svg?style=flat&color=00b0aa&labelColor=000000&logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPHN2ZyB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIHZpZXdCb3g9IjAgMCAxNiAxNiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTQuOTYxNTYgMS42MDAxSDIuMjQxNTZDMS44ODgxIDEuNjAwMSAxLjYwMTU2IDEuODg2NjQgMS42MDE1NiAyLjI0MDFWNC45NjAxQzEuNjAxNTYgNS4zMTM1NiAxLjg4ODEgNS42MDAxIDIuMjQxNTYgNS42MDAxSDQuOTYxNTZDNS4zMTUwMiA1LjYwMDEgNS42MDE1NiA1LjMxMzU2IDUuNjAxNTYgNC45NjAxVjIuMjQwMUM1LjYwMTU2IDEuODg2NjQgNS4zMTUwMiAxLjYwMDEgNC45NjE1NiAxLjYwMDFaIiBmaWxsPSIjZmZmIi8%2BCjxwYXRoIGQ9Ik00Ljk2MTU2IDEwLjM5OTlIMi4yNDE1NkMxLjg4ODEgMTAuMzk5OSAxLjYwMTU2IDEwLjY4NjQgMS42MDE1NiAxMS4wMzk5VjEzLjc1OTlDMS42MDE1NiAxNC4xMTM0IDEuODg4MSAxNC4zOTk5IDIuMjQxNTYgMTQuMzk5OUg0Ljk2MTU2QzUuMzE1MDIgMTQuMzk5OSA1LjYwMTU2IDE0LjExMzQgNS42MDE1NiAxMy43NTk5VjExLjAzOTlDNS42MDE1NiAxMC42ODY0IDUuMzE1MDIgMTAuMzk5OSA0Ljk2MTU2IDEwLjM5OTlaIiBmaWxsPSIjZmZmIi8%2BCjxwYXRoIGQ9Ik0xMy43NTg0IDEuNjAwMUgxMS4wMzg0QzEwLjY4NSAxLjYwMDEgMTAuMzk4NCAxLjg4NjY0IDEwLjM5ODQgMi4yNDAxVjQuOTYwMUMxMC4zOTg0IDUuMzEzNTYgMTAuNjg1IDUuNjAwMSAxMS4wMzg0IDUuNjAwMUgxMy43NTg0QzE0LjExMTkgNS42MDAxIDE0LjM5ODQgNS4zMTM1NiAxNC4zOTg0IDQuOTYwMVYyLjI0MDFDMTQuMzk4NCAxLjg4NjY0IDE0LjExMTkgMS42MDAxIDEzLjc1ODQgMS42MDAxWiIgZmlsbD0iI2ZmZiIvPgo8cGF0aCBkPSJNNCAxMkwxMiA0TDQgMTJaIiBmaWxsPSIjZmZmIi8%2BCjxwYXRoIGQ9Ik00IDEyTDEyIDQiIHN0cm9rZT0iI2ZmZiIgc3Ryb2tlLXdpZHRoPSIxLjUiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIvPgo8L3N2Zz4K&logoColor=ffffff)](https://zread.ai/limuyang2/fast-xxhash-android)

基于 [xxHash](https://github.com/Cyan4973/xxHash/) C 语言原版源码编译的 Android JNI 封装库，提供极快的哈希计算性能。

## 特性

- **原生 C 实现** — 直接编译 xxHash 的 C 源码，通过 JNI 调用，性能远超纯 Java/Kotlin 实现
- **全算法支持** — XXH32、XXH64、XXH3-64、XXH3-128
- **16KB Page Size 对齐** — 适配 Android 15+ 的 16KB 页面大小要求
- **全架构覆盖** — armeabi-v7a、arm64-v8a、x86、x86_64
- **极小编译优化** — 使用 `-O3` 编译选项，静态链接 xxHash，无运行时依赖
- **Java 友好** — 提供 `@JvmStatic` 注解，Java 中可直接以 `XXHash.xxh64(...)` 方式调用
- **Kotlin 友好** — 提供 `String`、`ByteArray` 扩展函数，使用更简洁

## 性能

xxHash 是目前最快的非加密哈希算法之一。以下为官方 C 实现与常见替代方案的对比：

| 算法       | 速度 (GB/s) | 哈希位数 |
|----------|-----------|------|
| XXH3-64  | 31.5      | 64   |
| XXH3-128 | 28.2      | 128  |
| XXH64    | 19.3      | 64   |
| XXH32    | 9.8       | 32   |
| MD5      | 0.65      | 128  |
| SHA-256  | 0.42      | 256  |

> 数据来源：[xxHash Benchmark](https://github.com/Cyan4973/xxHash/#benchmark)

本库直接编译自 C 原版源码，性能与 C 原生版本一致，无额外开销。

## 环境要求

- minSdk: 21 (Android 5.0)
- compileSdk: 36

## 引入

在模块的 `build.gradle.kts` 中添加依赖：

[![Maven Central](https://img.shields.io/maven-central/v/io.github.limuyang2/xxhash.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.limuyang2/xxhash)
```kotlin
dependencies {
    implementation("io.github.limuyang2:xxhash:<latest-version>")
}
```

> 查看最新版本：[Maven Central](https://central.sonatype.com/artifact/io.github.limuyang2/xxhash)

## 使用说明

### Kotlin 用法

本库提供 `String`、`ByteArray`、`ByteBuffer` 扩展函数，无需手动调用 `XXHash` 对象：

```kotlin
// String 扩展
val h32 = "Hello, World!".xxh32()
val h64 = "Hello, World!".xxh64()
val h3 = "Hello, World!".xxh3As64()
val h128 = "Hello, World!".xxh3As128()  // LongArray[2]

// 带 seed
val h32s = "Hello, World!".xxh32(seed = 42)
val h64s = "Hello, World!".xxh64(seed = 42)
val h3s = "Hello, World!".xxh3As64(seed = 42)
val h128s = "Hello, World!".xxh3As128(seed = 42)

// ByteArray 扩展
val data = "Hello, World!".toByteArray()
data.xxh64()
data.xxh3As128()

// ByteArray 切片 — 避免数组拷贝
data.xxh32(offset = 7, length = 6)  // 计算 data[7..13) 的哈希

// ByteBuffer 扩展（不改变 buffer 的 position）
val buffer = ByteBuffer.wrap(data)
buffer.xxh64()
```

### Java 用法

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

// XXH3 128-bit — 返回 long[2]，[0]=low64，[1]=high64
long[] h128 = XXHash.xxh3_128bits(data);

// XXH3 128-bit with seed
long[] h128s = XXHash.xxh3_128bitsWithSeed(data, 42);

// 数组切片
byte[] full = "Hello, World!".getBytes();
long hash = XXHash.xxh32Bytes(full, 7, 6, 0); // full[7..13) = "World!"
```

### 完整 API

| 方法                                        | 参数                          | 返回值            | 说明                  |
|-------------------------------------------|-----------------------------|----------------|---------------------|
| `xxh32(input, seed)`                      | `ByteArray, Int`            | `Long`         | XXH32 哈希            |
| `xxh32Bytes(input, offset, length, seed)` | `ByteArray, Int, Int, Int`  | `Long`         | XXH32 哈希（切片）        |
| `xxh64(input, seed)`                      | `ByteArray, Long`           | `Long`         | XXH64 哈希            |
| `xxh64Bytes(input, offset, length, seed)` | `ByteArray, Int, Int, Long` | `Long`         | XXH64 哈希（切片）        |
| `xxh3_64bits(input)`                      | `ByteArray`                 | `Long`         | XXH3-64 哈希          |
| `xxh3_64bitsWithSeed(input, seed)`        | `ByteArray, Long`           | `Long`         | XXH3-64 哈希（带 seed）  |
| `xxh3_128bits(input)`                     | `ByteArray`                 | `LongArray[2]` | XXH3-128 哈希         |
| `xxh3_128bitsWithSeed(input, seed)`       | `ByteArray, Long`           | `LongArray[2]` | XXH3-128 哈希（带 seed） |

> **注意**：所有返回值均为 Kotlin `Long`（有符号 64 位）。xxhash 产生的是无符号哈希值，高位为 1 时 `Long.toHexString()` 输出的是无符号十六进制表示。


## 许可证

MIT License

本库包含 [xxHash](https://github.com/Cyan4973/xxHash/) 源码，xxHash 采用 BSD 2-Clause 许可证。
