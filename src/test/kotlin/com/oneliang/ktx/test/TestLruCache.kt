package com.oneliang.ktx.test

import com.oneliang.ktx.util.common.LruCache

fun main() {
    val lruCache = LruCache<String, String?>(2)
    lruCache["a"] = "a"
    lruCache["b"] = "b"
    lruCache["c"] = "c"
    lruCache.snapshot().forEach { (key, value) ->
        println("$key,$value")
    }
}