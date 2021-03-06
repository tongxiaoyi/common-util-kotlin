package com.oneliang.ktx.util.json

import com.oneliang.ktx.util.common.DefaultKotlinClassProcessor
import com.oneliang.ktx.util.common.KotlinClassUtil
import kotlin.reflect.KClass

open class DefaultJsonKotlinClassProcessor : DefaultKotlinClassProcessor() {
    override fun <T : Any> changeClassProcess(kClass: KClass<T>, values: Array<String>, fieldName: String): Any? {
        val classType = KotlinClassUtil.getClassType(kClass)
        return if (classType != null) {
            super.changeClassProcess(kClass, values, fieldName)
        } else {
            if (values.isNotEmpty()) {
                if (kClass.java.isArray) {
                    val arrayComponentKClass = kClass.java.componentType::class
                    JsonUtil.jsonToObjectList(values[0], arrayComponentKClass, this)
                } else {
                    JsonUtil.jsonToObject(values[0], kClass, this)
                }
            } else {
                null
            }
        }
    }
}