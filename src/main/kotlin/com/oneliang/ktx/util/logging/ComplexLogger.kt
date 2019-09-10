package com.oneliang.ktx.util.logging

class ComplexLogger(level: Logger.Level, private val loggerList: List<AbstractLogger>) : AbstractLogger(level) {

    /**
     * log
     */
    override fun log(level: Logger.Level, message: Any, throwable: Throwable?, extraInfo: ExtraInfo) {
        for (logger in this.loggerList) {
            if (level.ordinal >= logger.level.ordinal) {
                logger.log(level, message, throwable, extraInfo)
            }
        }
    }

    override fun destroy() {
        for (logger in this.loggerList) {
            logger.destroy()
        }
    }
}