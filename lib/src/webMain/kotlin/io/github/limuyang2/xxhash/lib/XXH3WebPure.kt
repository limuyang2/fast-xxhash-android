package io.github.limuyang2.xxhash.lib

internal object XXH3WebPure {
    private const val STRIPE_LEN = 64
    private const val SECRET_CONSUME_RATE = 8
    private const val ACC_NB = STRIPE_LEN / 8
    private const val MIDSIZE_MAX = 240
    private const val MIDSIZE_STARTOFFSET = 3
    private const val MIDSIZE_LASTOFFSET = 17
    private const val SECRET_LASTACC_START = 7
    private const val SECRET_MERGEACCS_START = 11

    private val PRIME32_1 = 0x9E3779B1u.toLong()
    private val PRIME32_2 = 0x85EBCA77u.toLong()
    private val PRIME32_3 = 0xC2B2AE3Du.toLong()

    private val PRIME64_1 = 0x9E3779B185EBCA87uL.toLong()
    private val PRIME64_2 = 0xC2B2AE3D27D4EB4FuL.toLong()
    private val PRIME64_3 = 0x165667B19E3779F9uL.toLong()
    private val PRIME64_4 = 0x85EBCA77C2B2AE63uL.toLong()
    private val PRIME64_5 = 0x27D4EB2F165667C5uL.toLong()
    private val PRIME_MX1 = 0x165667919E3779F9uL.toLong()
    private val PRIME_MX2 = 0x9FB21C651E98DF25uL.toLong()

    private val SECRET = intArrayOf(
        0xb8, 0xfe, 0x6c, 0x39, 0x23, 0xa4, 0x4b, 0xbe, 0x7c, 0x01, 0x81, 0x2c, 0xf7, 0x21, 0xad, 0x1c,
        0xde, 0xd4, 0x6d, 0xe9, 0x83, 0x90, 0x97, 0xdb, 0x72, 0x40, 0xa4, 0xa4, 0xb7, 0xb3, 0x67, 0x1f,
        0xcb, 0x79, 0xe6, 0x4e, 0xcc, 0xc0, 0xe5, 0x78, 0x82, 0x5a, 0xd0, 0x7d, 0xcc, 0xff, 0x72, 0x21,
        0xb8, 0x08, 0x46, 0x74, 0xf7, 0x43, 0x24, 0x8e, 0xe0, 0x35, 0x90, 0xe6, 0x81, 0x3a, 0x26, 0x4c,
        0x3c, 0x28, 0x52, 0xbb, 0x91, 0xc3, 0x00, 0xcb, 0x88, 0xd0, 0x65, 0x8b, 0x1b, 0x53, 0x2e, 0xa3,
        0x71, 0x64, 0x48, 0x97, 0xa2, 0x0d, 0xf9, 0x4e, 0x38, 0x19, 0xef, 0x46, 0xa9, 0xde, 0xac, 0xd8,
        0xa8, 0xfa, 0x76, 0x3f, 0xe3, 0x9c, 0x34, 0x3f, 0xf9, 0xdc, 0xbb, 0xc7, 0xc7, 0x0b, 0x4f, 0x1d,
        0x8a, 0x51, 0xe0, 0x4b, 0xcd, 0xb4, 0x59, 0x31, 0xc8, 0x9f, 0x7e, 0xc9, 0xd9, 0x78, 0x73, 0x64,
        0xea, 0xc5, 0xac, 0x83, 0x34, 0xd3, 0xeb, 0xc3, 0xc5, 0x81, 0xa0, 0xff, 0xfa, 0x13, 0x63, 0xeb,
        0x17, 0x0d, 0xdd, 0x51, 0xb7, 0xf0, 0xda, 0x49, 0xd3, 0x16, 0x55, 0x26, 0x29, 0xd4, 0x68, 0x9e,
        0x2b, 0x16, 0xbe, 0x58, 0x7d, 0x47, 0xa1, 0xfc, 0x8f, 0xf8, 0xb8, 0xd1, 0x7a, 0xd0, 0x31, 0xce,
        0x45, 0xcb, 0x3a, 0x8f, 0x95, 0x16, 0x04, 0x28, 0xaf, 0xd7, 0xfb, 0xca, 0xbb, 0x4b, 0x40, 0x7e,
    ).map { it.toByte() }.toByteArray()

