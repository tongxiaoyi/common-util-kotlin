package com.oneliang.ktx.util.common

fun ByteArray.toHexString() = joinToString(separator = "") { String.format("%02X", (it.toInt() and 0xFF)) }