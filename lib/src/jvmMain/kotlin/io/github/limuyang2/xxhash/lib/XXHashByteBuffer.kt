@file:Suppress("unused")

package io.github.limuyang2.xxhash.lib

import java.nio.ByteBuffer

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