    fun xxh3_64bits(input: ByteArray): Long =
        xxh3_64bitsInternal(input, 0L, SECRET)

    fun xxh3_64bitsWithSeed(input: ByteArray, seed: Long): Long =
        xxh3_64bitsInternal(input, seed, SECRET)

    fun xxh3_128bits(input: ByteArray): LongArray =
        xxh3_128bitsInternal(input, 0L, SECRET).toLongArray()

    fun xxh3_128bitsWithSeed(input: ByteArray, seed: Long): LongArray =
        xxh3_128bitsInternal(input, seed, SECRET).toLongArray()

    private fun xxh3_64bitsInternal(input: ByteArray, seed: Long, secret: ByteArray): Long {
        val len = input.size
        return when {
            len <= 16 -> len0to16_64(input, len, secret, seed)
            len <= 128 -> len17to128_64(input, len, secret, seed)
            len <= MIDSIZE_MAX -> len129to240_64(input, len, secret, seed)
            seed == 0L -> hashLong64(input, secret)
            else -> hashLong64(input, initCustomSecret(seed))
        }
    }

    private fun xxh3_128bitsInternal(input: ByteArray, seed: Long, secret: ByteArray): Hash128 {
        val len = input.size
        return when {
            len <= 16 -> len0to16_128(input, len, secret, seed)
            len <= 128 -> len17to128_128(input, len, secret, seed)
            len <= MIDSIZE_MAX -> len129to240_128(input, len, secret, seed)
            seed == 0L -> hashLong128(input, secret)
            else -> hashLong128(input, initCustomSecret(seed))
        }
    }

    private fun len0to16_64(input: ByteArray, len: Int, secret: ByteArray, seed: Long): Long =
        when {
            len > 8 -> len9to16_64(input, len, secret, seed)
            len >= 4 -> len4to8_64(input, len, secret, seed)
            len > 0 -> len1to3_64(input, len, secret, seed)
            else -> avalanche64(seed xor (read64LE(secret, 56) xor read64LE(secret, 64)))
        }

    private fun len1to3_64(input: ByteArray, len: Int, secret: ByteArray, seed: Long): Long {
        val c1 = input[0].unsignedByte()
        val c2 = input[len shr 1].unsignedByte()
        val c3 = input[len - 1].unsignedByte()
        val combined = (c1 shl 16) or (c2 shl 24) or c3 or (len shl 8)
        val bitflip = ((read32LE(secret, 0).toLong() and 0xFFFF_FFFFL) xor
            (read32LE(secret, 4).toLong() and 0xFFFF_FFFFL)) + seed
        return xxh64Avalanche((combined.toLong() and 0xFFFF_FFFFL) xor bitflip)
    }

    private fun len4to8_64(input: ByteArray, len: Int, secret: ByteArray, seed: Long): Long {
        val keyedSeed = seed xor ((swap32(seed.toInt()).toLong() and 0xFFFF_FFFFL) shl 32)
        val input1 = read32LE(input, 0).toLong() and 0xFFFF_FFFFL
        val input2 = read32LE(input, len - 4).toLong() and 0xFFFF_FFFFL
        val bitflip = (read64LE(secret, 8) xor read64LE(secret, 16)) - keyedSeed
        val input64 = input2 + (input1 shl 32)
        return rrmxmx(input64 xor bitflip, len.toLong())
    }

    private fun len9to16_64(input: ByteArray, len: Int, secret: ByteArray, seed: Long): Long {
        val bitflip1 = (read64LE(secret, 24) xor read64LE(secret, 32)) + seed
        val bitflip2 = (read64LE(secret, 40) xor read64LE(secret, 48)) - seed
        val inputLo = read64LE(input, 0) xor bitflip1
        val inputHi = read64LE(input, len - 8) xor bitflip2
        val acc = len.toLong() + swap64(inputLo) + inputHi + mul128Fold64(inputLo, inputHi)
        return avalanche64(acc)
    }

