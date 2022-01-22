package com.zy.jet.notebook.jetpack.repository

/**
 * 状态
 */
enum class State {
    RUNNING,
    SUCCESS,
    FAILED,
    HIDDEN
}

class NetworkState private constructor(state: State) {
    companion object {
        val LOADED = NetworkState(State.SUCCESS)
        val LOADING = NetworkState(State.RUNNING)
        val HIDDEN = NetworkState(State.HIDDEN)
        val FAILED = NetworkState(State.FAILED)
    }
}