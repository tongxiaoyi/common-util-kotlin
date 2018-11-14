package com.oneliang.ktx.util.common

import com.oneliang.ktx.Constants
import java.util.*
import kotlin.reflect.KClass

object KotlinClassUtil {

    private val classTypeMap = mutableMapOf<KClass<*>, KClassType>()

    private val baseClassMap = mutableMapOf<KClass<*>, KClass<*>>()
    private val simpleClassMap = mutableMapOf<KClass<*>, KClass<*>>()
    private val baseArrayMap = mutableMapOf<KClass<*>, KClass<*>>()
    private val simpleArrayMap = mutableMapOf<KClass<*>, KClass<*>>()
    private val baseClassNameMap = mutableMapOf<String, KClass<*>>()
    private val simpleClassNameMap = mutableMapOf<String, KClass<*>>()

    val DEFAULT_KOTLIN_CLASS_PROCESSOR: KotlinClassProcessor = DefaultKotlinClassProcessor()

    enum class KClassType {
        KOTLIN_STRING, KOTLIN_CHARACTER, KOTLIN_SHORT, KOTLIN_INTEGER, KOTLIN_LONG,
        KOTLIN_FLOAT, KOTLIN_DOUBLE, KOTLIN_BOOLEAN, KOTLIN_BYTE, JAVA_UTIL_DATE,
        //        CHAR, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BOOLEAN,
        KOTLIN_STRING_ARRAY,
        KOTLIN_CHAR_ARRAY, KOTLIN_SHORT_ARRAY, KOTLIN_INT_ARRAY, KOTLIN_LONG_ARRAY,
        KOTLIN_FLOAT_ARRAY, KOTLIN_DOUBLE_ARRAY, KOTLIN_BOOLEAN_ARRAY, KOTLIN_BYTE_ARRAY,
        CHAR_ARRAY, BYTE_ARRAY, SHORT_ARRAY, INT_ARRAY, LONG_ARRAY, FLOAT_ARRAY, DOUBLE_ARRAY, BOOLEAN_ARRAY
    }

    init {
        classTypeMap[String::class] = KClassType.KOTLIN_STRING
        classTypeMap[Char::class] = KClassType.KOTLIN_CHARACTER
        classTypeMap[Short::class] = KClassType.KOTLIN_SHORT
        classTypeMap[Int::class] = KClassType.KOTLIN_INTEGER
        classTypeMap[Long::class] = KClassType.KOTLIN_LONG
        classTypeMap[Float::class] = KClassType.KOTLIN_FLOAT
        classTypeMap[Double::class] = KClassType.KOTLIN_DOUBLE
        classTypeMap[Boolean::class] = KClassType.KOTLIN_BOOLEAN
        classTypeMap[Byte::class] = KClassType.KOTLIN_BYTE
        classTypeMap[Date::class] = KClassType.JAVA_UTIL_DATE

        classTypeMap[Array<String>::class] = KClassType.KOTLIN_STRING_ARRAY
        classTypeMap[Array<Char>::class] = KClassType.KOTLIN_CHAR_ARRAY
        classTypeMap[Array<Short>::class] = KClassType.KOTLIN_SHORT_ARRAY
        classTypeMap[Array<Int>::class] = KClassType.KOTLIN_INT_ARRAY
        classTypeMap[Array<Long>::class] = KClassType.KOTLIN_LONG_ARRAY
        classTypeMap[Array<Float>::class] = KClassType.KOTLIN_FLOAT_ARRAY
        classTypeMap[Array<Double>::class] = KClassType.KOTLIN_DOUBLE_ARRAY
        classTypeMap[Array<Boolean>::class] = KClassType.KOTLIN_BOOLEAN_ARRAY
        classTypeMap[Array<Byte>::class] = KClassType.KOTLIN_BYTE_ARRAY
        classTypeMap[CharArray::class] = KClassType.CHAR_ARRAY
        classTypeMap[ByteArray::class] = KClassType.BYTE_ARRAY
        classTypeMap[ShortArray::class] = KClassType.SHORT_ARRAY
        classTypeMap[IntArray::class] = KClassType.INT_ARRAY
        classTypeMap[LongArray::class] = KClassType.LONG_ARRAY
        classTypeMap[FloatArray::class] = KClassType.FLOAT_ARRAY
        classTypeMap[DoubleArray::class] = KClassType.DOUBLE_ARRAY
        classTypeMap[BooleanArray::class] = KClassType.BOOLEAN_ARRAY

        simpleClassMap[String::class] = String::class
        simpleClassMap[Char::class] = Char::class
        simpleClassMap[Byte::class] = Byte::class
        simpleClassMap[Short::class] = Short::class
        simpleClassMap[Int::class] = Int::class
        simpleClassMap[Long::class] = Long::class
        simpleClassMap[Float::class] = Float::class
        simpleClassMap[Double::class] = Double::class
        simpleClassMap[Boolean::class] = Boolean::class

        baseArrayMap[CharArray::class] = CharArray::class
        baseArrayMap[ByteArray::class] = ByteArray::class
        baseArrayMap[ShortArray::class] = ShortArray::class
        baseArrayMap[IntArray::class] = IntArray::class
        baseArrayMap[LongArray::class] = LongArray::class
        baseArrayMap[FloatArray::class] = FloatArray::class
        baseArrayMap[DoubleArray::class] = DoubleArray::class
        baseArrayMap[BooleanArray::class] = BooleanArray::class

        simpleArrayMap[Array<String>::class] = Array<String>::class
        simpleArrayMap[Array<Char>::class] = Array<Char>::class
        simpleArrayMap[Array<Short>::class] = Array<Short>::class
        simpleArrayMap[Array<Int>::class] = Array<Int>::class
        simpleArrayMap[Array<Long>::class] = Array<Long>::class
        simpleArrayMap[Array<Float>::class] = Array<Float>::class
        simpleArrayMap[Array<Double>::class] = Array<Double>::class
        simpleArrayMap[Array<Boolean>::class] = Array<Boolean>::class
        simpleArrayMap[Array<Byte>::class] = Array<Byte>::class

        simpleClassNameMap[String::class.qualifiedName!!] = String::class
        simpleClassNameMap[Char::class.qualifiedName!!] = Char::class
        simpleClassNameMap[Byte::class.qualifiedName!!] = Byte::class
        simpleClassNameMap[Short::class.qualifiedName!!] = Short::class
        simpleClassNameMap[Int::class.qualifiedName!!] = Int::class
        simpleClassNameMap[Long::class.qualifiedName!!] = Long::class
        simpleClassNameMap[Float::class.qualifiedName!!] = Float::class
        simpleClassNameMap[Double::class.qualifiedName!!] = Double::class
        simpleClassNameMap[Boolean::class.qualifiedName!!] = Boolean::class
    }

