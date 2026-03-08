package com.vs.smartstep.data

import com.vs.smartstep.main.domain.smartStep.ConnectionProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeConnectionProvider : ConnectionProvider {
    private val _connectionFlow = MutableStateFlow(true)

    override var isConnected: Boolean = true
        get() = _connectionFlow.value
        set(value) {
            field = value
            _connectionFlow.value = value
        }

    override val isConnectedFlow: Flow<Boolean> = _connectionFlow
}