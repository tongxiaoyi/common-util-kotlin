package com.oneliang.ktx.util.common


object StringUtil {

    private const val ZERO = "0"

    /**
     * fill zero
     * @param length
     * @return String
     */
    fun fillZero(length: Int): String {
        val stringBuilder = StringBuilder()
        for (i in 0 until length) {
            stringBuilder.append(ZERO)
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