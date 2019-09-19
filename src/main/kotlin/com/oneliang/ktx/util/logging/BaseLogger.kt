package com.oneliang.ktx.util.logging

open class BaseLogger(level: Logger.Level) : AbstractLogger(level) {
    /**
     * log
     *
     * @param level
     * @param message
     * @param throwable
     */
    override fun log(level: Logger.Level, message: Any, throwable: Throwable?, extraInfo: ExtraInfo) {
        println(generateLogContent(level, message, throwable, extraInfo))
        throwable?.printStackTrace()
    }

    public override fun destroy() {}
}