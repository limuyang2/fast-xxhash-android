#include <stdlib.h>
#include <string.h>
#include <jni.h>

#define XXH_STATIC_LINKING_ONLY
#include "xxhash.h"

/* ========== XXH32 ========== */

JNIEXPORT jlong JNICALL
Java_io_github_limuyang2_xxhash_lib_XXHash_xxh32(JNIEnv *env, jclass cls,
                                                  jbyteArray input, jint seed)
{
    (void)cls;
    jsize len = (*env)->GetArrayLength(env, input);
    jbyte *data = (*env)->GetByteArrayElements(env, input, NULL);
    XXH32_hash_t hash = XXH32(data, (size_t)len, (XXH32_hash_t)seed);
    (*env)->ReleaseByteArrayElements(env, input, data, JNI_ABORT);
    return (jlong)hash;
}

JNIEXPORT jlong JNICALL
Java_io_github_limuyang2_xxhash_lib_XXHash_xxh32Bytes(JNIEnv *env, jclass cls,
                                                       jbyteArray input, jint offset,
                                                       jint length, jint seed)
{
    (void)cls;
    jbyte *data = (*env)->GetByteArrayElements(env, input, NULL);
    XXH32_hash_t hash = XXH32(data + offset, (size_t)length, (XXH32_hash_t)seed);
    (*env)->ReleaseByteArrayElements(env, input, data, JNI_ABORT);
    return (jlong)hash;
}

/* ========== XXH64 ========== */

JNIEXPORT jlong JNICALL
Java_io_github_limuyang2_xxhash_lib_XXHash_xxh64(JNIEnv *env, jclass cls,
                                                  jbyteArray input, jlong seed)
{
    (void)cls;
    jsize len = (*env)->GetArrayLength(env, input);
    jbyte *data = (*env)->GetByteArrayElements(env, input, NULL);
    XXH64_hash_t hash = XXH64(data, (size_t)len, (XXH64_hash_t)seed);
    (*env)->ReleaseByteArrayElements(env, input, data, JNI_ABORT);
    return (jlong)hash;
}

JNIEXPORT jlong JNICALL
Java_io_github_limuyang2_xxhash_lib_XXHash_xxh64Bytes(JNIEnv *env, jclass cls,
                                                       jbyteArray input, jint offset,
                                                       jint length, jlong seed)
{
    (void)cls;
    jbyte *data = (*env)->GetByteArrayElements(env, input, NULL);
    XXH64_hash_t hash = XXH64(data + offset, (size_t)length, (XXH64_hash_t)seed);
    (*env)->ReleaseByteArrayElements(env, input, data, JNI_ABORT);
    return (jlong)hash;
}

/* ========== XXH3 64-bit ========== */

JNIEXPORT jlong JNICALL
Java_io_github_limuyang2_xxhash_lib_XXHash_xxh3_164bits(JNIEnv *env, jclass cls,
                                                         jbyteArray input)
{
    (void)cls;
    jsize len = (*env)->GetArrayLength(env, input);
    jbyte *data = (*env)->GetByteArrayElements(env, input, NULL);
    XXH64_hash_t hash = XXH3_64bits(data, (size_t)len);
    (*env)->ReleaseByteArrayElements(env, input, data, JNI_ABORT);
    return (jlong)hash;
}

JNIEXPORT jlong JNICALL
Java_io_github_limuyang2_xxhash_lib_XXHash_xxh3_164bitsWithSeed(JNIEnv *env, jclass cls,
                                                                 jbyteArray input, jlong seed)
{
    (void)cls;
    jsize len = (*env)->GetArrayLength(env, input);
    jbyte *data = (*env)->GetByteArrayElements(env, input, NULL);
    XXH64_hash_t hash = XXH3_64bits_withSeed(data, (size_t)len, (XXH64_hash_t)seed);
    (*env)->ReleaseByteArrayElements(env, input, data, JNI_ABORT);
    return (jlong)hash;
}

/* ========== XXH3 128-bit ========== */

JNIEXPORT jlongArray JNICALL
Java_io_github_limuyang2_xxhash_lib_XXHash_xxh3_1128bits(JNIEnv *env, jclass cls,
                                                          jbyteArray input)
{
    (void)cls;
    jsize len = (*env)->GetArrayLength(env, input);
    jbyte *data = (*env)->GetByteArrayElements(env, input, NULL);
    XXH128_hash_t hash = XXH3_128bits(data, (size_t)len);
    (*env)->ReleaseByteArrayElements(env, input, data, JNI_ABORT);

    jlong result[2] = {(jlong)hash.low64, (jlong)hash.high64};
    jlongArray arr = (*env)->NewLongArray(env, 2);
    (*env)->SetLongArrayRegion(env, arr, 0, 2, result);
    return arr;
}

JNIEXPORT jlongArray JNICALL
Java_io_github_limuyang2_xxhash_lib_XXHash_xxh3_1128bitsWithSeed(JNIEnv *env, jclass cls,
                                                                  jbyteArray input, jlong seed)
{
    (void)cls;
    jsize len = (*env)->GetArrayLength(env, input);
    jbyte *data = (*env)->GetByteArrayElements(env, input, NULL);
    XXH128_hash_t hash = XXH3_128bits_withSeed(data, (size_t)len, (XXH64_hash_t)seed);
    (*env)->ReleaseByteArrayElements(env, input, data, JNI_ABORT);

    jlong result[2] = {(jlong)hash.low64, (jlong)hash.high64};
    jlongArray arr = (*env)->NewLongArray(env, 2);
    (*env)->SetLongArrayRegion(env, arr, 0, 2, result);
    return arr;
}
