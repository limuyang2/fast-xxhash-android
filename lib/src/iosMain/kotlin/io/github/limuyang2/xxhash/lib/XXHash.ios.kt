@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package io.github.limuyang2.xxhash.lib

import io.github.limuyang2.xxhash.lib.cinterop.XXH32
import io.github.limuyang2.xxhash.lib.cinterop.XXH3_128bits
import io.github.limuyang2.xxhash.lib.cinterop.XXH3_128bits_withSeed
import io.github.limuyang2.xxhash.lib.cinterop.XXH3_64bits
import io.github.limuyang2.xxhash.lib.cinterop.XXH3_64bits_withSeed
import io.github.limuyang2.xxhash.lib.cinterop.XXH64
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned

actual object XXHash {
    actual fun xxh32(input: ByteArray, seed: Int): Long =
        input.useSlicePointer(0, input.size) { data, length ->
            XXH32(data, length, seed.toUInt()).toLong() and 0xffffffffL
        }

    actual fun xxh32Bytes(input: ByteArray, offset: Int, length: Int, seed: Int): Long =
        input.useSlicePointer(offset, length) { data, size ->
            XXH32(data, size, seed.toUInt()).toLong() and 0xffffffffL
        }

    actual fun xxh64(input: ByteArray, seed: Long): Long =
        input.useSlicePointer(0, input.size) { data, length ->
            XXH64(data, length, seed.toULong()).toLong()
        }

    actual fun xxh64Bytes(input: ByteArray, offset: Int, length: Int, seed: Long): Long =
        input.useSlicePointer(offset, length) { data, size ->
            XXH64(data, size, seed.toULong()).toLong()
        }

    actual fun xxh3_64bits(input: ByteArray): Long =
        input.useSlicePointer(0, input.size) { data, length ->
            XXH3_64bits(data, length).toLong()
        }

    actual fun xxh3_64bitsWithSeed(input: ByteArray, seed: Long): Long =
        input.useSlicePointer(0, input.size) { data, length ->
            XXH3_64bits_withSeed(data, length, seed.toULong()).toLong()
        }

    actual fun xxh3_128bits(input: ByteArray): LongArray =
        input.useSlicePointer(0, input.size) { data, length ->
            XXH3_128bits(data, length).useContents {
                longArrayOf(low64.toLong(), high64.toLong())
            }
        }

    actual fun xxh3_128bitsWithSeed(input: ByteArray, seed: Long): LongArray =
        input.useSlicePointer(0, input.size) { data, length ->
            XXH3_128bits_withSeed(data, length, seed.toULong()).useContents {
                longArrayOf(low64.toLong(), high64.toLong())
            }
        }
}

private inline fun <R> ByteArray.useSlicePointer(
    offset: Int,
    length: Int,
    block: (CPointer<ByteVar>?, ULong) -> R,
): R {
    require(offset >= 0) { "offset must be >= 0" }
    require(length >= 0) { "length must be >= 0" }
    require(offset <= size) { "offset must be <= input size" }
    require(offset + length <= size) { "offset + length must be <= input size" }

    if (length == 0) {
        return block(null, 0u)
    }

    return usePinned { pinned ->
        block(pinned.addressOf(offset), length.convert())
    }
}
