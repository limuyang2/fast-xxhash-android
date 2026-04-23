package io.github.limuyang2.xxhash.lib

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class XXHashTest {

    // ======================== XXH32 ========================

    @Test
    fun xxh32_empty() {
        assertEquals("0000000002cc5d05", XXHash.xxh32(byteArrayOf(), 0).toHexString())
    }

    @Test
    fun xxh32_empty_withSeed() {
        assertEquals("00000000d5be6eb8", XXHash.xxh32(byteArrayOf(), 42).toHexString())
    }

    @Test
    fun xxh32_hello() {
        val data = "hello".toByteArray()
        assertEquals("00000000fb0077f9", XXHash.xxh32(data, 0).toHexString())
    }

    @Test
    fun xxh32_hello_withSeed() {
        val data = "hello".toByteArray()
        assertEquals("000000004d02c966", XXHash.xxh32(data, 42).toHexString())
    }

    @Test
    fun xxh32_differentInput_differentResult() {
        val foo = "foo".toByteArray()
        val bar = "bar".toByteArray()
        assertNotEquals(XXHash.xxh32(foo, 0), XXHash.xxh32(bar, 0))
    }

    @Test
    fun xxh32Bytes_matchesSubArray() {
        val full = "Hello, World!".toByteArray()
        // offset=7, length=6 -> "World!"
        assertEquals("000000004007de50", XXHash.xxh32Bytes(full, 0, full.size, 0).toHexString())
        assertEquals(XXHash.xxh32(full, 0), XXHash.xxh32Bytes(full, 0, full.size, 0))
        assertEquals(XXHash.xxh32(full, 42), XXHash.xxh32Bytes(full, 0, full.size, 42))
    }

    @Test
    fun xxh32Bytes_slice() {
        val full = "Hello, World!".toByteArray()
        val sub = "World!".toByteArray()
        assertEquals(XXHash.xxh32(sub, 0), XXHash.xxh32Bytes(full, 7, 6, 0))
        assertEquals(XXHash.xxh32(sub, 42), XXHash.xxh32Bytes(full, 7, 6, 42))
    }

    // ======================== XXH64 ========================

    @Test
    fun xxh64_empty() {
        assertEquals("ef46db3751d8e999", XXHash.xxh64(byteArrayOf(), 0).toHexString())
    }

    @Test
    fun xxh64_empty_withSeed() {
        assertEquals("98b1582b0977e704", XXHash.xxh64(byteArrayOf(), 42).toHexString())
    }

    @Test
    fun xxh64_hello() {
        assertEquals("26c7827d889f6da3", XXHash.xxh64("hello".toByteArray(), 0).toHexString())
    }

    @Test
    fun xxh64_hello_withSeed() {
        assertEquals("c3629e6318d53932", XXHash.xxh64("hello".toByteArray(), 42).toHexString())
    }

    @Test
    fun xxh64_differentInput_differentResult() {
        val foo = "foo".toByteArray()
        val bar = "bar".toByteArray()
        assertNotEquals(XXHash.xxh64(foo, 0), XXHash.xxh64(bar, 0))
    }

    @Test
    fun xxh64Bytes_fullRange_matchesXxh64() {
        val data = "Hello, World!".toByteArray()
        assertEquals(XXHash.xxh64(data, 0), XXHash.xxh64Bytes(data, 0, data.size, 0))
        assertEquals(XXHash.xxh64(data, 42), XXHash.xxh64Bytes(data, 0, data.size, 42))
    }

    @Test
    fun xxh64Bytes_slice() {
        val full = "Hello, World!".toByteArray()
        val sub = "World!".toByteArray()
        assertEquals(XXHash.xxh64(sub, 0), XXHash.xxh64Bytes(full, 7, 6, 0))
        assertEquals(XXHash.xxh64(sub, 42), XXHash.xxh64Bytes(full, 7, 6, 42))
    }

    // ======================== XXH3 64-bit ========================

    @Test
    fun xxh3_64bits_empty() {
        assertEquals("2d06800538d394c2", XXHash.xxh3_64bits(byteArrayOf()).toHexString())
    }

    @Test
    fun xxh3_64bits_hello() {
        assertEquals("9555e8555c62dcfd", XXHash.xxh3_64bits("hello".toByteArray()).toHexString())
    }

    @Test
    fun xxh3_64bits_helloWorld() {
        assertEquals("60415d5f616602aa", XXHash.xxh3_64bits("Hello, World!".toByteArray()).toHexString())
    }

    @Test
    fun xxh3_64bits_differentInput_differentResult() {
        val foo = "foo".toByteArray()
        val bar = "bar".toByteArray()
        assertNotEquals(XXHash.xxh3_64bits(foo), XXHash.xxh3_64bits(bar))
    }

    @Test
    fun xxh3_64bitsWithSeed_hello() {
        assertEquals("bafa072f07db7937", XXHash.xxh3_64bitsWithSeed("hello".toByteArray(), 42).toHexString())
    }

    @Test
    fun xxh3_64bitsWithSeed_differentSeed_differentResult() {
        val data = "hello".toByteArray()
        assertNotEquals(
            XXHash.xxh3_64bitsWithSeed(data, 0),
            XXHash.xxh3_64bitsWithSeed(data, 1)
        )
    }

    // ======================== XXH3 128-bit ========================

    @Test
    fun xxh3_128bits_empty() {
        val h = XXHash.xxh3_128bits(byteArrayOf())
        assertEquals(2, h.size)
        assertEquals("6001c324468d497f", h[0].toHexString())
        assertEquals("99aa06d3014798d8", h[1].toHexString())
    }

    @Test
    fun xxh3_128bits_hello() {
        val h = XXHash.xxh3_128bits("hello".toByteArray())
        assertEquals("c779cfaa5e523818", h[0].toHexString())
        assertEquals("b5e9c1ad071b3e7f", h[1].toHexString())
    }

    @Test
    fun xxh3_128bits_differentInput_differentResult() {
        val foo = "foo".toByteArray()
        val bar = "bar".toByteArray()
        assertFalse(XXHash.xxh3_128bits(foo) contentEquals XXHash.xxh3_128bits(bar))
    }

    @Test
    fun xxh3_128bitsWithSeed_hello() {
        val h = XXHash.xxh3_128bitsWithSeed("hello".toByteArray(), 42)
        assertEquals("8c2f5b7e4cd59e16", h[0].toHexString())
        assertEquals("6ce89a0bdba81f08", h[1].toHexString())
    }

    @Test
    fun xxh3_128bitsWithSeed_differentSeed_differentResult() {
        val data = "hello".toByteArray()
        assertFalse(
            XXHash.xxh3_128bitsWithSeed(data, 0) contentEquals
                    XXHash.xxh3_128bitsWithSeed(data, 1)
        )
    }
}
