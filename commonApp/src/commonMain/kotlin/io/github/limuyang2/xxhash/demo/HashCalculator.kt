package io.github.limuyang2.xxhash.demo

data class HashSummary(
    val xxh32: String,
    val xxh64: String,
    val xxh3As64: String,
    val xxh3As128: String,
    val isSupported: Boolean = true,
)

expect object HashCalculator {
    fun calculate(input: String): HashSummary
}
