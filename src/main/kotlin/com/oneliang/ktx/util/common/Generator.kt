package com.oneliang.ktx.util.common

import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.FileInputStream
import java.util.*

object Generator {
    private val characters = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
    private const val FONT_FAMILY_TIMES_NEW_ROMAN = "Times New Roman"
    private const val COUNT_MAX_LENGTH = 3
    private const val COUNT_MAX_VALUE = 999
    private val countThreadLocal = object : ThreadLocal<Int>() {
        override fun initialValue(): Int {
            return 0
        }
    }

    /**
     * the union id generator
     *
     * @return String
     */
    fun ID(): String {
        var count = countThreadLocal.get()
        val threadId = Thread.currentThread().id
        val timeMillis = System.currentTimeMillis()
        val result = timeMillis.toString() + StringUtil.fillZero(COUNT_MAX_LENGTH + 1 - threadId.toString().length) + threadId.toString() + StringUtil.fillZero(COUNT_MAX_LENGTH - count.toString().length) + count.toString()
        count++
        if (count > COUNT_MAX_VALUE) {
            count = 0
        }
        countThreadLocal.set(count)
        return result
    }

    /**
     * the uuid generator
     * @return String
     */
    fun UUID(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * random string
     * @param size
     * @return String
     */
    fun randomString(size: Int): String {
        val stringBuilder = StringBuilder()
        val random = Random()
        for (i in 0 until size) {
            val string = Character.toString(characters[random.nextInt(characters.size)])
            stringBuilder.append(string)
        }
        return stringBuilder.toString()
    }

    /**
     * create random image
     *
     * @param string
     * @param width
     * @param height
     * @return BufferedImage
     */
    fun createRandomImage(string: String, width: Int, height: Int): BufferedImage {
        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val graphics = bufferedImage.getGraphics()
        val random = Random()
        graphics.color = getRandomColor(200, 250)
        graphics.fillRect(0, 0, width, height)
        graphics.font = Font(FONT_FAMILY_TIMES_NEW_ROMAN, Font.PLAIN, 18)
        graphics.color = getRandomColor(160, 200)
        for (i in 0..154) {
            val x = random.nextInt(width)
            val y = random.nextInt(height)
            val xl = random.nextInt(12)
            val yl = random.nextInt(12)
            graphics.drawLine(x, y, x + xl, y + yl)
        }
        for (i in 0 until string.length) {
            graphics.color = Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110))
            graphics.drawString(string.get(i).toString(), 14 * i + 6, 16)
        }
        graphics.dispose()
        return bufferedImage
    }

    /**
     * get random color
     * @param frontColor
     * @param backColor
     * @return Color
     */
    private fun getRandomColor(frontColor: Int, backColor: Int): Color {
        val random = Random()
        val newFrontColor = if (frontColor > 0xFF) 0xFF else frontColor
        val newBackColor = if (backColor > 0xFF) 0xFF else backColor
        val red = newFrontColor + random.nextInt(newBackColor - newFrontColor)
        val green = newFrontColor + random.nextInt(newBackColor - newFrontColor)
        val blue = newFrontColor + random.nextInt(newBackColor - newFrontColor)
        return Color(red, green, blue)
    }

    /**
     * refer to rfc2104 HMAC
     * @param key
     * @param data
     * @return byte[]
     */
    fun getHmacMd5Bytes(key: ByteArray, data: ByteArray): ByteArray {
/*
		 * HmacMd5 calculation formula: H(K XOR opad, H(K XOR ipad, text))
		 *
		 * HmacMd5 计算公式：H(K XOR opad, H(K XOR ipad, text))
		 *
		 * H代表hash算法，本类中使用MD5算法，K代表密钥，text代表要加密的数据
		 *
		 * ipad为0x36，opad为0x5C。
		 */
        val length = 64
        val ipad = ByteArray(length)
        val opad = ByteArray(length)
        for (i in 0..63) {
            ipad[i] = 0x36
            opad[i] = 0x5C
        }
        var actualKey = key // Actual key.
        val keyArr = ByteArray(length) // Key bytes of 64 bytes length
/*
		 * If key's length is longer than 64,then use hash to digest it and use
		 * the result as actual key.
		 *
		 * 如果密钥长度，大于64字节，就使用哈希算法，计算其摘要，作为真正的密钥。
		 */
        if (key.size > length) {
            actualKey = key.MD5()
        }
        for (i in actualKey.indices) {
            keyArr[i] = actualKey[i]
        }
/*
		 * append zeros to K
		 *
		 * 如果密钥长度不足64字节，就使用0x00补齐到64字节。
		 */
        if (actualKey.size < length) {
            for (i in actualKey.size until keyArr.size) {
                keyArr[i] = 0x00
            }
        }
/*
		 * calc K XOR ipad
		 *
		 * 使用密钥和ipad进行异或运算。
		 */
        val kIpadXorResult = ByteArray(length)
        for (i in 0 until length) {
            kIpadXorResult[i] = (keyArr[i].toInt() xor ipad[i].toInt()).toByte()
        }
/*
		 * append "text" to the end of "K XOR ipad"
		 *
		 * 将待加密数据追加到K XOR ipad计算结果后面。
		 */
        val firstAppendResult = ByteArray(kIpadXorResult.size + data.size)
        for (i in kIpadXorResult.indices) {
            firstAppendResult[i] = kIpadXorResult[i]
        }
        for (i in data.indices) {
            firstAppendResult[i + keyArr.size] = data[i]
        }
/*
		 * calc H(K XOR ipad, text)
		 *
		 * 使用哈希算法计算上面结果的摘要。
		 */
        val firstHashResult = firstAppendResult.MD5()
/*
		 * calc K XOR opad
		 *
		 * 使用密钥和opad进行异或运算。
		 */
        val kOpadXorResult = ByteArray(length)
        for (i in 0 until length) {
            kOpadXorResult[i] = (keyArr[i].toInt() xor opad[i].toInt()).toByte()
        }
/*
		 * append "H(K XOR ipad, text)" to the end of "K XOR opad"
		 *
		 * 将H(K XOR ipad, text)结果追加到K XOR opad结果后面
		 */
        val secondAppendResult = ByteArray(kOpadXorResult.size + firstHashResult.size)
        for (i in kOpadXorResult.indices) {
            secondAppendResult[i] = kOpadXorResult[i]
        }
        for (i in firstHashResult.indices) {
            secondAppendResult[i + keyArr.size] = firstHashResult[i]
        }
/*
		 * H(K XOR opad, H(K XOR ipad, text))
		 *
		 * 对上面的数据进行哈希运算。
		 */
        return secondAppendResult.MD5()
    }

    /**
     * md5 file
     * @param fullFilename
     * @return String
     */
    fun MD5File(fullFilename: String): String {
        val result: String
        try {
            result = FileInputStream(fullFilename).MD5().toHexString()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return result
    }

    /**
     * SHA1 byte array
     * @param byteArray
     * @return byte[]
     */
    fun SHA1ByteArray(byteArray: ByteArray): ByteArray {
        val result: ByteArray
        try {
            val digest = java.security.MessageDigest.getInstance("SHA-1")
            digest.update(byteArray)
            result = digest.digest()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return result
    }

    /**
     * SHA1
     * @param source
     * @return String
     */
    fun SHA1(source: String): String {
        val string: String
        try {
            string = SHA1ByteArray(source.toByteArray(Charsets.UTF_8)).toHexString()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return string
    }
}