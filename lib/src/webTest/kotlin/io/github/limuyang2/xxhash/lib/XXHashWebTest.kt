package io.github.limuyang2.xxhash.lib

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

class XXHashWebTest {
    @Test
    fun xxh32_matchesKnownVectors() {
        assertEquals("0000000002cc5d05", XXHash.xxh32(byteArrayOf(), 0).toFixedHex())
        assertEquals("00000000d5be6eb8", XXHash.xxh32(byteArrayOf(), 42).toFixedHex())
        assertEquals("00000000fb0077f9", XXHash.xxh32("hello".encodeToByteArray(), 0).toFixedHex())
        assertEquals("000000004d02c966", XXHash.xxh32("hello".encodeToByteArray(), 42).toFixedHex())
    }

    @Test
    fun xxh32Bytes_matchesSubArray() {
        val full = "Hello, World!".encodeToByteArray()
        val sub = "World!".encodeToByteArray()

        assertEquals("000000004007de50", XXHash.xxh32Bytes(full, 0, full.size, 0).toFixedHex())
        assertEquals(XXHash.xxh32(sub, 0), XXHash.xxh32Bytes(full, 7, 6, 0))
        assertEquals(XXHash.xxh32(sub, 42), XXHash.xxh32Bytes(full, 7, 6, 42))
    }

    @Test
    fun xxh64_matchesKnownVectors() {
        assertEquals("ef46db3751d8e999", XXHash.xxh64(byteArrayOf(), 0).toFixedHex())
        assertEquals("98b1582b0977e704", XXHash.xxh64(byteArrayOf(), 42).toFixedHex())
        assertEquals("26c7827d889f6da3", XXHash.xxh64("hello".encodeToByteArray(), 0).toFixedHex())
        assertEquals("c3629e6318d53932", XXHash.xxh64("hello".encodeToByteArray(), 42).toFixedHex())
    }

    @Test
    fun xxh64Bytes_matchesSubArray() {
        val full = "Hello, World!".encodeToByteArray()
        val sub = "World!".encodeToByteArray()

        assertEquals(XXHash.xxh64(full, 0), XXHash.xxh64Bytes(full, 0, full.size, 0))
        assertEquals(XXHash.xxh64(full, 42), XXHash.xxh64Bytes(full, 0, full.size, 42))
        assertEquals(XXHash.xxh64(sub, 0), XXHash.xxh64Bytes(full, 7, 6, 0))
        assertEquals(XXHash.xxh64(sub, 42), XXHash.xxh64Bytes(full, 7, 6, 42))
    }

    @Test
    fun xxh3_64bits_matchesKnownVectors() {
        assertEquals("2d06800538d394c2", XXHash.xxh3_64bits(byteArrayOf()).toFixedHex())
        assertEquals("9555e8555c62dcfd", XXHash.xxh3_64bits("hello".encodeToByteArray()).toFixedHex())
        assertEquals("60415d5f616602aa", XXHash.xxh3_64bits("Hello, World!".encodeToByteArray()).toFixedHex())
        assertEquals("bafa072f07db7937", XXHash.xxh3_64bitsWithSeed("hello".encodeToByteArray(), 42).toFixedHex())
    }

    @Test
    fun xxh3_64bits_changesByInputAndSeed() {
        assertNotEquals(XXHash.xxh3_64bits("foo".encodeToByteArray()), XXHash.xxh3_64bits("bar".encodeToByteArray()))
        assertNotEquals(
            XXHash.xxh3_64bitsWithSeed("hello".encodeToByteArray(), 0),
            XXHash.xxh3_64bitsWithSeed("hello".encodeToByteArray(), 1),
        )
    }

    @Test
    fun xxh3_128bits_matchesKnownVectors() {
        val empty = XXHash.xxh3_128bits(byteArrayOf())
        assertEquals("6001c324468d497f", empty[0].toFixedHex())
        assertEquals("99aa06d3014798d8", empty[1].toFixedHex())

        val hello = XXHash.xxh3_128bits("hello".encodeToByteArray())
        assertEquals("c779cfaa5e523818", hello[0].toFixedHex())
        assertEquals("b5e9c1ad071b3e7f", hello[1].toFixedHex())

        val seeded = XXHash.xxh3_128bitsWithSeed("hello".encodeToByteArray(), 42)
        assertEquals("8c2f5b7e4cd59e16", seeded[0].toFixedHex())
        assertEquals("6ce89a0bdba81f08", seeded[1].toFixedHex())
    }

    @Test
    fun xxh3_128bits_changesByInputAndSeed() {
        assertFalse(XXHash.xxh3_128bits("foo".encodeToByteArray()) contentEquals XXHash.xxh3_128bits("bar".encodeToByteArray()))
        assertFalse(
            XXHash.xxh3_128bitsWithSeed("hello".encodeToByteArray(), 0) contentEquals
                XXHash.xxh3_128bitsWithSeed("hello".encodeToByteArray(), 1),
        )
    }

    @Test
    fun xxh3_handlesLongInputs() {
        val data = ByteArray(512) { it.toByte() }

        val mid = data.copyOf(200)
        assertEquals("f42a8864feaf0703", XXHash.xxh3_64bits(mid).toFixedHex())
        assertEquals("c335a2de8a09a90e", XXHash.xxh3_64bitsWithSeed(mid, 42).toFixedHex())

        val mid128 = XXHash.xxh3_128bits(mid)
        assertEquals("dd97e9af3609d9f5", mid128[0].toFixedHex())
        assertEquals("cb0395310643ba0e", mid128[1].toFixedHex())

        assertEquals("1059105ad19bfa09", XXHash.xxh3_64bits(data).toFixedHex())
        assertEquals("b3c0be157c7c1132", XXHash.xxh3_64bitsWithSeed(data, 42).toFixedHex())

        val hash128 = XXHash.xxh3_128bits(data)
        assertEquals("1059105ad19bfa09", hash128[0].toFixedHex())
        assertEquals("111d5771df64cbcb", hash128[1].toFixedHex())
    }
}

private fun Long.toFixedHex(): String =
    toULong().toString(radix = 16).padStart(16, '0')
