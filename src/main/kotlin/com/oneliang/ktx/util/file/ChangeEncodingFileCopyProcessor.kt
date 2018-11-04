package com.oneliang.ktx.util.file

import com.oneliang.ktx.util.file.FileUtil.FileCopyProcessor

class ChangeEncodingFileCopyProcessor(private val fromEncoding: String, private val toEncoding: String) : FileCopyProcessor {

    /**
     * copyFileToFileProcess
     * @param from,maybe directory
     * @param to,maybe directory
     * @param isFile,maybe directory or file
     * @return boolean,if true keep going copy,only active in directory so far
     */
    override fun copyFileToFileProcess(fromFile: String, toFile: String, isFile: Boolean): Boolean {
        try {
//            if (isFile) {
//                FileUtil.createFile(toFile)
//                val inputStream = FileInputStream(fromFile)
//                val outputStream = FileOutputStream(toFile)
//                val bufferedReader = BufferedReader(InputStreamReader(inputStream, this.fromEncoding))
//                val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream, this.toEncoding))
//                val string: String? = null
//                while ((string = bufferedReader.readLine()) != null) {
//                    bufferedWriter.write(string + StringUtil.CRLF_STRING)
//                }
//                bufferedReader.close()
//                bufferedWriter.flush()
//                bufferedWriter.close()
//            } else {
//                FileUtil.createDirectory(toFile)
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }
}