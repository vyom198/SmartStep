package com.vs.smartstep.main.domain.smartStep

import kotlinx.coroutines.flow.Flow

interface ConnectionProvider {
  val isConnected : Boolean

  val isConnectedFlow : Flow<Boolean>
}