    private fun len17to128_64(input: ByteArray, len: Int, secret: ByteArray, seed: Long): Long {
        var acc = len.toLong() * PRIME64_1
        if (len > 32) {
            if (len > 64) {
                if (len > 96) {
                    acc += mix16B(input, 48, secret, 96, seed)
                    acc += mix16B(input, len - 64, secret, 112, seed)
                }
                acc += mix16B(input, 32, secret, 64, seed)
                acc += mix16B(input, len - 48, secret, 80, seed)
            }
            acc += mix16B(input, 16, secret, 32, seed)
            acc += mix16B(input, len - 32, secret, 48, seed)
        }
        acc += mix16B(input, 0, secret, 0, seed)
        acc += mix16B(input, len - 16, secret, 16, seed)
        return avalanche64(acc)
    }

    private fun len129to240_64(input: ByteArray, len: Int, secret: ByteArray, seed: Long): Long {
        val nbRounds = len / 16
        var acc = len.toLong() * PRIME64_1
        for (i in 0 until 8) {
            acc += mix16B(input, 16 * i, secret, 16 * i, seed)
        }
        var accEnd = mix16B(input, len - 16, secret, 136 - MIDSIZE_LASTOFFSET, seed)
        acc = avalanche64(acc)
        for (i in 8 until nbRounds) {
            accEnd += mix16B(input, 16 * i, secret, 16 * (i - 8) + MIDSIZE_STARTOFFSET, seed)
        }
        return avalanche64(acc + accEnd)
    }

    private fun len0to16_128(input: ByteArray, len: Int, secret: ByteArray, seed: Long): Hash128 =
        when {
            len > 8 -> len9to16_128(input, len, secret, seed)
            len >= 4 -> len4to8_128(input, len, secret, seed)
            len > 0 -> len1to3_128(input, len, secret, seed)
            else -> Hash128(
                low64 = xxh64Avalanche(seed xor (read64LE(secret, 64) xor read64LE(secret, 72))),
                high64 = xxh64Avalanche(seed xor (read64LE(secret, 80) xor read64LE(secret, 88))),
            )
        }

    private fun len1to3_128(input: ByteArray, len: Int, secret: ByteArray, seed: Long): Hash128 {
        val c1 = input[0].unsignedByte()
        val c2 = input[len shr 1].unsignedByte()
        val c3 = input[len - 1].unsignedByte()
        val combinedLow = (c1 shl 16) or (c2 shl 24) or c3 or (len shl 8)
        val combinedHigh = rotateLeft(swap32(combinedLow), 13)
        val bitflipLow = ((read32LE(secret, 0).toLong() and 0xFFFF_FFFFL) xor
            (read32LE(secret, 4).toLong() and 0xFFFF_FFFFL)) + seed
        val bitflipHigh = ((read32LE(secret, 8).toLong() and 0xFFFF_FFFFL) xor
            (read32LE(secret, 12).toLong() and 0xFFFF_FFFFL)) - seed
        return Hash128(
            low64 = xxh64Avalanche((combinedLow.toLong() and 0xFFFF_FFFFL) xor bitflipLow),
            high64 = xxh64Avalanche((combinedHigh.toLong() and 0xFFFF_FFFFL) xor bitflipHigh),
        )
    }

    private fun len4to8_128(input: ByteArray, len: Int, secret: ByteArray, seed: Long): Hash128 {
        val keyedSeed = seed xor ((swap32(seed.toInt()).toLong() and 0xFFFF_FFFFL) shl 32)
        val inputLow = read32LE(input, 0).toLong() and 0xFFFF_FFFFL
        val inputHigh = read32LE(input, len - 4).toLong() and 0xFFFF_FFFFL
        val input64 = inputLow + (inputHigh shl 32)
        val bitflip = (read64LE(secret, 16) xor read64LE(secret, 24)) + keyedSeed
        val m128 = mult64To128(input64 xor bitflip, PRIME64_1 + (len.toLong() shl 2))
        m128.high64 += m128.low64 shl 1
        m128.low64 = m128.low64 xor (m128.high64 ushr 3)
        m128.low64 = xorshift64(m128.low64, 35) * PRIME_MX2
        m128.low64 = xorshift64(m128.low64, 28)
        m128.high64 = avalanche64(m128.high64)
        return m128
    }

