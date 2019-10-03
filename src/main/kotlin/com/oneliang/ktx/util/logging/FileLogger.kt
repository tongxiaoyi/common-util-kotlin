package com.oneliang.ktx.util.logging

import com.oneliang.ktx.Constants
import com.oneliang.ktx.util.file.FileUtil
import com.oneliang.ktx.util.logging.Logger.Level
import java.io.*

class FileLogger(level: Level, outputFile: File) : BaseLogger(level) {
    private var fileOutputStream: FileOutputStream? = null

    init {
        try {
            FileUtil.createFile(outputFile.absolutePath)
            this.fileOutputStream = FileOutputStream(outputFile, true)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun log(level: Level, message: String, throwable: Throwable?, extraInfo: ExtraInfo) {
        val messageString = this.generateLogContent(level, message, throwable, extraInfo) + Constants.String.CRLF_STRING
        try {
            val fileOutputStream = this.fileOutputStream
            if (fileOutputStream != null) {
                fileOutputStream.write(messageString.toByteArray())
                throwable?.printStackTrace(PrintStream(fileOutputStream))
                fileOutputStream.flush()
            }
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