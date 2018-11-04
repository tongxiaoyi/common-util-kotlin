package com.oneliang.ktx.util.logging

import com.oneliang.ktx.Constants
import com.oneliang.ktx.util.logging.Logger.Level
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream

class FileLogger(level: Level, outputFile: File) : BaseLogger(level) {
    private var fileOutputStream: FileOutputStream? = null

    init {
        try {
//            FileUtil.createFile(outputFile.getAbsolutePath())
            this.fileOutputStream = FileOutputStream(outputFile, true)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun log(level: Level, message: Any, throwable: Throwable?) {
        val messageString = this.processMessage(level, message, throwable) + Constants.String.CRLF_STRING
        try {
            this.fileOutputStream?.write(messageString.toByteArray())
            if (throwable != null) {
                throwable.printStackTrace(PrintStream(this.fileOutputStream))
            }
            this.fileOutputStream?.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun destroy() {
        try {
            this.fileOutputStream?.flush()
            this.fileOutputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}