    private fun len9to16_128(input: ByteArray, len: Int, secret: ByteArray, seed: Long): Hash128 {
        val bitflipLow = (read64LE(secret, 32) xor read64LE(secret, 40)) - seed
        val bitflipHigh = (read64LE(secret, 48) xor read64LE(secret, 56)) + seed
        val inputLow = read64LE(input, 0)
        var inputHigh = read64LE(input, len - 8)
        val m128 = mult64To128(inputLow xor inputHigh xor bitflipLow, PRIME64_1)
        m128.low64 += (len - 1).toLong() shl 54
        inputHigh = inputHigh xor bitflipHigh
        m128.high64 += inputHigh + mult32To64(inputHigh, PRIME32_2 - 1)
        m128.low64 = m128.low64 xor swap64(m128.high64)
        val h128 = mult64To128(m128.low64, PRIME64_2)
        h128.high64 += m128.high64 * PRIME64_2
        h128.low64 = avalanche64(h128.low64)
        h128.high64 = avalanche64(h128.high64)
        return h128
    }

    private fun len17to128_128(input: ByteArray, len: Int, secret: ByteArray, seed: Long): Hash128 {
        var acc = Hash128(len.toLong() * PRIME64_1, 0L)
        if (len > 32) {
            if (len > 64) {
                if (len > 96) {
                    acc = mix32B(acc, input, 48, input, len - 64, secret, 96, seed)
                }
                acc = mix32B(acc, input, 32, input, len - 48, secret, 64, seed)
            }
            acc = mix32B(acc, input, 16, input, len - 32, secret, 32, seed)
        }
        acc = mix32B(acc, input, 0, input, len - 16, secret, 0, seed)
        return finalizeShort128(acc, len, seed)
    }

    private fun len129to240_128(input: ByteArray, len: Int, secret: ByteArray, seed: Long): Hash128 {
        var acc = Hash128(len.toLong() * PRIME64_1, 0L)
        var i = 32
        while (i < 160) {
            acc = mix32B(acc, input, i - 32, input, i - 16, secret, i - 32, seed)
            i += 32
        }
        acc.low64 = avalanche64(acc.low64)
        acc.high64 = avalanche64(acc.high64)
        i = 160
        while (i <= len) {
            acc = mix32B(acc, input, i - 32, input, i - 16, secret, MIDSIZE_STARTOFFSET + i - 160, seed)
            i += 32
        }
        acc = mix32B(acc, input, len - 16, input, len - 32, secret, 136 - MIDSIZE_LASTOFFSET - 16, -seed)
        return finalizeShort128(acc, len, seed)
    }

    private fun finalizeShort128(acc: Hash128, len: Int, seed: Long): Hash128 =
        Hash128(
            low64 = avalanche64(acc.low64 + acc.high64),
            high64 = -avalanche64(acc.low64 * PRIME64_1 + acc.high64 * PRIME64_4 + (len.toLong() - seed) * PRIME64_2),
        )

    private fun hashLong64(input: ByteArray, secret: ByteArray): Long {
        val acc = initAcc()
        hashLongInternalLoop(acc, input, secret)
        return mergeAccs(acc, secret, SECRET_MERGEACCS_START, input.size.toLong() * PRIME64_1)
    }

    private fun hashLong128(input: ByteArray, secret: ByteArray): Hash128 {
        val acc = initAcc()
        hashLongInternalLoop(acc, input, secret)
        return Hash128(
            low64 = mergeAccs(acc, secret, SECRET_MERGEACCS_START, input.size.toLong() * PRIME64_1),
            high64 = mergeAccs(
                acc,
                secret,
                secret.size - STRIPE_LEN - SECRET_MERGEACCS_START,
                (input.size.toLong() * PRIME64_2).inv(),
            ),
        )
    }

