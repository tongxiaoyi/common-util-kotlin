package com.oneliang.ktx.test

import com.oneliang.ktx.util.json.JsonUtil

fun main() {
    println(JsonUtil.objectToJson(JsonBean(), emptyArray()))
}