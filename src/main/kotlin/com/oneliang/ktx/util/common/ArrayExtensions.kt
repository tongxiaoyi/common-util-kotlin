package com.oneliang.ktx.util.common

fun <T, K, V> Array<T>.toMap(transform: (T) -> Pair<K, V>): Map<K, V> = this.toMapWithIndex { _, t ->
    transform(t)
}

fun <T, K, V> Array<out T>.toMapWithIndex(transform: (index: Int, t: T) -> Pair<K, V>): Map<K, V> {
    val map = mutableMapOf<K, V>()
    this.forEachIndexed { index, t ->
        map += transform(index, t)
    }
    return map
}