package com.oneliang.ktx.util.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toFormatString(format: String, locale: Locale = Locale.getDefault()): String {
    val simpleDateFormat = SimpleDateFormat(format, locale);
    return simpleDateFormat.format(this);
}