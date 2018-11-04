package com.oneliang.ktx.util.logging

import kotlin.collections.Map.Entry
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListSet
import com.oneliang.ktx.util.common.matchPattern

/**
 * @author oneliang
 */
object LoggerManager {
    private val loggerMap = ConcurrentHashMap<Class<*>, Logger>()
    private val patternLoggerMap = ConcurrentHashMap<String, Logger>()
    private val loggerPatternSet = ConcurrentSkipListSet<String>()
    /**
     * get logger
     *
     * @param clazz
     * @return Logger
     */
    fun getLogger(clazz: Class<*>): Logger {
        var logger = loggerMap.get(clazz)
        if (logger == null) {
            val className = clazz.getName()
            for (patternKey in loggerPatternSet) {
                if (className.matchPattern(patternKey)) {
                    logger = patternLoggerMap.get(patternKey)
                    break
                }
            }
        }
        return if (logger == null) BaseLogger(Logger.Level.ERROR) else logger
    }

    /**
     * register logger
     *
     * @param clazz
     * @param logger
     */
    fun registerLogger(clazz: Class<*>, logger: Logger) {
        loggerMap.put(clazz, logger)
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
    fun unregisterLogger(clazz: Class<*>) {
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