    private fun hashLongInternalLoop(acc: LongArray, input: ByteArray, secret: ByteArray) {
        val nbStripesPerBlock = (secret.size - STRIPE_LEN) / SECRET_CONSUME_RATE
        val blockLength = STRIPE_LEN * nbStripesPerBlock
        val nbBlocks = (input.size - 1) / blockLength

        for (block in 0 until nbBlocks) {
            accumulate(acc, input, block * blockLength, secret, 0, nbStripesPerBlock)
            scrambleAcc(acc, secret, secret.size - STRIPE_LEN)
        }

        val nbStripes = ((input.size - 1) - (blockLength * nbBlocks)) / STRIPE_LEN
        accumulate(acc, input, nbBlocks * blockLength, secret, 0, nbStripes)
        accumulate512(acc, input, input.size - STRIPE_LEN, secret, secret.size - STRIPE_LEN - SECRET_LASTACC_START)
    }

    private fun accumulate(acc: LongArray, input: ByteArray, inputOffset: Int, secret: ByteArray, secretOffset: Int, nbStripes: Int) {
        for (n in 0 until nbStripes) {
            accumulate512(
                acc,
                input,
                inputOffset + n * STRIPE_LEN,
                secret,
                secretOffset + n * SECRET_CONSUME_RATE,
            )
        }
    }

    private fun accumulate512(acc: LongArray, input: ByteArray, inputOffset: Int, secret: ByteArray, secretOffset: Int) {
        for (lane in 0 until ACC_NB) {
            val dataValue = read64LE(input, inputOffset + lane * 8)
            val dataKey = dataValue xor read64LE(secret, secretOffset + lane * 8)
            acc[lane xor 1] += dataValue
            acc[lane] += mult32To64(dataKey, dataKey ushr 32)
        }
    }

    private fun scrambleAcc(acc: LongArray, secret: ByteArray, secretOffset: Int) {
        for (lane in 0 until ACC_NB) {
            var acc64 = acc[lane]
            acc64 = xorshift64(acc64, 47)
            acc64 = acc64 xor read64LE(secret, secretOffset + lane * 8)
            acc64 *= PRIME32_1
            acc[lane] = acc64
        }
    }

    private fun mergeAccs(acc: LongArray, secret: ByteArray, secretOffset: Int, start: Long): Long {
        var result = start
        for (i in 0 until 4) {
            result += mul128Fold64(
                acc[2 * i] xor read64LE(secret, secretOffset + 16 * i),
                acc[2 * i + 1] xor read64LE(secret, secretOffset + 16 * i + 8),
            )
        }
        return avalanche64(result)
    }

    private fun mix16B(input: ByteArray, inputOffset: Int, secret: ByteArray, secretOffset: Int, seed: Long): Long {
        val inputLow = read64LE(input, inputOffset)
        val inputHigh = read64LE(input, inputOffset + 8)
        return mul128Fold64(
            inputLow xor (read64LE(secret, secretOffset) + seed),
            inputHigh xor (read64LE(secret, secretOffset + 8) - seed),
        )
    }

    private fun mix32B(
        acc: Hash128,
        input1: ByteArray,
        inputOffset1: Int,
        input2: ByteArray,
        inputOffset2: Int,
        secret: ByteArray,
        secretOffset: Int,
        seed: Long,
    ): Hash128 {
        acc.low64 += mix16B(input1, inputOffset1, secret, secretOffset, seed)
        acc.low64 = acc.low64 xor (read64LE(input2, inputOffset2) + read64LE(input2, inputOffset2 + 8))
        acc.high64 += mix16B(input2, inputOffset2, secret, secretOffset + 16, seed)
        acc.high64 = acc.high64 xor (read64LE(input1, inputOffset1) + read64LE(input1, inputOffset1 + 8))
        return acc
    }

    private fun initCustomSecret(seed: Long): ByteArray {
        val customSecret = ByteArray(SECRET.size)
        for (i in 0 until SECRET.size / 16) {
            write64LE(customSecret, 16 * i, read64LE(SECRET, 16 * i) + seed)
            write64LE(customSecret, 16 * i + 8, read64LE(SECRET, 16 * i + 8) - seed)
        }
        return customSecret
    }

    private fun initAcc(): LongArray =
        longArrayOf(PRIME32_3, PRIME64_1, PRIME64_2, PRIME64_3, PRIME64_4, PRIME32_2, PRIME64_5, PRIME32_1)

    private fun mul128Fold64(lhs: Long, rhs: Long): Long {
        val product = mult64To128(lhs, rhs)
        return product.low64 xor product.high64
    }

