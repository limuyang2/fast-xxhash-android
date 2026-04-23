@file:Suppress("unused")

package io.github.limuyang2.xxhash.lib

import java.nio.ByteBuffer

// -------- ByteArray extensions --------

/**
 * XXH32 hash of the entire array.
 */
fun ByteArray.xxh32(seed: Int = 0): Long = XXHash.xxh32(this, seed)

/**
 * XXH32 hash of a slice of the array.
 */
fun ByteArray.xxh32(offset: Int, length: Int, seed: Int = 0): Long =
    XXHash.xxh32Bytes(this, offset, length, seed)

/**
 * XXH64 hash of the entire array.
 */
fun ByteArray.xxh64(seed: Long = 0): Long = XXHash.xxh64(this, seed)

/**
 * XXH64 hash of a slice of the array.
 */
fun ByteArray.xxh64(offset: Int, length: Int, seed: Long = 0): Long =
    XXHash.xxh64Bytes(this, offset, length, seed)

/**
 * XXH3 64-bit hash.
 */
fun ByteArray.xxh3As64(seed: Long = 0): Long =
    if (seed == 0L) XXHash.xxh3_64bits(this) else XXHash.xxh3_64bitsWithSeed(this, seed)

/**
 * XXH3 128-bit hash.
 */
fun ByteArray.xxh3As128(seed: Long = 0): LongArray =
    if (seed == 0L) XXHash.xxh3_128bits(this) else XXHash.xxh3_128bitsWithSeed(this, seed)

// -------- String extensions --------

/**
 * XXH32 hash of this string (UTF-8 bytes).
 */
fun String.xxh32(seed: Int = 0): Long = toByteArray().xxh32(seed)

/**
 * XXH64 hash of this string (UTF-8 bytes).
 */
fun String.xxh64(seed: Long = 0): Long = toByteArray().xxh64(seed)

/**
 * XXH3 64-bit hash of this string (UTF-8 bytes).
 */
fun String.xxh3As64(seed: Long = 0): Long = toByteArray().xxh3As64(seed)

/**
 * XXH3 128-bit hash of this string (UTF-8 bytes).
 */
fun String.xxh3As128(seed: Long = 0): LongArray = toByteArray().xxh3As128(seed)

// -------- ByteBuffer extensions --------


/**
 * XXH32 hash of the ByteBuffer's remaining bytes.
 * Buffer position is not modified.
 */
fun ByteBuffer.xxh32(seed: Int = 0): Long {
    val arr = ByteArray(remaining())
    val pos = position()
    get(arr)
    position(pos)
    return arr.xxh32(seed)
}

/**
 * XXH64 hash of the ByteBuffer's remaining bytes.
 * Buffer position is not modified.
 */
fun ByteBuffer.xxh64(seed: Long = 0): Long {
    val arr = ByteArray(remaining())
    val pos = position()
    get(arr)
    position(pos)
    return arr.xxh64(seed)
}

/**
 * XXH3 64-bit hash of the ByteBuffer's remaining bytes.
 * Buffer position is not modified.
 */
fun ByteBuffer.xxh3As64(seed: Long = 0): Long {
    val arr = ByteArray(remaining())
    val pos = position()
    get(arr)
    position(pos)
    return arr.xxh3As64(seed)
}

/**
 * XXH3 128-bit hash of the ByteBuffer's remaining bytes.
 * Buffer position is not modified.
 */
fun ByteBuffer.xxh3As128(seed: Long = 0): LongArray {
    val arr = ByteArray(remaining())
    val pos = position()
    get(arr)
    position(pos)
    return arr.xxh3As128(seed)
}
