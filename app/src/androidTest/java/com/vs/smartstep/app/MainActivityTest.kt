package com.vs.smartstep.app

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val activityRule = createAndroidComposeRule<MainActivity>()
    private val isProfileSetupFlow = MutableStateFlow(false)
    @Test
    fun activityLaunchesSuccessfully() {

    }

}