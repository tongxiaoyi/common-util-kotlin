package com.oneliang.ktx.util.common

import java.io.ByteArrayOutputStream
import java.nio.charset.Charset

fun <T : Any, K, V> Iterable<T>.toMap(transform: (T) -> Pair<K, V>): Map<K, V> = this.associate(transform)

fun List<String>.toByteArray(charset: Charset = Charsets.UTF_8): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.forEach {
        byteArrayOutputStream.write(it.toByteArray(charset))
        byteArrayOutputStream.flush()
    }
    return byteArrayOutputStream.toByteArray()
}