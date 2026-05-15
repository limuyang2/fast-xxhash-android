package io.github.limuyang2.xxhash.lib

internal object XXHashWebPure {
    private val PRIME32_1 = 0x9E3779B1u.toInt()
    private val PRIME32_2 = 0x85EBCA77u.toInt()
    private val PRIME32_3 = 0xC2B2AE3Du.toInt()
    private val PRIME32_4 = 0x27D4EB2Fu.toInt()
    private val PRIME32_5 = 0x165667B1u.toInt()

    private val PRIME64_1 = 0x9E3779B185EBCA87uL.toLong()
    private val PRIME64_2 = 0xC2B2AE3D27D4EB4FuL.toLong()
    private val PRIME64_3 = 0x165667B19E3779F9uL.toLong()
    private val PRIME64_4 = 0x85EBCA77C2B2AE63uL.toLong()
    private val PRIME64_5 = 0x27D4EB2F165667C5uL.toLong()

    fun xxh32(input: ByteArray, offset: Int, length: Int, seed: Int): Long {
        var index = offset
        val end = offset + length
        var hash: Int

        if (length >= 16) {
            val limit = end - 15
            var v1 = seed + PRIME32_1 + PRIME32_2
            var v2 = seed + PRIME32_2
            var v3 = seed
            var v4 = seed - PRIME32_1

            while (index < limit) {
                v1 = round32(v1, read32LE(input, index))
                index += 4
                v2 = round32(v2, read32LE(input, index))
                index += 4
                v3 = round32(v3, read32LE(input, index))
                index += 4
                v4 = round32(v4, read32LE(input, index))
                index += 4
            }

            hash = rotateLeft(v1, 1) +
                rotateLeft(v2, 7) +
                rotateLeft(v3, 12) +
                rotateLeft(v4, 18)
        } else {
            hash = seed + PRIME32_5
        }

        hash += length

        while (index <= end - 4) {
            hash += read32LE(input, index) * PRIME32_3
            hash = rotateLeft(hash, 17) * PRIME32_4
            index += 4
        }

        while (index < end) {
            hash += input[index].unsignedByte() * PRIME32_5
            hash = rotateLeft(hash, 11) * PRIME32_1
            index++
        }

        return avalanche32(hash).toLong() and 0xFFFF_FFFFL
    }

    fun xxh64(input: ByteArray, offset: Int, length: Int, seed: Long): Long {
        var index = offset
        val end = offset + length
        var hash: Long

        if (length >= 32) {
            val limit = end - 31
            var v1 = seed + PRIME64_1 + PRIME64_2
            var v2 = seed + PRIME64_2
            var v3 = seed
            var v4 = seed - PRIME64_1

            while (index < limit) {
                v1 = round64(v1, read64LE(input, index))
                index += 8
                v2 = round64(v2, read64LE(input, index))
                index += 8
                v3 = round64(v3, read64LE(input, index))
                index += 8
                v4 = round64(v4, read64LE(input, index))
                index += 8
            }

            hash = rotateLeft(v1, 1) +
                rotateLeft(v2, 7) +
                rotateLeft(v3, 12) +
                rotateLeft(v4, 18)

            hash = mergeRound64(hash, v1)
            hash = mergeRound64(hash, v2)
            hash = mergeRound64(hash, v3)
            hash = mergeRound64(hash, v4)
        } else {
            hash = seed + PRIME64_5
        }

        hash += length.toLong()

        while (index <= end - 8) {
            val k1 = round64(0L, read64LE(input, index))
            hash = hash xor k1
            hash = rotateLeft(hash, 27) * PRIME64_1 + PRIME64_4
            index += 8
        }

        if (index <= end - 4) {
            hash = hash xor ((read32LE(input, index).toLong() and 0xFFFF_FFFFL) * PRIME64_1)
            hash = rotateLeft(hash, 23) * PRIME64_2 + PRIME64_3
            index += 4
        }

        while (index < end) {
            hash = hash xor (input[index].unsignedByte().toLong() * PRIME64_5)
            hash = rotateLeft(hash, 11) * PRIME64_1
            index++
        }

        return avalanche64(hash)
    }

    private fun round32(accumulator: Int, input: Int): Int {
        var acc = accumulator + input * PRIME32_2
        acc = rotateLeft(acc, 13)
        acc *= PRIME32_1
        return acc
    }

    private fun avalanche32(value: Int): Int {
        var hash = value
        hash = hash xor (hash ushr 15)
        hash *= PRIME32_2
        hash = hash xor (hash ushr 13)
        hash *= PRIME32_3
        hash = hash xor (hash ushr 16)
        return hash
    }

    private fun round64(accumulator: Long, input: Long): Long {
        var acc = accumulator + input * PRIME64_2
        acc = rotateLeft(acc, 31)
        acc *= PRIME64_1
        return acc
    }

    private fun mergeRound64(accumulator: Long, value: Long): Long {
        var acc = accumulator
        acc = acc xor round64(0L, value)
        acc = acc * PRIME64_1 + PRIME64_4
        return acc
    }

    private fun avalanche64(value: Long): Long {
        var hash = value
        hash = hash xor (hash ushr 33)
        hash *= PRIME64_2
        hash = hash xor (hash ushr 29)
        hash *= PRIME64_3
        hash = hash xor (hash ushr 32)
        return hash
    }

    private fun read32LE(input: ByteArray, index: Int): Int =
        input[index].unsignedByte() or
            (input[index + 1].unsignedByte() shl 8) or
            (input[index + 2].unsignedByte() shl 16) or
            (input[index + 3].unsignedByte() shl 24)

    private fun read64LE(input: ByteArray, index: Int): Long =
        (input[index].unsignedByte().toLong()) or
            (input[index + 1].unsignedByte().toLong() shl 8) or
            (input[index + 2].unsignedByte().toLong() shl 16) or
            (input[index + 3].unsignedByte().toLong() shl 24) or
            (input[index + 4].unsignedByte().toLong() shl 32) or
            (input[index + 5].unsignedByte().toLong() shl 40) or
            (input[index + 6].unsignedByte().toLong() shl 48) or
            (input[index + 7].unsignedByte().toLong() shl 56)

    private fun rotateLeft(value: Int, bitCount: Int): Int =
        (value shl bitCount) or (value ushr (32 - bitCount))

    private fun rotateLeft(value: Long, bitCount: Int): Long =
        (value shl bitCount) or (value ushr (64 - bitCount))

    private fun Byte.unsignedByte(): Int = toInt() and 0xFF
}
