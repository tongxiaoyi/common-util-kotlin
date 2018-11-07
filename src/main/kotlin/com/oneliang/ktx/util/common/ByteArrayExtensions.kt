package com.oneliang.ktx.util.common

fun ByteArray.toHexString(): String {
    val stringBuilder = StringBuilder()
    this.forEach { it ->
        stringBuilder.append(it.toString(16))
    }
    return stringBuilder.toString()
}