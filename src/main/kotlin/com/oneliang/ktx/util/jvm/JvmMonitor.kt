package com.oneliang.ktx.util.jvm

import com.oneliang.ktx.Constants
import com.oneliang.ktx.util.logging.LoggerManager

class JvmMonitor : Runnable {
    companion object {

        private val logger = LoggerManager.getLogger(JvmMonitor::class)
        private const val DEFAULT_PERCENT: Short = 85
        private var thread: Thread? = null
    }

    var percent: Short = 85
        set(value: Short) {
            field = if (value >= 100) {
                DEFAULT_PERCENT
            } else {
                value
            }
        }

    private var checkTime: Long = 5000

    constructor() {}

    constructor(percent: Short, checkTime: Long) {
        this.percent = percent
        this.checkTime = checkTime
    }

    @Synchronized
    fun start() {
        if (thread == null) {
            thread = Thread(this)
            thread!!.start()
        }
    }

    /**
     * interrupt
     */
    @Synchronized
    fun interrupt() {
        if (thread != null) {
            thread!!.interrupt()
            thread = null
        }
    }

    override fun run() {
        while (!Thread.currentThread().isInterrupted) {
            try {
                val heapSize = Runtime.getRuntime().totalMemory()
                val heapMaxSize = Runtime.getRuntime().maxMemory()
                //			    long heapFreeSize=Runtime.getRuntime().freeMemory();

                val heapSizeM = heapSize / Constants.Capacity.BYTES_PER_MB
                val heapMaxSizeM = heapMaxSize / Constants.Capacity.BYTES_PER_MB
                //			    long heapFreeSizeM=heapFreeSize/Constant.Capacity.BYTES_PER_MB;
                val currentPercent = (heapSizeM.toDouble() / heapMaxSizeM * 100).toShort()
                if (currentPercent >= this.percent) {
                    Runtime.getRuntime().gc()
                }
                logger.debug("jvm used percent:" + currentPercent + "%" + ",heap size:" + heapSizeM + "M,max:" + heapMaxSizeM + "M")
                Thread.sleep(this.checkTime)
            } catch (e: InterruptedException) {
                logger.debug("need to interrupt:" + e.message)
                Thread.currentThread().interrupt()
            } catch (e: Exception) {
                logger.error(Constants.Base.EXCEPTION, e)
            }

        }
    }

    /**
     * @param checkTime the checkTime to set
     */
    fun setCheckTime(checkTime: Long) {
        this.checkTime = checkTime
    }
}
