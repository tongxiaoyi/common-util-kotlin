package com.oneliang.ktx.util.logging

import com.oneliang.ktx.util.common.matchPattern
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.reflect.KClass

/**
 * @author oneliang
 */
object LoggerManager {
    private val loggerMap = ConcurrentHashMap<KClass<*>, Logger>()
    private val patternLoggerMap = ConcurrentHashMap<String, Logger>()
    private val loggerPatternSet = ConcurrentSkipListSet<String>()
    /**
     * get logger
     *
     * @param clazz
     * @return Logger
     */
    fun getLogger(clazz: KClass<*>): Logger {
        var logger = loggerMap[clazz]
        if (logger == null) {
            val className = clazz.java.name
            for (patternKey in loggerPatternSet) {
                if (className.matchPattern(patternKey)) {
                    logger = patternLoggerMap.get(patternKey)
                    break
                }
            }
        }
        return logger ?: BaseLogger(Logger.Level.ERROR)
    }

    /**
     * register logger
     *
     * @param clazz
     * @param logger
     */
    fun registerLogger(clazz: KClass<*>, logger: Logger) {
        loggerMap[clazz] = logger
    }

    /**
     * register logger
     *
     * @param pattern
     * @param logger
     */
    fun registerLogger(pattern: String, logger: Logger) {
        patternLoggerMap.put(pattern, logger)
        loggerPatternSet.add(pattern)
    }

    /**
     * unregister logger
     *
     * @param clazz
     */
    fun unregisterLogger(clazz: KClass<*>) {
        loggerMap.remove(clazz)
    }

    /**
     * unregister logger
     *
     * @param pattern
     */
    fun unregisterLogger(pattern: String) {
        patternLoggerMap.remove(pattern)
        loggerPatternSet.remove(pattern)
    }

    /**
     * unregister all logger
     */
    fun unregisterAllLogger() {
        loggerMap.forEach { (_, logger) ->
            logger.destroy()
        }
        patternLoggerMap.forEach { (_, logger) ->
            logger.destroy()
        }
        loggerMap.clear()
        patternLoggerMap.clear()
        loggerPatternSet.clear()
    }
}