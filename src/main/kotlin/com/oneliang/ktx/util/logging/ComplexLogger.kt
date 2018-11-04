package com.oneliang.ktx.util.logging

class ComplexLogger(level: Logger.Level, private val loggerList: List<AbstractLogger>) : AbstractLogger(level) {

    /**
     * log
     */
    override fun log(level: Logger.Level, message: Any, throwable: Throwable?) {
        for (logger in this.loggerList) {
            logger.log(level, message, throwable)
        }
    }

    override fun destroy() {
        for (logger in this.loggerList) {
            logger.destroy()
        }
    }
}