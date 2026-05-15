package io.github.limuyang2.xxhash.demo

import io.github.limuyang2.xxhash.lib.xxh32
import io.github.limuyang2.xxhash.lib.xxh3As128
import io.github.limuyang2.xxhash.lib.xxh3As64
import io.github.limuyang2.xxhash.lib.xxh64

actual object HashCalculator {
    actual fun calculate(input: String): HashSummary {
        val data = input.encodeToByteArray()
        val h128 = data.xxh3As128()

        return HashSummary(
            xxh32 = data.xxh32().toFixedHex(),
            xxh64 = data.xxh64().toFixedHex(),
            xxh3As64 = data.xxh3As64().toFixedHex(),
            xxh3As128 = h128[1].toFixedHex() + h128[0].toFixedHex(),
        )
    }
}

private fun Long.toFixedHex(): String =
    toULong().toString(radix = 16).padStart(16, '0')
