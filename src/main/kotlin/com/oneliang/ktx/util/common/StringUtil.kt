package com.oneliang.ktx.util.common

import com.oneliang.ktx.Constants
import java.util.regex.Pattern


object StringUtil {

    /**
     *
     * Method:only for regex,parse regex group when regex include group
     * @param string
     * @param regex
     * @return List<String>
    </String> */
    fun parseRegexGroup(string: String, regex: String): List<String> {
        val groupList = mutableListOf<String>()
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(string)
        val groupCount = matcher.groupCount()
        var count = 1
        if (matcher.find()) {
            while (count <= groupCount) {
                groupList.add(matcher.group(count))
                count++
            }
        }
        return groupList
    }

    /**
     *
     *
     * Method: check the string match the regex or not and return the match
     * field value
     * like {xxxx} can find xxxx
     *
     * @param string
     * @param regex
     * @param firstRegex
     * @param firstRegexReplace
     * @param lastRegexStringLength like {xxxx},last regex string is "}" so last regex string length equals 1
     * @return List<String>
    </String> */
    fun parseStringGroup(string: String, regex: String, firstRegex: String = Constants.String.BLANK, firstRegexReplace: String = Constants.String.BLANK, lastRegexStringLength: Int = 0): List<String> {
        val list = mutableListOf<String>()
        val lastRegexLength = if (lastRegexStringLength < 0) 0 else lastRegexStringLength
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(string)
        var group: String?
        var start = 0
        while (matcher.find(start)) {
            start = matcher.end()
            group = matcher.group()
            group = group!!.replaceFirst(firstRegex.toRegex(), firstRegexReplace)
            group = group.substring(0, group.length - lastRegexLength)
            list.add(group)
        }
        return list
    }
}