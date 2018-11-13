package com.oneliang.ktx.util.common

import com.oneliang.ktx.Constants
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NamedNodeMap
import java.io.*
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.reflect.KClass

object JavaXmlUtil {

    private val DEFAULT_CLASS_PROCESSOR = KClassUtil.DEFAULT_CLASS_PROCESSOR

    /**
     * get document builder
     * @return DocumentBuilder
     */
    private val documentBuilder: DocumentBuilder
        @Throws(Exception::class)
        get() {
            val documentBuilder: DocumentBuilder
            val documentBuilderFactory = DocumentBuilderFactory.newInstance()
            try {
                documentBuilder = documentBuilderFactory.newDocumentBuilder()
            } catch (e: Exception) {
                throw JavaXmlUtilException(e)
            }

            return documentBuilder
        }

    val emptyDocument: Document
        @Throws(Exception::class)
        get() {
            val document: Document
            try {
                val documentBuilder = documentBuilder
                document = documentBuilder.newDocument()
                document.normalize()
            } catch (e: Exception) {
                throw JavaXmlUtilException(e)
            }

            return document
        }

    /**
     * parse
     * @param filename
     * @return Document
     */
    @Throws(Exception::class)
    fun parse(filename: String): Document {
        val document: Document
        try {
            val documentBuilder = documentBuilder
            document = documentBuilder.parse(File(filename))
            document.normalize()
        } catch (e: Exception) {
            throw JavaXmlUtilException(e)
        }

        return document
    }

    /**
     * parse
     * @param inputStream
     * @return Document
     */
    @Throws(Exception::class)
    fun parse(inputStream: InputStream): Document {
        val document: Document
        try {
            val documentBuilder = documentBuilder
            document = documentBuilder.parse(inputStream)
            document.normalize()
        } catch (e: Exception) {
            throw JavaXmlUtilException(e)
        }

        return document
    }

    /**
     * save document
     * @param document
     * @param outputFullFilename
     */
    @Throws(Exception::class)
    fun saveDocument(document: Document, outputFullFilename: String) {
        var outputStream: OutputStream? = null
        try {
            val transformerFactory = TransformerFactory.newInstance()
            val transformer = transformerFactory.newTransformer()
            val domSource = DOMSource(document)
            transformer.setOutputProperty(OutputKeys.ENCODING, Constants.Encoding.UTF8)
            outputStream = FileOutputStream(outputFullFilename)
            val result = StreamResult(outputStream)
            transformer.transform(domSource, result)
        } catch (e: Exception) {
            throw JavaXmlUtilException(e)
        } finally {
            try {
                outputStream?.close()
            } catch (e: Exception) {
                throw JavaXmlUtilException(e)
            }
        }
    }

    /**
     * initialize from attribute map
     * @param object
     * @param namedNodeMap
     * @param classProcessor
     */
    @Throws(Exception::class)
    fun initializeFromAttributeMap(objectValue: Any, namedNodeMap: NamedNodeMap, classProcessor: KClassUtil.KClassProcessor = DEFAULT_CLASS_PROCESSOR) {
        val methods = objectValue.javaClass.methods
        for (method in methods) {
            val methodName = method.name
            val fieldName = if (methodName.startsWith(Constants.Method.PREFIX_SET)) {
                ObjectUtil.methodNameToFieldName(Constants.Method.PREFIX_SET, methodName)
            } else {
                Constants.String.BLANK
            }
            if (fieldName.isNotBlank()) {
                val node = namedNodeMap.getNamedItem(fieldName)
                if (node != null) {
                    val classes = method.parameterTypes
                    if (classes.size == 1) {
                        val objectClass = classes[0].kotlin
                        val attributeValue = node.nodeValue
                        val value = KClassUtil.changeType(objectClass, arrayOf(attributeValue), classProcessor = classProcessor)
                        try {
                            method.invoke(objectValue, value)
                        } catch (e: Exception) {
                            throw JavaXmlUtilException(e)
                        }
                    }
                }
            }
        }
    }

    /**
     * xml to list
     * @param <T>
     * @param xml
     * @param xmlObjectTag
     * @param clazz
     * @param mapping
     * @return List<T>
    </T></T> */
    @Throws(Exception::class)
    fun <T : Any> xmlToList(xml: String, xmlObjectTag: String, clazz: KClass<T>, mapping: Map<String, String>): List<T> {
        val list = ArrayList<T>()
        try {
            val inputStream = ByteArrayInputStream(xml.toByteArray(Charsets.UTF_8))
            val document = JavaXmlUtil.parse(inputStream)
            val root = document.documentElement
            val nodeList = root.getElementsByTagName(xmlObjectTag)
            for (i in 0 until nodeList.length) {
                val node = nodeList.item(i)
                list.add(xmlToObject(node as Element, clazz, mapping))
            }
        } catch (e: Exception) {
            throw JavaXmlUtilException(e)
        }
        return list
    }

    /**
     * xml to object
     * @param <T>
     * @param element
     * @param clazz
     * @param mapping
     * @return <T>
    </T></T> */
    @Throws(Exception::class)
    private fun <T : Any> xmlToObject(element: Element, clazz: KClass<T>, mapping: Map<String, String>): T {
        val objectValue: T
        try {
            objectValue = clazz.java.newInstance()
            val methods = clazz.java.methods
            for (method in methods) {
                val methodName = method.name
                val classes = method.parameterTypes
                if (!methodName.startsWith(Constants.Method.PREFIX_SET)) {
                    continue
                }
                val fieldName = ObjectUtil.methodNameToFieldName(Constants.Method.PREFIX_SET, methodName)
                if (fieldName.isBlank()) {
                    continue
                }
                val xmlTagName = mapping[fieldName] ?: continue
                val nodeList = element.getElementsByTagName(xmlTagName)
                if (nodeList != null && nodeList.length > 0) {
                    val node = nodeList.item(0)
                    val xmlTagValue = node.textContent.trim()
                    if (classes.size == 1) {
                        val value = KClassUtil.changeType(classes[0].kotlin, arrayOf<String>(xmlTagValue), classProcessor = DEFAULT_CLASS_PROCESSOR)
                        method.invoke(objectValue, value)
                    }
                }
            }
        } catch (e: Exception) {
            throw JavaXmlUtilException(e)
        }

        return objectValue
    }

    /**
     * xml to object
     * @param <T>
     * @param xml
     * @param clazz
     * @param mapping
     * @return <T>
    </T></T> */
    @Throws(Exception::class)
    fun <T : Any> xmlToObject(xml: String, clazz: KClass<T>, mapping: Map<String, String>): T {
        val objectValue: T
        try {
            val inputStream = ByteArrayInputStream(xml.toByteArray(Charsets.UTF_8))
            val document = JavaXmlUtil.parse(inputStream)
            val root = document.documentElement
            objectValue = xmlToObject(root, clazz, mapping)
        } catch (e: Exception) {
            throw JavaXmlUtilException(e)
        }

        return objectValue
    }

    class JavaXmlUtilException(cause: Throwable) : Exception(cause)
}
