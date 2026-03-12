package com.vs.smartstep.main.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vs.smartstep.R
import com.vs.smartstep.core.theme.ButtonSecondary
import com.vs.smartstep.core.theme.SmartStepTheme
import com.vs.smartstep.core.theme.StrokeMain
import com.vs.smartstep.core.theme.TextSecondary
import com.vs.smartstep.core.theme.bodyLargeRegular
import com.vs.smartstep.core.theme.segmentedText
import com.vs.smartstep.core.theme.title_Medium
import com.vs.smartstep.main.presentation.chat.components.ChatWindow
import com.vs.smartstep.main.presentation.chat.components.Suggestions
import com.vs.smartstep.main.presentation.util.getWidthOfSuggestionBox
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatRoot(
    viewModel: ChatViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ChatScreen(
        state = state,
        onAction = viewModel::onAction,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    state: ChatState,
    onAction: (ChatAction) -> Unit,
    onBack: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface
            ),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "AI Coach",
                        style = MaterialTheme.typography.title_Medium,
                        color = segmentedText
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,

                    ),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color(0xff4A4459),
                        modifier = Modifier
                            .size(34.dp)
                            .clickable {
                                onBack()
                            }
                    )
                },
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
                .padding(paddingValues)

        ) {

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = StrokeMain
            )
            Column(modifier =Modifier.weight(1f)) {
                ChatWindow(list = state.messages)
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = StrokeMain
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = Color.White
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .getWidthOfSuggestionBox(windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass)
                        .wrapContentHeight()
                        .padding(16.dp),
                ) {
                    Row(
                        modifier = Modifier.wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Text(
                            text = "Quick suggestions",
                            style = MaterialTheme.typography.title_Medium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Icon(
                            painter = painterResource(if (state.isSuggestion) R.drawable.arrow_up else R.drawable.arrow_down),
                            contentDescription = null,
                            tint = Color(0xff4A4459),
                            modifier = Modifier
                                .size(34.dp)
                                .clickable {
                                    onAction(ChatAction.ToogleSuggestion)
                                }
                        )
                    }
                    if (state.isSuggestion) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Suggestions(
                            isEnabled = state.isInternetAvailable,
                            onSuggestionClick = {
                                onAction(ChatAction.SendMessage(it))
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)

                    ) {
                        OutlinedTextField(
                            value = state.query,
                            textStyle = MaterialTheme.typography.bodyLargeRegular,
                            onValueChange = {
                                onAction(ChatAction.ontextChange(it))

                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = StrokeMain,
                                disabledBorderColor = StrokeMain,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                            enabled = state.isInternetAvailable,
                            trailingIcon = {
                                if (!state.isInternetAvailable) {
                                    Icon(
                                        painter = painterResource(R.drawable.cloud_off),
                                        contentDescription = null,
                                        tint = Color.Unspecified,
                                        modifier = Modifier
                                            .size(24.dp)

                                    )
                                } else null

                            },
                            placeholder = {
                                Text(
                                    text = if (state.isInternetAvailable) stringResource(R.string.ask_me_anything) else stringResource(
                                        R.string.online_connection_required
                                    ),
                                    style = MaterialTheme.typography.bodyLargeRegular,
                                    color = TextSecondary
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.88f)
                                .onFocusChanged { focusState ->
                                    isFocused = focusState.isFocused && state.isInternetAvailable
                                }
                                .height(
                                    if (isFocused) 120.dp else 56.dp
                                ).testTag("input_field"),
                        )
                        IconButton(
                            onClick = {
                                onAction(ChatAction.SendMessage(state.query))
                                onAction(ChatAction.ontextChange(""))

                            },

                            enabled = state.isInternetAvailable,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(
                                    CircleShape
                                )
                                .background(
                                    color = if (state.isInternetAvailable) MaterialTheme.colorScheme.primary
                                    else ButtonSecondary
                                ).testTag("send")
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.send),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(28.dp)

                            )
                        }
                    }

                }
            }

        }
    }
}

//@PreviewScreenSizes
//@Composable
//private fun Preview() {
//    SmartStepTheme {
//        ChatScreen(
//            state = ChatState(
//                messages = listOf(
//                    ChatMessage(
//                        content = "Hello! I'm your AI fitness coach. I've noticed your activity levels are a bit lower than usual today. I'm here to help you get back on track and answer any questions you might have about your fitness journey.",
//                         sender = Sender.AI
//                        ),
//                    ChatMessage(
//                        sender = Sender.USER,
//                        content = "Hello , who are you ? "
//                    )
//                ),
//                query = "",
//                isSuggestion = true,
//                isInternetAvailable = true
//            ),
//            onAction = {},
//            onBack = {}
//        )
//    }
//}