    /**
     * get class with class name
     * @param className
     * @return Type
     * @throws Exception class not found
     */
    fun getClass(classLoader: ClassLoader, className: String): KClass<*>? {
        return if (baseClassNameMap.containsKey(className)) {
            baseClassNameMap[className]
        } else {
            classLoader.loadClass(className).kotlin
        }
    }

    /**
     * getClassType,for manual judge use
     * @param clazz
     * @return ClassType
     */
    fun getClassType(clazz: KClass<*>): KClassType? {
        return classTypeMap[clazz]
    }

    /**
     * is base class or not
     * include boolean short int long float double byte char
     * @param clazz
     * @return boolean
     */
    fun isBaseClass(clazz: KClass<*>): Boolean {
        return baseClassMap.containsKey(clazz)
    }

    /**
     * is base class or not
     * include boolean short int long float double byte char
     * @param className
     * @return boolean
     */
    fun isBaseClass(className: String): Boolean {
        return baseClassNameMap.containsKey(className)
    }

    /**
     * simple class or not
     * include Boolean Short Integer Long Float Double Byte String
     * @param clazz
     * @return boolean
     */
    fun isSimpleClass(clazz: KClass<*>): Boolean {
        return simpleClassMap.containsKey(clazz)
    }

    /**
     * simple class or not
     * include Boolean Short Integer Long Float Double Byte String
     * @param className
     * @return boolean
     */
    fun isSimpleClass(className: String): Boolean {
        return simpleClassNameMap.containsKey(className)
    }

    /**
     * basic array or not
     * @param clazz
     * @return boolean
     */
    fun isBaseArray(clazz: KClass<*>): Boolean {
        return baseArrayMap.containsKey(clazz)
    }

    /**
     * simple array or not
     * @param clazz
     * @return boolean
     */
    fun isSimpleArray(clazz: KClass<*>): Boolean {
        return simpleArrayMap.containsKey(clazz)
    }

    /**
     * change type width class processor
     * @param <T>
     * @param clazz
     * @param values
     * @param fieldName is null if not exist
     * @param classProcessor
     * @return Object
    </T> */
    fun <T : Any> changeType(clazz: KClass<T>, values: Array<String>, fieldName: String = Constants.String.BLANK, classProcessor: KotlinClassProcessor = DEFAULT_KOTLIN_CLASS_PROCESSOR): T? {
        return classProcessor.changeClassProcess(clazz, values, fieldName)
    }

    interface KotlinClassProcessor {

        /**
         * change class process
         * @param clazz
         * @param values
         * @param fieldName is null if not exist
         * @return Object
         */
        fun <T : Any> changeClassProcess(clazz: KClass<T>, values: Array<String>, fieldName: String): T?
    }
}
