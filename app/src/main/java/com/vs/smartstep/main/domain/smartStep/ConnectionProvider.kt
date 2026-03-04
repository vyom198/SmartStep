package com.vs.smartstep.main.domain.smartStep

import kotlinx.coroutines.flow.Flow

interface ConnectionProvider {
  val isConnected : Flow<Boolean>
}