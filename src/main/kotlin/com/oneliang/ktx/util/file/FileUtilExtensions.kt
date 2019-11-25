package com.oneliang.ktx.util.file

import java.io.InputStream
import java.io.OutputStream

fun InputStream.copyTo(outputStream: OutputStream) {
    FileUtil.copyStream(this, outputStream)
}