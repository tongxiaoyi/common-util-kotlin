package com.oneliang.ktx.test

import com.oneliang.ktx.util.common.LruCache

class TestLruCache(maxSize: Int) : LruCache<String, String>(maxSize) {
    var index = 0
    override fun create(key: String): String {
        println(index)
        val result = when (index) {
            0 -> {
                "0"
            }
            else -> {
                "else"
            }
        }
        index++
        return result
    }
}

fun main() {
    val lruCache = TestLruCache(2)
    println(lruCache["a"])
    println(lruCache["a"])
    lruCache.snapshot().forEach { (key, value) ->
        println("$key,$value")
    }
}