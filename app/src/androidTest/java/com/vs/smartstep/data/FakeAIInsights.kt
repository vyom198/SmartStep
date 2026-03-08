package com.vs.smartstep.data

import com.vs.smartstep.main.domain.smartStep.AIInsights
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAIInsights : AIInsights {
    private val _insightsFlow = MutableStateFlow("Keep going!")
    
    override fun getInsights(): Flow<String> = _insightsFlow

    override suspend fun getInitialMessage(): String = "Hello! I'm your AI coach."

    override suspend fun chatWithAI(query: String): String = "You asked: $query. You're doing great!"

    // Helper for testing: change the insight mid-test
    fun emitInsight(newInsight: String) {
        _insightsFlow.value = newInsight
    }
}