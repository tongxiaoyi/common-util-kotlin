package com.oneliang.ktx.util.common

private val hexStringTransform: (Byte) -> CharSequence = { String.format("%02X", (it.toInt() and 0xFF)) }
fun ByteArray.toHexString() = joinToString(separator = "", transform = hexStringTransform)

fun Array<Byte>.toHexString() = joinToString(separator = "", transform = hexStringTransform)

private val binaryStringTransform: (Byte) -> CharSequence = { String.format("%8s", Integer.toBinaryString(it.toInt() and 0xFF)).replace(' ', '0') }
fun ByteArray.toBinaryString() = joinToString(separator = "", transform = binaryStringTransform)

fun Array<Byte>.toBinaryString() = joinToString(separator = "", transform = binaryStringTransform)

private val toInt: ((Array<Byte>) -> Int) = {
    if (it.isNotEmpty() && it.size == 4) {
        var result = 0
        for ((i, byte) in it.reversedArray().withIndex()) {
            result = result or (byte.toInt() and 0xFF shl 8 * i)
        }
        result
    } else {
        0
    }
}

fun ByteArray.toInt(): Int = toInt(this.toTypedArray())
fun Array<Byte>.toInt(): Int = toInt(this)

private val toLong: ((Array<Byte>) -> Long) = {
    if (it.isNotEmpty() && it.size == 8) {
        var result = 0L
        for ((i, byte) in it.reversedArray().withIndex()) {
            result = result or (byte.toLong() and 0xFF shl 8 * i)
        }
        result
    } else {
        0
    }
}

fun ByteArray.toLong(): Long = toLong(this.toTypedArray())
fun Array<Byte>.toLong(): Long = toLong(this)