package com.oneliang.ktx.util.file

import com.oneliang.ktx.Constants
import java.io.InputStream

fun InputStream.readContentIgnoreLine(encoding: String = Constants.Encoding.UTF8, append: String = Constants.String.BLANK) = FileUtil.readInputStreamContentIgnoreLine(this, encoding, append)