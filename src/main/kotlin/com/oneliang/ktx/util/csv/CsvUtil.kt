package com.oneliang.ktx.util.csv

import com.oneliang.ktx.Constants
import com.oneliang.ktx.util.file.FileUtil
import java.io.BufferedWriter

object CsvUtil {

    fun saveToCsvFile(fullFilename: String, dataList: List<Array<String>>) {
        FileUtil.writeFileContent(fullFilename, object : FileUtil.WriteFileContentProcessor {
            override fun writeContent(bufferedWriter: BufferedWriter) {
                dataList.forEach { data ->
                    val lineStringBuilder = StringBuilder()
                    data.forEach {
                        lineStringBuilder.append(it)
                        lineStringBuilder.append(Constants.Symbol.COMMA)
                    }
                    bufferedWriter.newLine()
                    bufferedWriter.flush()
                }
            }
        })
    }
}

fun List<Array<String>>.saveToCsvFile(fullFilename: String) {
    CsvUtil.saveToCsvFile(fullFilename, this)
}