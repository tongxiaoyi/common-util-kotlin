package com.oneliang.ktx.util.common

fun Int.toUnsigned(): Long {
    return this.toLong() and 0xffffffffL
}