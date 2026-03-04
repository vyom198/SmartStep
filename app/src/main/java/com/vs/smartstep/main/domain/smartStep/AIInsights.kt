package com.vs.smartstep.main.domain.smartStep

import kotlinx.coroutines.flow.Flow

interface AIInsights {
    fun getInsights(): Flow<String>
}