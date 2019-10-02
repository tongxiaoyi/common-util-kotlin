package com.oneliang.ktx.util.state

import com.oneliang.ktx.Constants
import com.oneliang.ktx.util.logging.LoggerManager
import java.util.*

open class State(val key: Int = 0, val name: String = Constants.String.BLANK) {
    companion object {
        private val logger = LoggerManager.getLogger(State::class)
    }

    private val previousStateMap = HashMap<Int, State>()
    private val nextStateMap = HashMap<Int, State>()

    /**
     * get previous key set
     *
     * @return Set<Integer>
    </Integer> */
    val previousKeySet: Set<Int>
        get() = this.previousStateMap.keys

    /**
     * get next key set
     *
     * @return Set<Integer>
    </Integer> */
    val nextKeySet: Set<Int>
        get() = this.nextStateMap.keys

    /**
     * add previous state
     *
     * @param state
     */
    private fun addPreviousState(state: State) {
        this.previousStateMap[state.key] = state
    }

    /**
     * add next state
     *
     * @param state
     */
    fun addNextState(state: State) {
        this.nextStateMap[state.key] = state
        state.addPreviousState(this)
    }

    /**
     * has previous
     *
     * @return boolean
     */
    fun hasPrevious(): Boolean {
        return this.previousStateMap.isNotEmpty()
    }

    /**
     * previous
     *
     * @param key
     * @return State
     * @throws StateNotFoundException
     */
    @Throws(StateNotFoundException::class)
    fun previous(key: Int): State {
        if (previousStateMap.containsKey(key)) {
            return previousStateMap[key]!!
        } else {
            logger.error("previous state key:%s is not exist", key)
            throw StateNotFoundException(String.format("previous state key:%s", key))
        }
    }

    /**
     * has next
     *
     * @return boolean
     */
    operator fun hasNext(): Boolean {
        return this.nextStateMap.isNotEmpty()
    }

    /**
     * next
     *
     * @param key
     * @return State
     * @throws StateNotFoundException
     */
    @Throws(StateNotFoundException::class)
    fun next(key: Int): State {
        if (nextStateMap.containsKey(key)) {
            return nextStateMap[key]!!
        } else {
            logger.error("next state key:%s is not exist", key)
            throw StateNotFoundException(String.format("next state key:%s", key))
        }
    }
}
