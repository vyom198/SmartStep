package com.vs.smartstep.main.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.vs.smartstep.R
import com.vs.smartstep.core.theme.StrokeMain
import com.vs.smartstep.core.theme.bodyMediumRegular
import com.vs.smartstep.main.presentation.chat.ChatMessage
import com.vs.smartstep.main.presentation.chat.Sender
import com.vs.smartstep.main.presentation.util.getWidthOfBubble

@Composable
fun ChatWindow(list  : List<ChatMessage> , modifier: Modifier = Modifier) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val scrollState = rememberLazyListState()
    LaunchedEffect(list.size) {
        if(list.isNotEmpty()){
            scrollState.animateScrollToItem(list.lastIndex)
        }

    }
    LazyColumn(
        state = scrollState,

        modifier = modifier.fillMaxSize().padding( 16.dp).semantics{
            testTag = "chat_window"
        },
        horizontalAlignment = if(windowSizeClass == WindowWidthSizeClass.COMPACT)
            Alignment.Start else Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(list) {
           ChatBubble(it)

        }
    }
}

@Composable
fun ChatBubble(item: ChatMessage) {
    when(item.sender){

        Sender.AI -> {
          AIBubble(item)
        }
        Sender.USER -> {
            UserBubble(item)

        }

    }

}
@Composable
fun AIBubble(item: ChatMessage) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    Row(
        modifier = Modifier.getWidthOfBubble(windowSizeClass, item.sender).wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier.size(32.dp).clip(
                CircleShape
            ).background(
                color = MaterialTheme.colorScheme.primary
            ),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(R.drawable.ai),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
        Column(
            modifier = Modifier.weight(1f).wrapContentHeight().clip(
                shape = RoundedCornerShape(
                    topStart = 6.dp,
                    topEnd = 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp,
                )
            ).border(
                width = 1.dp,
                shape = RoundedCornerShape(
                    topStart = 6.dp,
                    topEnd = 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp,
                ),
                color = StrokeMain

            ).background(
                color = Color.White
            ).padding(16.dp),

        ) {

            Text(
                text = item.content,
                style = MaterialTheme.typography.bodyMediumRegular,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
@Composable
fun UserBubble(item: ChatMessage) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
     Row(
         modifier = Modifier.getWidthOfBubble(windowSizeClass , Sender.AI),

     ) {
         Spacer(modifier = Modifier.weight(1f))
         Column(
             modifier = Modifier.getWidthOfBubble(windowSizeClass , item.sender).wrapContentHeight().clip(
                 shape = RoundedCornerShape(
                     topStart = 16.dp,
                     topEnd = 6.dp,
                     bottomStart = 16.dp,
                     bottomEnd = 16.dp,
                 )
             ).background(
                 color = MaterialTheme.colorScheme.primary
             ).padding(16.dp),

             ) {

             Text(
                 text = item.content,
                 style = MaterialTheme.typography.bodyMediumRegular,
                 color = Color.White
             )
         }
     }

}

