package io.github.limuyang2.xxhash.lib.internal;

public final class XXHashNative {

    static {
        System.loadLibrary("muxxhash");
    }

    private XXHashNative() {
    }

    public static native long xxh32(byte[] input, int seed);

    public static native long xxh32Bytes(byte[] input, int offset, int length, int seed);

    public static native long xxh64(byte[] input, long seed);

    public static native long xxh64Bytes(byte[] input, int offset, int length, long seed);

    public static native long xxh3_64bits(byte[] input);

    public static native long xxh3_64bitsWithSeed(byte[] input, long seed);

    public static native long[] xxh3_128bits(byte[] input);

    public static native long[] xxh3_128bitsWithSeed(byte[] input, long seed);
}
