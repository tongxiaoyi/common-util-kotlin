package com.oneliang.ktx.util.common

fun String.toBase64String(): String = Base64.encodeToString(this.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)

fun String.fromBase64String(): String = String(Base64.decode(this, Base64.DEFAULT), Charsets.UTF_8)