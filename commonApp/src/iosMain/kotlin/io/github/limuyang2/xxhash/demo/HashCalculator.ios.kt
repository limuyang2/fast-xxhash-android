package io.github.limuyang2.xxhash.demo

actual object HashCalculator {
    actual fun calculate(input: String): HashSummary =
        HashSummary(
            xxh32 = "",
            xxh64 = "iOS hashing will be enabled after the lib iOS actual implementation is added.",
            xxh3As64 = "",
            xxh3As128 = "",
            isSupported = false,
        )
}
