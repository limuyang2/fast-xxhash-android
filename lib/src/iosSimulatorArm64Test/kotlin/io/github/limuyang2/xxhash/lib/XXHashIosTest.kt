package io.github.limuyang2.xxhash.lib

import kotlin.test.Test
import kotlin.test.assertEquals

class XXHashIosTest {
    @Test
    fun xxh32_empty() {
        assertEquals("0000000002cc5d05", XXHash.xxh32(byteArrayOf(), 0).toFixedHex())
    }

    @Test
    fun xxh64_hello() {
        assertEquals("26c7827d889f6da3", XXHash.xxh64("hello".encodeToByteArray(), 0).toFixedHex())
    }

    @Test
    fun xxh3_64bits_helloWorld() {
        assertEquals("60415d5f616602aa", XXHash.xxh3_64bits("Hello, World!".encodeToByteArray()).toFixedHex())
    }

    @Test
    fun xxh3_128bits_hello() {
        val h = XXHash.xxh3_128bits("hello".encodeToByteArray())
        assertEquals("c779cfaa5e523818", h[0].toFixedHex())
        assertEquals("b5e9c1ad071b3e7f", h[1].toFixedHex())
    }
}

private fun Long.toFixedHex(): String =
    toULong().toString(radix = 16).padStart(16, '0')
