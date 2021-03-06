package com.oneliang.ktx.util.concurrent

import com.oneliang.ktx.Constants
import com.oneliang.ktx.util.logging.LoggerManager
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * it will process all resources which are in queue when interrupt
 * ResourceQueueThread
 *
 * @param <T>
 */
class ResourceQueueThread<T : Any>(private val resourceProcessor: ResourceProcessor<T>) : Runnable {
    companion object {
        private val logger = LoggerManager.getLogger(ResourceQueueThread::class)
    }

    // addResource() may do before the start(),so must initialize it in self
// instance initializing
    private val resourceQueue = ConcurrentLinkedQueue<T>()
    private var thread: Thread? = null
    // always binding in self instance(ResourceQueueThread),finalize self
// instance will set null(release the resourceProcessor)
    private var needToInterrupt = false
    private val lock = Object()

    override fun run() {
        while (!Thread.currentThread().isInterrupted) {
            try {
                if (!this.resourceQueue.isEmpty()) {
                    val resource = this.resourceQueue.poll()
                    this.resourceProcessor.process(resource)
                } else {
                    synchronized(lock) {
                        // check for the scene which notify first,so do it in
// synchronized block
                        if (this.needToInterrupt) {
                            this.realInterrupt()
                        }
                        lock.wait()
                    }
                }
            } catch (e: InterruptedException) {
                logger.verbose("need to interrupt:" + e.message)
                Thread.currentThread().interrupt()
                break
            } catch (e: Exception) {
                logger.error(Constants.Base.EXCEPTION, e)
            }
        }
    }

    /**
     * start
     */
    @Synchronized
    fun start() {
        if (this.thread == null) {
            this.thread = Thread(this)
            this.thread?.start()
        }
    }

    /**
     * interrupt
     */
    fun interrupt() {
        this.needToInterrupt = true
        synchronized(lock) {
            lock.notify()
        }
    }

    /**
     * real interrupt
     */
    private fun realInterrupt() {
        if (this.thread != null) {
            this.thread?.interrupt()
            this.thread = null
            this.needToInterrupt = false
        }
    }

    /**
     * @param resource
     * the resource to add
     */
    fun addResource(resource: T?) {
        if (resource != null) {
            this.resourceQueue.add(resource)
            synchronized(lock) {
                lock.notify()
            }
        }
    }

    /**
     * remove resource
     *
     * @param resource
     */
    fun removeResource(resource: T): Boolean {
        return this.resourceQueue.remove(resource)
    }

    /**
     * finalize
     */
    @Throws(Throwable::class)
    protected fun finalize() {
        this.interrupt()
    }

    interface ResourceProcessor<T : Any> {
        /**
         * process the resource
         *
         * @param resource
         */
        fun process(resource: T)
    }
}