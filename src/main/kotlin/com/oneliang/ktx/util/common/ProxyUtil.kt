package com.oneliang.ktx.util.common

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

object ProxyUtil {

    /**
     *
     * Method: return the proxy interface of the interfaces of object
     * @param <T>
     * @param classLoader can not be null
     * @param object can not be null
     * @param handler can not be null
     * @return proxy interface
    </T> */
    fun <T : Any> newProxyInstance(classLoader: ClassLoader, instance: T, handler: InvocationHandler): Any {
        val interfaces = ObjectUtil.getClassAllInterfaces(instance.javaClass)
        return Proxy.newProxyInstance(classLoader, interfaces, handler)
    }
}
