package io.github.limuyang2.xxhash.lib

import net.jpountz.xxhash.XXHashFactory
import net.openhft.hashing.LongHashFunction
import net.openhft.hashing.LongTupleHashFunction

private val xxHashFactory = XXHashFactory.fastestJavaInstance()
private val xxh32 = xxHashFactory.hash32()
private val xxh64 = xxHashFactory.hash64()

actual object XXHash {
    actual fun xxh32(input: ByteArray, seed: Int): Long =
        xxh32.hash(input, 0, input.size, seed).toLong() and 0xffffffffL

    actual fun xxh32Bytes(input: ByteArray, offset: Int, length: Int, seed: Int): Long {
        validateSlice(input, offset, length)
        return xxh32.hash(input, offset, length, seed).toLong() and 0xffffffffL
    }

    actual fun xxh64(input: ByteArray, seed: Long): Long =
        xxh64.hash(input, 0, input.size, seed)

    actual fun xxh64Bytes(input: ByteArray, offset: Int, length: Int, seed: Long): Long {
        validateSlice(input, offset, length)
        return xxh64.hash(input, offset, length, seed)
    }

    actual fun xxh3_64bits(input: ByteArray): Long =
        LongHashFunction.xx3().hashBytes(input)

    actual fun xxh3_64bitsWithSeed(input: ByteArray, seed: Long): Long =
        LongHashFunction.xx3(seed).hashBytes(input)

    actual fun xxh3_128bits(input: ByteArray): LongArray =
        LongTupleHashFunction.xx128().hashBytes(input).copyOf(2)

    actual fun xxh3_128bitsWithSeed(input: ByteArray, seed: Long): LongArray =
        LongTupleHashFunction.xx128(seed).hashBytes(input).copyOf(2)


    private fun validateSlice(input: ByteArray, offset: Int, length: Int) {
        require(offset >= 0) { "offset must be >= 0" }
        require(length >= 0) { "length must be >= 0" }
        require(offset <= input.size) { "offset must be <= input size" }
        require(offset + length <= input.size) { "offset + length must be <= input size" }
    }
}


