package io.github.limuyang2.xxhash.lib

actual object XXHash {
    actual fun xxh32(input: ByteArray, seed: Int): Long =
        XXHashWebPure.xxh32(input, 0, input.size, seed)

    actual fun xxh32Bytes(input: ByteArray, offset: Int, length: Int, seed: Int): Long {
        validateSlice(input, offset, length)
        return XXHashWebPure.xxh32(input, offset, length, seed)
    }

    actual fun xxh64(input: ByteArray, seed: Long): Long =
        XXHashWebPure.xxh64(input, 0, input.size, seed)

    actual fun xxh64Bytes(input: ByteArray, offset: Int, length: Int, seed: Long): Long {
        validateSlice(input, offset, length)
        return XXHashWebPure.xxh64(input, offset, length, seed)
    }

    actual fun xxh3_64bits(input: ByteArray): Long =
        XXH3WebPure.xxh3_64bits(input)

    actual fun xxh3_64bitsWithSeed(input: ByteArray, seed: Long): Long =
        XXH3WebPure.xxh3_64bitsWithSeed(input, seed)

    actual fun xxh3_128bits(input: ByteArray): LongArray =
        XXH3WebPure.xxh3_128bits(input)

    actual fun xxh3_128bitsWithSeed(input: ByteArray, seed: Long): LongArray =
        XXH3WebPure.xxh3_128bitsWithSeed(input, seed)
}

private fun validateSlice(input: ByteArray, offset: Int, length: Int) {
    require(offset >= 0) { "offset must be >= 0" }
    require(length >= 0) { "length must be >= 0" }
    require(offset <= input.size) { "offset must be <= input size" }
    require(length <= input.size - offset) { "offset + length must be <= input size" }
}
