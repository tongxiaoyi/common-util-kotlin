package com.oneliang.ktx.util.logging

import com.oneliang.ktx.util.logging.Logger.Level


/**
 * constructor
 *
 * @param level
 */
abstract class AbstractLogger(private val level: Level) : Logger {

    /**
     * verbose
     *
     * @param message
     */
    override fun verbose(message: Any) {
        logByLevel(Level.VERBOSE, message)
    }

    /**
     * debug
     *
     * @param message
     */
    override fun debug(message: Any) {
        logByLevel(Level.DEBUG, message)
    }

    /**
     * info
     *
     * @param message
     */
    override fun info(message: Any) {
        logByLevel(Level.INFO, message)
    }

    /**
     * warning
     *
     * @param message
     */
    override fun warning(message: Any) {
        logByLevel(Level.WARNING, message)
    }

    /**
     * error
     *
     * @param message
     */
    override fun error(message: Any) {
        logByLevel(Level.ERROR, message, null)
    }

    /**
     * error
     *
     * @param message
     * @param throwable
     */
    override fun error(message: Any, throwable: Throwable) {
        logByLevel(Level.ERROR, message, throwable)
    }

    /**
     * fatal
     *
     * @param message
     */
    override fun fatal(message: Any) {
        logByLevel(Level.FATAL, message)
    }

    /**
     * log by level
     *
     * @param level
     * @param message
     * @param throwable
     */
    private fun logByLevel(level: Level, message: Any, throwable: Throwable? = null) {
        if (level.ordinal >= this.level.ordinal) {
            log(level, message, throwable)
        }
    }

    /**
     * log
     *
     * @param level
     * @param message
     * @param throwable
     */
    abstract fun log(level: Level, message: Any, throwable: Throwable?)
}