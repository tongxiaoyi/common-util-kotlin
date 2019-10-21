package com.oneliang.ktx.util.state

import com.oneliang.ktx.util.logging.LoggerManager

class StateMachine<T : State>(private val stateMap: StateMap<T>) {

    companion object {
        private val logger = LoggerManager.getLogger(StateMachine::class)
    }

    /**
     * @return the currentState
     */
    var currentState: T? = null
        private set

    /**
     * get previous state key set
     *
     * @return Set<Integer>
    </Integer> */
    val previousStateKeySet: Set<Int>
        get() {
            if (currentState == null) {
                logger.error("current state is null, so doesn't have previous state key set")
                return emptySet()
            }
            return this.currentState!!.previousKeySet
        }

    /**
     * get next state key set
     *
     * @return Set<Integer>
    </Integer> */
    val nextStateKeySet: Set<Int>
        get() {
            if (currentState == null) {
                logger.error("current state is null, so doesn't have next state key set")
                return emptySet()
            }
            return this.currentState!!.nextKeySet
        }

    init {
        this.currentState = this.stateMap.startState
    }

    /**
     * has previous state
     *
     * @return boolean
     */
    fun hasPreviousState(): Boolean {
        if (currentState == null) {
            logger.error("current state is null, so doesn't have previous state")
            return false
        }
        return this.currentState!!.hasPrevious()
    }

    /**
     * previous state
     *
     * @param key
     * @throws StateNotFoundException
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(StateNotFoundException::class)
    fun previousState(key: Int) {
        if (currentState == null) {
            logger.error("current state is null")
            return
        }
        this.currentState = this.currentState!!.previous(key) as T
    }

    /**
     * has next state
     *
     * @return boolean
     */
    fun hasNextState(): Boolean {
        if (currentState == null) {
            logger.error("current state is null, so doesn't have next state")
            return false
        }
        return this.currentState!!.hasNext()
    }

    /**
     * next state
     *
     * @param key
     * @throws StateNotFoundException
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(StateNotFoundException::class)
    fun nextState(key: Int) {
        if (currentState == null) {
            logger.error("current state is null")
            return
        }
        this.currentState = this.currentState!!.next(key) as T
    }
}
