package com.oneliang.ktx.util.common

import com.oneliang.ktx.Constants
import com.oneliang.ktx.util.logging.LoggerManager
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * reflect the object property and invoke the method
 *
 * @author Dandelion
 * @since 2008-04-??
 */
object ObjectUtil {

    private val logger = LoggerManager.getLogger(ObjectUtil::class.java)

    /**
     * field name to method name
     *
     * @param methodPrefix
     * @param fieldName
     * @param ignoreFirstLetterCase
     * @return methodName
     */
    @JvmOverloads
    fun fieldNameToMethodName(methodPrefix: String, fieldName: String, ignoreFirstLetterCase: Boolean = false): String {
        val methodName: String
        if (fieldName.isNotEmpty()) {
            if (ignoreFirstLetterCase) {
                methodName = methodPrefix + fieldName
            } else {
                methodName = methodPrefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)
            }
        } else {
            methodName = methodPrefix
        }
        return methodName
    }

    /**
     * method name to field name
     *
     * @param methodPrefix
     * @param methodName
     * @param ignoreFirstLetterCase
     * @return fieldName
     */
    @JvmOverloads
    fun methodNameToFieldName(methodPrefix: String, methodName: String, ignoreFirstLetterCase: Boolean = false): String {
        val fieldName: String
        if (methodName.length > methodPrefix.length) {
            val front = methodPrefix.length
            if (ignoreFirstLetterCase) {
                fieldName = methodName.substring(front, front + 1) + methodName.substring(front + 1)
            } else {
                fieldName = methodName.substring(front, front + 1).toLowerCase() + methodName.substring(front + 1)
            }
        } else {
            fieldName = Constants.String.BLANK
        }
        return fieldName
    }

    /**
     * get field with method name,which start with method prefix get or is,not
     * include method getClass()
     *
     * @param methodName
     * get or is method
     * @param ignoreFirstLetterCase
     * @return String
     */
    @JvmOverloads
    fun methodNameToFieldName(methodName: String, ignoreFirstLetterCase: Boolean = false): String {
        val fieldName: String
        if (methodName.startsWith(Constants.Method.PREFIX_GET) && methodName != Constants.Method.GET_CLASS) {
            fieldName = ObjectUtil.methodNameToFieldName(Constants.Method.PREFIX_GET, methodName, ignoreFirstLetterCase)
        } else if (methodName.startsWith(Constants.Method.PREFIX_IS)) {
            fieldName = ObjectUtil.methodNameToFieldName(Constants.Method.PREFIX_IS, methodName, ignoreFirstLetterCase)
        } else {
            fieldName = Constants.String.BLANK
        }
        return fieldName
    }

    /**
     * get class all interface list
     *
     * @param <T>
     * @param clazz
     * @return List<Class></Class>>
    </T> */
    private fun <T : Any> getClassAllInterfaceList(clazz: Class<T>): List<Class<*>> {
        return getClassAllSuperclassAndAllInterfaceList(false, false, true, clazz)
    }

    /**
     * get class all superclass and all interface list include self class.
     *
     * @param <T>
     * @param isIncludeSelfClass
     * @param isAllSuperclass
     * @param isAllInterface
     * @param clazz
     * @return List<Class></Class>>
    </T> */
    private fun <T : Any> getClassAllSuperclassAndAllInterfaceList(isIncludeSelfClass: Boolean, isAllSuperclass: Boolean, isAllInterface: Boolean, clazz: Class<T>): List<Class<*>> {
        val list = ArrayList<Class<*>>()
        val queue = ConcurrentLinkedQueue<Class<*>>()
        queue.add(clazz)
        if (isIncludeSelfClass) {
            list.add(clazz)
        }
        while (!queue.isEmpty()) {
            val currentClass = queue.poll()
            val superclass = currentClass.getSuperclass()
            if (superclass != null) {
                queue.add(superclass)
                if (isAllSuperclass) {
                    if (!list.contains(superclass)) {
                        list.add(superclass)
                    }
                }
            }
            if (isAllInterface) {
                val interfaces = currentClass.interfaces
                if (interfaces != null && interfaces.isNotEmpty()) {
                    for (interfaceClass in interfaces) {
                        queue.add(interfaceClass)
                        if (!list.contains(interfaceClass)) {
                            list.add(interfaceClass)
                        }
                    }
                }
            }
        }
        return list
    }

    /**
     * getClassAllInterfaces
     *
     * @param <T>
     * @param clazz
     * @return Class[]
    </T> */
    fun <T : Any> getClassAllInterfaces(clazz: Class<T>): Array<Class<*>> {
        val list = getClassAllInterfaceList(clazz)
        return list.toTypedArray()
    }

    /**
     * is interface implement
     *
     * @param implement
     * @param interfaceClass
     * @return boolean
     */
    fun isInterfaceImplement(implement: Class<*>, interfaceClass: Class<*>): Boolean {
        var result = false
        val list = getClassAllInterfaceList(implement)
        if (list.contains(interfaceClass)) {
            result = true
        }
        return result
    }

    /**
     * just objectClass is it the inheritance or interface implement of clazz
     *
     * @param objectClass
     * @param clazz
     * @return boolean
     */
    fun isInheritanceOrInterfaceImplement(objectClass: Class<*>, clazz: Class<*>): Boolean {
        var result = false
        val list = getClassAllSuperclassAndAllInterfaceList(true, true, true, objectClass)
        if (list.contains(clazz)) {
            result = true
        }
        return result
    }

    /**
     * judge the object is it the entity of class or interface
     *
     * @param object
     * @param clazz
     * @return boolean
     */
    fun isEntity(value: Any, clazz: Class<*>): Boolean {
        return isInheritanceOrInterfaceImplement(value.javaClass, clazz)
    }

    /**
     * invoke getter or is method for field
     *
     * @param object
     * @param fieldName
     * @param ignoreFirstLetterCase
     * @return Object
     */
    @JvmOverloads
    fun getterOrIsMethodInvoke(instance: Any, fieldName: String, ignoreFirstLetterCase: Boolean = false): Any {
        val value: Any
        var methodName = ObjectUtil.fieldNameToMethodName(Constants.Method.PREFIX_GET, fieldName, ignoreFirstLetterCase)
        var method: Method
        try {
            method = instance.javaClass.getMethod(methodName)
        } catch (e: Exception) {
            methodName = ObjectUtil.fieldNameToMethodName(Constants.Method.PREFIX_IS, fieldName, ignoreFirstLetterCase)
            try {
                method = instance.javaClass.getMethod(methodName)
            } catch (ex: Exception) {
                throw ObjectUtilException("No getter or is method for field:$fieldName", ex)
            }
        }

        try {
            value = method.invoke(instance, arrayOf<Any>())
        } catch (e: Exception) {
            throw ObjectUtilException(e)
        }

        return value
    }

    /**
     * read object
     *
     * @param inputStream
     * @return Object
     */
    fun readObject(inputStream: InputStream): Any? {
        var value: Any? = null
        var objectInputStream: ObjectInputStream? = null
        try {
            objectInputStream = ObjectInputStream(inputStream)
            value = objectInputStream.readObject()
        } catch (e: Exception) {
            logger.warning("Read exception:" + e.message)
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close()
                } catch (e: Exception) {
                    logger.warning("Read close exception:" + e.message)
                }

            }
        }
        return value
    }

    /**
     * write object
     *
     * @param serializable
     * @param outputStream
     */
    fun writeObject(serializable: java.io.Serializable, outputStream: OutputStream) {
        var objectOutputStream: ObjectOutputStream? = null
        try {
            objectOutputStream = ObjectOutputStream(outputStream)
            objectOutputStream.writeObject(serializable)
            objectOutputStream.flush()
        } catch (e: Exception) {
            logger.warning("Write exception:" + e.message)
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close()
                } catch (e: Exception) {
                    logger.warning("Write close exception:" + e.message)
                }

            }
        }
    }


    /**
     * new instance
     *
     * @param clazz
     * @param parameterTypes
     * @param parameterValues
     * @return T
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> newInstance(clazz: Class<*>, parameterTypes: Array<Class<*>>, parameterValues: Array<Any>): T {
        val value: T
        try {
            value = clazz.getConstructor(*parameterTypes).newInstance(*parameterValues) as T
        } catch (e: Exception) {
            throw ObjectUtilException(e)
        }

        return value
    }

    /**
     * method invoke
     *
     * @param object
     * @param methodName
     * @param parameterTypes
     * @param parameterValues
     * @return T
     */
    fun <T : Any> methodInvoke(instance: Any, methodName: String, parameterTypes: Array<Class<*>>, parameterValues: Array<Any>): T? {
        return methodInvoke(instance.javaClass, instance, methodName, parameterTypes, parameterValues)
    }

    /**
     * method invoke
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @param parameterValues
     * @return T
     */
    fun <T : Any> methodInvoke(clazz: Class<*>, methodName: String, parameterTypes: Array<Class<*>>, parameterValues: Array<Any>): T? {
        return methodInvoke(clazz, null, methodName, parameterTypes, parameterValues)
    }

    /**
     * method invoke
     * @param clazz
     * @param object
     * @param methodName
     * @param parameterTypes
     * @param parameterValues
     * @return T
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> methodInvoke(clazz: Class<*>, instance: Any?, methodName: String, parameterTypes: Array<Class<*>>, parameterValues: Array<Any>): T? {
        val value: T?
        try {
            value = clazz.getMethod(methodName, *parameterTypes).invoke(instance, *parameterValues) as T
        } catch (e: Exception) {
            throw ObjectUtilException(e)
        }

        return value
    }

    class ObjectUtilException : RuntimeException {
        constructor(message: String, cause: Throwable) : super(message, cause)
        constructor(cause: Throwable) : super(cause)
        constructor(message: String) : super(message)
    }
}