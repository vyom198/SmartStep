package com.vs.smartstep.main.presentation.chat

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.vs.smartstep.app.MainActivity
import org.junit.Rule
import org.junit.Test

class ChatRootIntegrationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()
    @Test
    fun writeMessageDisplayedInChatWindow(){
        composeRule.onNodeWithContentDescription("more").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("input_field").performTextInput("Hello")
        composeRule.onNodeWithTag("send").performClick()
        composeRule.onNodeWithTag("chat_window").assertTextContains("Hello")

    }

}