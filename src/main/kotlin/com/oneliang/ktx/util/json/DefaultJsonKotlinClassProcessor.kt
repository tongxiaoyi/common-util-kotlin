package com.oneliang.ktx.util.json

import com.oneliang.ktx.util.common.DefaultKotlinClassProcessor
import com.oneliang.ktx.util.common.KotlinClassUtil
import kotlin.reflect.KClass

open class DefaultJsonKotlinClassProcessor : DefaultKotlinClassProcessor() {
    override fun <T : Any> changeClassProcess(clazz: KClass<T>, values: Array<String>, fieldName: String): Any? {
        val classType = KotlinClassUtil.getClassType(clazz)
        return if (classType != null) {
            super.changeClassProcess(clazz, values, fieldName)
        } else {
            if (values.isNotEmpty()) {
                JsonUtil.jsonToObject(values[0], clazz, this)
            } else {
                null
            }
        }
    }
}