    private fun mult64To128(lhs: Long, rhs: Long): Hash128 {
        val loLo = mult32To64(lhs, rhs)
        val hiLo = mult32To64(lhs ushr 32, rhs)
        val loHi = mult32To64(lhs, rhs ushr 32)
        val hiHi = mult32To64(lhs ushr 32, rhs ushr 32)
        val cross = (loLo ushr 32) + (hiLo and 0xFFFF_FFFFL) + loHi
        val upper = (hiLo ushr 32) + (cross ushr 32) + hiHi
        val lower = (cross shl 32) or (loLo and 0xFFFF_FFFFL)
        return Hash128(low64 = lower, high64 = upper)
    }

    private fun mult32To64(lhs: Long, rhs: Long): Long =
        (lhs and 0xFFFF_FFFFL) * (rhs and 0xFFFF_FFFFL)

    private fun avalanche64(value: Long): Long {
        var hash = xorshift64(value, 37)
        hash *= PRIME_MX1
        hash = xorshift64(hash, 32)
        return hash
    }

    private fun rrmxmx(value: Long, len: Long): Long {
        var hash = value
        hash = hash xor rotateLeft(hash, 49) xor rotateLeft(hash, 24)
        hash *= PRIME_MX2
        hash = hash xor ((hash ushr 35) + len)
        hash *= PRIME_MX2
        return xorshift64(hash, 28)
    }

    private fun xxh64Avalanche(value: Long): Long {
        var hash = value
        hash = hash xor (hash ushr 33)
        hash *= PRIME64_2
        hash = hash xor (hash ushr 29)
        hash *= PRIME64_3
        hash = hash xor (hash ushr 32)
        return hash
    }

    private fun xorshift64(value: Long, shift: Int): Long =
        value xor (value ushr shift)

    private fun read32LE(input: ByteArray, index: Int): Int =
        input[index].unsignedByte() or
            (input[index + 1].unsignedByte() shl 8) or
            (input[index + 2].unsignedByte() shl 16) or
            (input[index + 3].unsignedByte() shl 24)

    private fun read64LE(input: ByteArray, index: Int): Long =
        input[index].unsignedByte().toLong() or
            (input[index + 1].unsignedByte().toLong() shl 8) or
            (input[index + 2].unsignedByte().toLong() shl 16) or
            (input[index + 3].unsignedByte().toLong() shl 24) or
            (input[index + 4].unsignedByte().toLong() shl 32) or
            (input[index + 5].unsignedByte().toLong() shl 40) or
            (input[index + 6].unsignedByte().toLong() shl 48) or
            (input[index + 7].unsignedByte().toLong() shl 56)

    private fun write64LE(output: ByteArray, index: Int, value: Long) {
        for (i in 0 until 8) {
            output[index + i] = (value ushr (8 * i)).toByte()
        }
    }

    private fun swap32(value: Int): Int =
        ((value and 0xFF) shl 24) or
            ((value and 0xFF00) shl 8) or
            ((value ushr 8) and 0xFF00) or
            ((value ushr 24) and 0xFF)

    private fun swap64(value: Long): Long =
        ((value and 0x0000_0000_0000_00FFL) shl 56) or
            ((value and 0x0000_0000_0000_FF00L) shl 40) or
            ((value and 0x0000_0000_00FF_0000L) shl 24) or
            ((value and 0x0000_0000_FF00_0000L) shl 8) or
            ((value ushr 8) and 0x0000_0000_FF00_0000L) or
            ((value ushr 24) and 0x0000_0000_00FF_0000L) or
            ((value ushr 40) and 0x0000_0000_0000_FF00L) or
            ((value ushr 56) and 0x0000_0000_0000_00FFL)

    private fun rotateLeft(value: Int, bitCount: Int): Int =
        (value shl bitCount) or (value ushr (32 - bitCount))

    private fun rotateLeft(value: Long, bitCount: Int): Long =
        (value shl bitCount) or (value ushr (64 - bitCount))

    private fun Byte.unsignedByte(): Int = toInt() and 0xFF

    private data class Hash128(var low64: Long, var high64: Long) {
        fun toLongArray(): LongArray = longArrayOf(low64, high64)
    }
}
