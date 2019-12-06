package com.oneliang.ktx.util.common

import java.io.ByteArrayOutputStream
import java.nio.charset.Charset

fun <T, K, V> Iterable<T>.toMap(transform: (T) -> Pair<K, V>): Map<K, V> = this.associate(transform)

fun <T, K, V> Iterable<T>.toMapWithIndex(transform: (index: Int, t: T) -> Pair<K, V>): Map<K, V> {
    val map = mutableMapOf<K, V>()
    this.forEachIndexed { index, t ->
        map += transform(index, t)
    }
    return map
}

fun List<String>.toByteArray(charset: Charset = Charsets.UTF_8): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.forEach {
        byteArrayOutputStream.write(it.toByteArray(charset))
        byteArrayOutputStream.flush()
    }
    return byteArrayOutputStream.toByteArray()
}