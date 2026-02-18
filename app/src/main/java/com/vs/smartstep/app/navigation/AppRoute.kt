package com.vs.smartstep.app.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute {
    @Serializable
    object Profile : AppRoute

    @Serializable
    object SmartStep : AppRoute

}