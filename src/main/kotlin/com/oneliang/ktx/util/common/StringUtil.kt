package com.oneliang.ktx.util.common

import com.oneliang.ktx.Constants

fun Long.toFillZeroString(maxLength: Int): String {
    return StringUtil.fillZero(maxLength - this.toString().length) + this.toString()
}

fun Int.toFillZeroString(maxLength: Int): String {
    return this.toLong().toFillZeroString(maxLength)
}

object StringUtil {

    /**
     * fill zero
     * @param length
     * @return String
     */
    fun fillZero(length: Int): String {
        val stringBuilder = StringBuilder()
        for (i in 0 until length) {
            stringBuilder.append(Constants.String.ZERO)
        }
        return stringBuilder.toString()
    }

    fun compareString(stringArray1: Array<String>, stringArray2: Array<String>): Array<String> {
        val list = mutableListOf<String>()
        for (i in stringArray1.indices) {
            var sign = false
            for (j in stringArray2.indices) {
                if (stringArray1[i] == stringArray2[j]) {
                    sign = true
                    break
                }
            }
            if (!sign) {
                list.add(stringArray1[i])
            }
        }
        return list.toTypedArray()
    }
}