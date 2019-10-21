package com.oneliang.ktx.util.state

import com.oneliang.ktx.util.logging.LoggerManager
import java.util.concurrent.ConcurrentHashMap

class StateMap<T : State>(val startState: T) {
    companion object {
        private val logger = LoggerManager.getLogger(StateMap::class)

        @Throws(Exception::class)
        private fun printState(startState: State) {
            logger.info("key:%s, name:%s", startState.key, startState.name)
            if (startState.hasNext()) {
                val nextStateKeySet = startState.nextKeySet
                for (nextKey in nextStateKeySet) {
                    val nextState = startState.next(nextKey)
                    printState(nextState)
                }
            }
        }
    }

    private val stateMap = ConcurrentHashMap<Int, T>()

    init {
        this.stateMap[this.startState.key] = this.startState
    }

    fun addNextState(key: Int, nextState: T) {
        if (stateMap.containsKey(key)) {
            val previousState = stateMap[key]!!
            previousState.addNextState(nextState)
            stateMap[nextState.key] = nextState
        } else {
            logger.error("state key:%s is not exist", key)
        }
    }

    fun printState() {
        try {
            printState(this.startState)
        } catch (e: Exception) {
            logger.error("state not found", e)
        }

    }
}
