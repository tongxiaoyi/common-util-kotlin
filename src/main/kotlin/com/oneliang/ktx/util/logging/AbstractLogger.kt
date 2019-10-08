package com.oneliang.ktx.util.logging

import com.oneliang.ktx.Constants
import com.oneliang.ktx.util.common.nullToBlank
import com.oneliang.ktx.util.logging.Logger.Level


/**
 * constructor
 *
 * @param level
 */
abstract class AbstractLogger(val level: Level) : Logger {

    /**
     * verbose
     *
     * @param message
     * @param args
     */
    override fun verbose(message: String, vararg args: Any) {
        logByLevel(Level.VERBOSE, message, args = *args)
    }

    /**
     * debug
     *
     * @param message
     * @param args
     */
    override fun debug(message: String, vararg args: Any) {
        logByLevel(Level.DEBUG, message, args = *args)
    }

    /**
     * info
     *
     * @param message
     * @param args
     */
    override fun info(message: String, vararg args: Any) {
        logByLevel(Level.INFO, message, args = *args)
    }

    /**
     * warning
     *
     * @param message
     * @param args
     */
    override fun warning(message: String, vararg args: Any) {
        logByLevel(Level.WARNING, message, args = *args)
    }

    /**
     * error
     *
     * @param message
     * @param args
     */
    override fun error(message: String, vararg args: Any) {
        logByLevel(Level.ERROR, message, args = *args)
    }

    /**
     * error
     *
     * @param message
     * @param throwable
     * @param args
     */
    override fun error(message: String, throwable: Throwable, vararg args: Any) {
        logByLevel(Level.ERROR, message, throwable, args = *args)
    }

    /**
     * fatal
     *
     * @param message
     * @param args
     */
    override fun fatal(message: String, vararg args: Any) {
        logByLevel(Level.FATAL, message, args = *args)
    }

    /**
     * log by level
     *
     * @param level
     * @param message
     * @param throwable
     * @param args
     */
    private fun logByLevel(level: Level, message: String, throwable: Throwable? = null, vararg args: Any) {
        if (level.ordinal >= this.level.ordinal) {
            val extraInfo = ExtraInfo()
            val stackTraceArray = Thread.currentThread().stackTrace
            if (stackTraceArray.size > 4) {
                val stackTrace = stackTraceArray[4]
                val currentThread = Thread.currentThread()
                extraInfo.threadName = currentThread.name
                extraInfo.threadId = currentThread.id
                extraInfo.className = stackTrace.className
                extraInfo.methodName = stackTrace.methodName
                extraInfo.lineNumber = stackTrace.lineNumber
                extraInfo.filename = stackTrace.fileName.nullToBlank()
            }
            if (args.isEmpty()) {
                log(level, message, throwable, extraInfo)
            } else {
                log(level, message.format(args), throwable, extraInfo)
            }
        }
    }

    /**
     * log
     *
     * @param level
     * @param message
     * @param throwable
     * @param extraInfo
     */
    abstract fun log(level: Level, message: String, throwable: Throwable?, extraInfo: ExtraInfo)

    class ExtraInfo {
        var threadName = Constants.String.BLANK
        var threadId: Long = 0
        var className = Constants.String.BLANK
        var methodName = Constants.String.BLANK
        var lineNumber = 0
        var filename = Constants.String.BLANK
    }
}