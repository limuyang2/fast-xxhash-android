package io.github.limuyang2.xxhash.lib

import io.github.limuyang2.xxhash.lib.internal.XXHashNative

actual object XXHash {
    @JvmStatic
    actual fun xxh32(input: ByteArray, seed: Int): Long = XXHashNative.xxh32(input, seed)

    @JvmStatic
    actual fun xxh32Bytes(input: ByteArray, offset: Int, length: Int, seed: Int): Long =
        XXHashNative.xxh32Bytes(input, offset, length, seed)

    @JvmStatic
    actual fun xxh64(input: ByteArray, seed: Long): Long = XXHashNative.xxh64(input, seed)

    @JvmStatic
    actual fun xxh64Bytes(input: ByteArray, offset: Int, length: Int, seed: Long): Long =
        XXHashNative.xxh64Bytes(input, offset, length, seed)

    @JvmStatic
    actual fun xxh3_64bits(input: ByteArray): Long = XXHashNative.xxh3_64bits(input)

    @JvmStatic
    actual fun xxh3_64bitsWithSeed(input: ByteArray, seed: Long): Long =
        XXHashNative.xxh3_64bitsWithSeed(input, seed)

    @JvmStatic
    actual fun xxh3_128bits(input: ByteArray): LongArray = XXHashNative.xxh3_128bits(input)

    @JvmStatic
    actual fun xxh3_128bitsWithSeed(input: ByteArray, seed: Long): LongArray =
        XXHashNative.xxh3_128bitsWithSeed(input, seed)
}
