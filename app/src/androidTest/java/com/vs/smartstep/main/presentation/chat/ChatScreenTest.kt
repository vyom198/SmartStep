package com.vs.smartstep.main.presentation.chat

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vs.smartstep.core.theme.SmartStepTheme
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class ChatScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testChatScreen() {
        composeRule.setContent {
            SmartStepTheme {
                ChatScreen(
                    state = ChatState(),
                    onAction = {},
                    onBack = {}
                )
            }
        }

        composeRule.onNodeWithText("AI Coach").assertIsDisplayed()

    }
}