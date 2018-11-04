package com.oneliang.ktx.util.logging

/**
 * @author oneliang
 */
interface Logger {
    enum class Level {
        VERBOSE, DEBUG, INFO, WARNING, ERROR, FATAL
    }

    /**
     * verbose
     *
     * @param message
     */
    fun verbose(message: Any)

    /**
     * debug
     *
     * @param message
     */
    fun debug(message: Any)

    /**
     * info
     *
     * @param message
     */
    fun info(message: Any)

    /**
     * warning
     *
     * @param message
     */
    fun warning(message: Any)

    /**
     * error
     *
     * @param message
     */
    fun error(message: Any)

    /**
     * error
     *
     * @param message
     * @param throwable
     */
    fun error(message: Any, throwable: Throwable)

    /**
     * fatal
     *
     * @param message
     */
    fun fatal(message: Any)

    /**
     * destroy
     */
    fun destroy()
}