package com.oneliang.ktx.util.http

import com.oneliang.ktx.Constants
import com.oneliang.ktx.util.logging.LoggerManager
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset

object HttpUtil {

    private val logger = LoggerManager.getLogger(HttpUtil::class.java)
    const val DEFAULT_TIMEOUT = 20000

    /**
     * send request by get method
     *
     * @param httpUrl
     * @param httpHeaderList
     * @param returnEncoding
     * @param advancedOption
     * @return String
     */
    fun sendRequestGet(httpUrl: String, httpHeaderList: List<HttpNameValue> = emptyList(), timeout: Int = DEFAULT_TIMEOUT, returnEncoding: String = Constants.Encoding.UTF8, advancedOption: AdvancedOption? = null): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        sendRequestGet(httpUrl, httpHeaderList, timeout, advancedOption, object : Callback {
            @Throws(Exception::class)
            override fun httpOkCallback(headerFieldMap: Map<String, List<String>>, inputStream: InputStream, contentLength: Int) {
                val buffer = ByteArray(Constants.Capacity.BYTES_PER_KB)
                var dataLength = inputStream.read(buffer, 0, buffer.size)
                while (dataLength != -1) {
                    byteArrayOutputStream.write(buffer, 0, dataLength)
                    dataLength = inputStream.read(buffer, 0, buffer.size)
                }
                byteArrayOutputStream.close()
            }

            @Throws(Exception::class)
            override fun httpNotOkCallback(responseCode: Int, headerFieldMap: Map<String, List<String>>) {
                logger.debug("Response code:$responseCode")
            }

            override fun exceptionCallback(exception: Exception) {
                logger.error(Constants.Base.EXCEPTION, exception)
            }
        })
        val byteArray = byteArrayOutputStream.toByteArray()
        var result: String = Constants.String.BLANK
        if (byteArray.size > 0) {
            try {
                result = String(byteArray, Charset.forName(returnEncoding))
            } catch (e: UnsupportedEncodingException) {
                logger.error(Constants.Base.EXCEPTION, e)
            }
        }
        return result
    }

    /**
     * send request by get method
     *
     * @param httpUrl
     * @param httpHeaderList
     * @param timeout
     * @param advancedOption
     * @param callback
     */
    fun sendRequestGet(httpUrl: String, httpHeaderList: List<HttpNameValue> = emptyList(), timeout: Int = DEFAULT_TIMEOUT, advancedOption: AdvancedOption? = null, callback: Callback) {
        sendRequest(httpUrl, Constants.Http.RequestMethod.GET, httpHeaderList, emptyList(), ByteArray(0), null, timeout, null, advancedOption, callback)
    }


    /**
     * send request by post method
     *
     * @param httpUrl
     * @param httpHeaderList
     * @param httpParameterList
     * @param timeout
     * @param returnEncoding
     * @param advancedOption
     * @return String
     */
    fun sendRequestPost(httpUrl: String, httpHeaderList: List<HttpNameValue> = emptyList(), httpParameterList: List<HttpNameValue> = emptyList(), timeout: Int = DEFAULT_TIMEOUT, returnEncoding: String = Constants.Encoding.UTF8, advancedOption: AdvancedOption? = null): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        sendRequestPost(httpUrl, httpHeaderList, httpParameterList, timeout, advancedOption, object : Callback {
            @Throws(Exception::class)
            override fun httpOkCallback(headerFieldMap: Map<String, List<String>>, inputStream: InputStream, contentLength: Int) {
                val buffer = ByteArray(Constants.Capacity.BYTES_PER_KB)
                var dataLength = inputStream.read(buffer, 0, buffer.size)
                while (dataLength != -1) {
                    byteArrayOutputStream.write(buffer, 0, dataLength)
                    dataLength = inputStream.read(buffer, 0, buffer.size)
                }
                byteArrayOutputStream.close()
            }

            override fun exceptionCallback(exception: Exception) {
                logger.error(Constants.Base.EXCEPTION, exception)
            }

            @Throws(Exception::class)
            override fun httpNotOkCallback(responseCode: Int, headerFieldMap: Map<String, List<String>>) {
                logger.debug("Response code:$responseCode")
            }
        })
        val byteArray = byteArrayOutputStream.toByteArray()
        var result: String = Constants.String.BLANK
        if (byteArray.size > 0) {
            try {
                result = String(byteArray, Charset.forName(returnEncoding))
            } catch (e: UnsupportedEncodingException) {
                logger.error(Constants.Base.EXCEPTION, e)
            }

        }
        return result
    }

    /**
     * send request by post method,most for download
     *
     * @param httpUrl
     * @param httpHeaderList
     * @param httpParameterList
     * @param timeout
     * @param advancedOption
     * @param callback
     */
    fun sendRequestPost(httpUrl: String, httpHeaderList: List<HttpNameValue> = emptyList(), httpParameterList: List<HttpNameValue> = emptyList(), timeout: Int = DEFAULT_TIMEOUT, advancedOption: AdvancedOption? = null, callback: Callback) {
        sendRequestPost(httpUrl, httpHeaderList, httpParameterList, ByteArray(0), null, timeout, null, advancedOption, callback)
    }

    /**
     * send request with bytes by post method,most for upload
     *
     * @param httpUrl
     * @param httpHeaderList
     * @param byteArray
     * @param timeout
     * @param advancedOption
     * @return String
     */
    fun sendRequestPostWithBytes(httpUrl: String, httpHeaderList: List<HttpNameValue> = emptyList(), byteArray: ByteArray = ByteArray(0), timeout: Int = DEFAULT_TIMEOUT, advancedOption: AdvancedOption? = null): String {
        val tempByteArray = sendRequestPostWithWholeBytes(httpUrl, httpHeaderList, byteArray, timeout, advancedOption)
        var result: String = Constants.String.BLANK
        if (tempByteArray.size > 0) {
            try {
                result = String(tempByteArray, Charsets.UTF_8)
            } catch (e: UnsupportedEncodingException) {
                logger.error(Constants.Base.EXCEPTION, e)
            }

        }
        return result
    }

    /**
     * send request with whole bytes by post method,most for communication,whole
     * bytes means request and response are bytes
     *
     * @param httpUrl
     * @param httpHeaderList
     * @param byteArray
     * @param timeout
     * @param advancedOption
     * @return byte[]
     */
    fun sendRequestPostWithWholeBytes(httpUrl: String, httpHeaderList: List<HttpNameValue> = emptyList(), byteArray: ByteArray = ByteArray(0), timeout: Int = DEFAULT_TIMEOUT, advancedOption: AdvancedOption? = null): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        sendRequestPostWithBytes(httpUrl, httpHeaderList, byteArray, timeout, advancedOption, object : Callback {
            @Throws(Exception::class)
            override fun httpOkCallback(headerFieldMap: Map<String, List<String>>, inputStream: InputStream, contentLength: Int) {
                val buffer = ByteArray(Constants.Capacity.BYTES_PER_KB)
                var dataLength = inputStream.read(buffer, 0, buffer.size)
                while (dataLength != -1) {
                    byteArrayOutputStream.write(buffer, 0, dataLength)
                    dataLength = inputStream.read(buffer, 0, buffer.size)
                }
                byteArrayOutputStream.close()
            }

            override fun exceptionCallback(exception: Exception) {
                logger.error(Constants.Base.EXCEPTION, exception)
            }

            @Throws(Exception::class)
            override fun httpNotOkCallback(responseCode: Int, headerFieldMap: Map<String, List<String>>) {
                logger.debug("Response code:$responseCode")
            }
        })
        return byteArrayOutputStream.toByteArray()
    }

    /**
     * send request with bytes by post method,most for upload
     *
     * @param httpUrl
     * @param httpHeaderList
     * @param byteArray
     * @param timeout
     * @param advancedOption
     * @param callback
     */
    fun sendRequestPostWithBytes(httpUrl: String, httpHeaderList: List<HttpNameValue> = emptyList(), byteArray: ByteArray = ByteArray(0), timeout: Int = DEFAULT_TIMEOUT, advancedOption: AdvancedOption? = null, callback: Callback) {
        sendRequestPost(httpUrl, httpHeaderList, emptyList(), byteArray, null, timeout, null, advancedOption, callback)
    }

    /**
     * send request with input stream,most for upload
     *
     * @param httpUrl
     * @param httpHeaderList
     * @param inputStream
     * @param timeout
     * @param advancedOption
     * @return String
     */
    fun sendRequestWithInputStream(httpUrl: String, httpHeaderList: List<HttpNameValue> = emptyList(), inputStream: InputStream, timeout: Int = DEFAULT_TIMEOUT, advancedOption: AdvancedOption? = null): String {
        return sendRequestWithInputStream(httpUrl = httpUrl, httpHeaderList = httpHeaderList, inputStream = inputStream, timeout = timeout, inputStreamProcessor = object : InputStreamProcessor {
            @Throws(Exception::class)
            override fun process(inputStream: InputStream, outputStream: OutputStream) {
                val buffer = ByteArray(Constants.Capacity.BYTES_PER_KB)
                var dataLength = inputStream.read(buffer, 0, buffer.size)
                while (dataLength != -1) {
                    outputStream.write(buffer, 0, dataLength)
                    outputStream.flush()
                    dataLength = inputStream.read(buffer, 0, buffer.size)
                }
            }
        }, advancedOption = advancedOption)
    }

    /**
     * send request with input stream,most for upload
     *
     * @param httpUrl
     * @param httpHeaderList
     * @param inputStream
     * @param timeout
     * @param inputStreamProcessor
     * @param returnEncoding
     * @param advancedOption
     * @return String
     */
    fun sendRequestWithInputStream(httpUrl: String, httpHeaderList: List<HttpNameValue> = emptyList(), inputStream: InputStream, timeout: Int = DEFAULT_TIMEOUT, inputStreamProcessor: InputStreamProcessor, returnEncoding: String = Constants.Encoding.UTF8, advancedOption: AdvancedOption? = null): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        sendRequestWithInputStream(httpUrl, httpHeaderList, inputStream, timeout, inputStreamProcessor, advancedOption, object : Callback {
            @Throws(Exception::class)
            override fun httpOkCallback(headerFieldMap: Map<String, List<String>>, inputStream: InputStream, contentLength: Int) {
                val buffer = ByteArray(Constants.Capacity.BYTES_PER_KB)
                var dataLength = inputStream.read(buffer, 0, buffer.size)
                while (dataLength != -1) {
                    byteArrayOutputStream.write(buffer, 0, dataLength)
                    dataLength = inputStream.read(buffer, 0, buffer.size)
                }
                byteArrayOutputStream.close()
            }

            override fun exceptionCallback(exception: Exception) {
                logger.error(Constants.Base.EXCEPTION, exception)
            }

            @Throws(Exception::class)
            override fun httpNotOkCallback(responseCode: Int, headerFieldMap: Map<String, List<String>>) {
                logger.debug("Response code:$responseCode")
            }
        })
        val byteArray = byteArrayOutputStream.toByteArray()
        var result: String = Constants.String.BLANK
        if (byteArray.size > 0) {
            try {
                result = String(byteArray, Charset.forName(returnEncoding))
            } catch (e: UnsupportedEncodingException) {
                logger.error(Constants.Base.EXCEPTION, e)
            }

        }
        return result
    }

    /**
     * send request with input stream,most for upload
     *
     * @param httpUrl
     * @param httpHeaderList
     * @param inputStream
     * @param timeout
     * @param callback
     */
    fun sendRequestWithInputStream(httpUrl: String, httpHeaderList: List<HttpNameValue>, inputStream: InputStream, timeout: Int, advancedOption: AdvancedOption, callback: Callback) {
        sendRequestWithInputStream(httpUrl, httpHeaderList, inputStream, timeout, object : InputStreamProcessor {
            @Throws(Exception::class)
            override fun process(inputStream: InputStream, outputStream: OutputStream) {
                val buffer = ByteArray(Constants.Capacity.BYTES_PER_KB)
                var dataLength = inputStream.read(buffer, 0, buffer.size)
                while (dataLength != -1) {
                    outputStream.write(buffer, 0, dataLength)
                    outputStream.flush()
                    dataLength = inputStream.read(buffer, 0, buffer.size)
                }
            }
        }, advancedOption, callback)
    }

    /**
     * send request with input stream,most for upload
     *
     * @param httpUrl
     * @param httpHeaderList
     * @param inputStream
     * @param timeout
     * @param inputStreamProcessor
     * @param advancedOption
     * @param callback
     */
    fun sendRequestWithInputStream(httpUrl: String, httpHeaderList: List<HttpNameValue> = emptyList(), inputStream: InputStream, timeout: Int = DEFAULT_TIMEOUT, inputStreamProcessor: InputStreamProcessor? = null, advancedOption: AdvancedOption? = null, callback: Callback) {
        sendRequestPost(httpUrl, httpHeaderList, emptyList(), ByteArray(0), inputStream, timeout, inputStreamProcessor, advancedOption, callback)
    }

    /**
     * send request post
     *
     * @param httpUrl
     * @param httpHeaderList
     * @param httpParameterList
     * @param streamByteArray
     * @param inputStream
     * @param timeout
     * @param inputStreamProcessor
     * @param advancedOption
     * @param callback
     */
    private fun sendRequestPost(httpUrl: String, httpHeaderList: List<HttpNameValue> = emptyList(), httpParameterList: List<HttpNameValue> = emptyList(), streamByteArray: ByteArray = ByteArray(0), inputStream: InputStream? = null, timeout: Int = DEFAULT_TIMEOUT, inputStreamProcessor: InputStreamProcessor? = null, advancedOption: AdvancedOption?, callback: Callback) {
        sendRequest(httpUrl, Constants.Http.RequestMethod.POST, httpHeaderList, httpParameterList, streamByteArray, inputStream, timeout, inputStreamProcessor, advancedOption, callback)
    }

    /**
     * send request
     *
     * @param httpUrl
     * @param method
     * @param httpHeaderList
     * @param httpParameterList
     * @param streamByteArray
     * @param inputStream
     * @param timeout
     * @param inputStreamProcessor
     * @param advancedOption
     * @param callback
     */
    private fun sendRequest(httpUrl: String, method: String, httpHeaderList: List<HttpNameValue> = emptyList(), httpParameterList: List<HttpNameValue> = emptyList(), streamByteArray: ByteArray = ByteArray(0), inputStream: InputStream? = null, timeout: Int = DEFAULT_TIMEOUT, inputStreamProcessor: InputStreamProcessor? = null, advancedOption: AdvancedOption? = null, callback: Callback? = null) {
        try {
            val url = URL(httpUrl)
            var proxy = Proxy.NO_PROXY
            if (advancedOption != null && advancedOption.proxyHostname.isNotBlank() && advancedOption.proxyPort > 0) {
                val inetSocketAddress = InetSocketAddress(advancedOption.proxyHostname, advancedOption.proxyPort)
                proxy = Proxy(Proxy.Type.HTTP, inetSocketAddress)
            }
            val httpUrlConnection = url.openConnection(proxy) as HttpURLConnection
            httpUrlConnection.setDoOutput(true)
            httpUrlConnection.setDoInput(true)
            httpUrlConnection.setRequestMethod(method)
            httpUrlConnection.setUseCaches(false)
            httpUrlConnection.setInstanceFollowRedirects(true)
            httpUrlConnection.setConnectTimeout(timeout)
            httpUrlConnection.setReadTimeout(timeout)
            if (httpHeaderList.isNotEmpty()) {
                for (httpParameter in httpHeaderList) {
                    httpUrlConnection.setRequestProperty(httpParameter.name, httpParameter.value)
                }
            }
            val content = StringBuilder()
            if (httpParameterList.isNotEmpty()) {
                val length = httpParameterList.size
                var index = 0
                for (httpParameter in httpParameterList) {
                    content.append(httpParameter.name)
                    content.append(Constants.Symbol.EQUAL)
                    content.append(URLEncoder.encode(httpParameter.value, Constants.Encoding.UTF8))
                    if (index < length - 1) {
                        content.append(Constants.Symbol.AND)
                    }
                    index++
                }
            }
            httpUrlConnection.connect()
            if (method.isNotBlank() && method.equals(Constants.Http.RequestMethod.POST, ignoreCase = true)) {
                val outputStream = httpUrlConnection.getOutputStream()
                outputStream.write(content.toString().toByteArray(Charsets.UTF_8))
                if (streamByteArray.size > 0) {
                    outputStream.write(streamByteArray)
                    outputStream.flush()
                } else {
                    if (inputStreamProcessor != null && inputStream != null) {
                        inputStreamProcessor.process(inputStream, outputStream)
                    }
                }
                outputStream.close()
            }
            val responseCode = httpUrlConnection.getResponseCode()
            val headerFieldMap = httpUrlConnection.getHeaderFields() ?: emptyMap()
            if (responseCode == HttpURLConnection.HTTP_OK) {
                if (callback != null) {
                    val contentLength = httpUrlConnection.getContentLength()
                    val responseInputStream = httpUrlConnection.getInputStream()
                    try {
                        callback.httpOkCallback(headerFieldMap, responseInputStream, contentLength)
                    } catch (e: Exception) {
                        callback.exceptionCallback(e)
                    }

                    responseInputStream.close()
                }
            } else {
                if (callback != null) {
                    try {
                        callback.httpNotOkCallback(responseCode, headerFieldMap)
                    } catch (e: Exception) {
                        callback.exceptionCallback(e)
                    }

                }
            }
            httpUrlConnection.disconnect()
        } catch (e: Exception) {
            if (callback != null) {
                callback.exceptionCallback(e)
            }
        }

    }

    interface InputStreamProcessor {
        /**
         * process
         *
         * @param inputStream
         * @param outputStream
         * @throws Exception
         */
        @Throws(Exception::class)
        fun process(inputStream: InputStream, outputStream: OutputStream)
    }

    interface Callback {
        /**
         * http ok callback only for http status 200
         *
         * @param headerFieldMap
         * @param inputStream
         * @param contentLength
         * @throws Exception
         */
        @Throws(Exception::class)
        fun httpOkCallback(headerFieldMap: Map<String, List<String>>, inputStream: InputStream, contentLength: Int)

        /**
         * request process throw exception callback like timeout unknownhost and
         * so on
         *
         * @param exception
         */
        fun exceptionCallback(exception: Exception)

        /**
         * http not ok callback
         *
         * @param responseCode
         * @param headerFieldMap
         * @throws Exception
         */
        @Throws(Exception::class)
        fun httpNotOkCallback(responseCode: Int, headerFieldMap: Map<String, List<String>>)
    }

    class HttpNameValue(var name: String, var value: String)

    class AdvancedOption {
        var proxyHostname: String = Constants.String.BLANK
        var proxyPort = 0
    }
}