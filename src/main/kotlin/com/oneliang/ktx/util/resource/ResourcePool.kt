package com.oneliang.ktx.util.resource

import com.oneliang.ktx.Constants
import com.oneliang.ktx.util.logging.LoggerManager

/**
 * class Pool,resource pool
 *
 * @author Dandelion
 * @since 2011-09-19
 */
abstract class ResourcePool<T : Any> : Runnable {
    companion object {
        internal val logger = LoggerManager.getLogger(ResourcePool::class)
    }

    var resourcePoolName: String = Constants.String.BLANK
    var resourceSource: ResourceSource<T>? = null
    var minResources = 1
    var maxResources = 1
    var resourceAliveTime: Long = 0
    var threadSleepTime = 300000L
    var resourceStatusArray = emptyArray<ResourceStatus<T>?>()
    var currentSize = 0
    private var thread: Thread? = null
    private val lock = Any()

    /**
     * get resource from resource pool
     * @return T
     * @throws Exception
     */
    //true prove if have the resource which have not in use
    //current size > 0 prove the resource pool have the resource
    open val resource: T?
        @Throws(ResourcePoolException::class)
        get() {
            var resource: T? = null
            synchronized(this.lock) {
                if (this.currentSize > 0) {
                    for (resourceStatus in this.resourceStatusArray) {
                        if (resourceStatus == null || resourceStatus.isInUse) {
                            continue
                        }
                        resource = resourceStatus.resource
                        resourceStatus.isInUse = true
                        break
                    }
                }
                if (resource == null) {
                    if (this.currentSize < this.maxResources) {
                        var index = 0
                        for (resourceStatus in this.resourceStatusArray) {
                            if (resourceStatus != null) {
                                index++//next resource status
                                continue
                            }
                            resource = this.resourceSource!!.resource
                            if (resource != null) {
                                val oneStatus = ResourceStatus<T>()
                                oneStatus.resource = resource
                                oneStatus.isInUse = true
                                this.resourceStatusArray[index] = oneStatus
                                this.currentSize++
                            }
                            break
                        }
                    } else {
                        throw ResourcePoolException("The resource pool is max,current:" + this.currentSize)
                    }
                }
            }
            return resource
        }

    /**
     * initial
     */
    open fun initialize() {
        this.resourceStatusArray = arrayOfNulls<ResourceStatus<T>?>(this.maxResources)
        for (i in 0 until this.minResources) {
            val resource = this.resourceSource!!.resource ?: continue
            val resourceStatus = ResourceStatus<T>()
            resourceStatus.resource = resource
            this.resourceStatusArray[i] = resourceStatus
            this.currentSize++
        }
        this.thread = Thread(this).apply { start() }
    }

    /**
     * release resource to pool
     * @param resource
     */
    open fun releaseResource(resource: T?) {
        if (resource == null) {
            return
        }
        for (resourceStatus in this.resourceStatusArray) {
            if (resourceStatus == null) {
                continue
            }
            //find the resource and set in use false
            if (resource == resourceStatus.resource) {
                resourceStatus.isInUse = false
                resourceStatus.lastNotInUseTime = System.currentTimeMillis()
                break
            }
        }
    }

    /**
     * destroy resource
     * @param resource
     * @throws ResourcePoolException
     */
    @Throws(ResourcePoolException::class)
    protected abstract fun destroyResource(resource: T?)

    /**
     * clean the timeout resource
     * @throws Exception
     */
    open fun clean() {
        synchronized(this.lock) {
            for ((index, resourceStatus) in this.resourceStatusArray.withIndex()) {
                if (resourceStatus == null || resourceStatus.isInUse) {
                    continue
                }
                val lastTime = resourceStatus.lastNotInUseTime
                val currentTime = System.currentTimeMillis()
                val resource = resourceStatus.resource
                if (currentTime - lastTime >= this.resourceAliveTime) {
                    try {
                        destroyResource(resource)
                    } catch (e: Exception) {
                        logger.error(Constants.Base.EXCEPTION, e)
                    }
                    this.resourceStatusArray[index] = null
                    this.currentSize--
                }
            }
        }
    }

    /**
     * thread run
     */
    override fun run() {
        while (!Thread.currentThread().isInterrupted) {
            try {
                Thread.sleep(threadSleepTime)
                logger.debug("--" + thread!!.name + "--The resource pool is:'" + this.resourcePoolName + "',before clean resources number:" + this.currentSize.toString())
                this.clean()
                logger.debug("--" + thread!!.name + "--The resource pool is:'" + this.resourcePoolName + "',after clean resources number:" + this.currentSize.toString())
            } catch (e: InterruptedException) {
                logger.debug("need to interrupt:" + e.message)
                Thread.currentThread().interrupt()
            } catch (e: Exception) {
                logger.error(Constants.Base.EXCEPTION, e)
            }
        }
    }
}