package com.oneliang.ktx.util.common

fun <T : Any, K, V> Iterable<T>.toMap(transform: (T) -> Pair<K, V>): Map<K, V> = this.associate(transform)