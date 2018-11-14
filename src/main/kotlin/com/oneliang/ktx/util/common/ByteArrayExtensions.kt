package com.oneliang.ktx.util.common

private val hexStringTransform: (Byte) -> CharSequence = { String.format("%02X", (it.toInt() and 0xFF)) }
fun ByteArray.toHexString() = joinToString(separator = "", transform = hexStringTransform)

fun Array<Byte>.toHexString() = joinToString(separator = "", transform = hexStringTransform)