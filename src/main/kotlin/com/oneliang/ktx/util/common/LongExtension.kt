package com.oneliang.ktx.util.common

fun Long.toByteArray(): ByteArray = ByteArray(8) {
    (this shr (8 * it) and 0xFF).toByte()
}.apply { this.reverse() }