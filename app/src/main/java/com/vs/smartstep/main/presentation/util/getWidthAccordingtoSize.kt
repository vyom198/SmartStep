package com.vs.smartstep.main.presentation.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vs.smartstep.main.presentation.chat.Sender


fun Modifier.getWidthOfBubble(
    windowSizeClass: androidx.window.core.layout.WindowWidthSizeClass,
    sender: Sender
): Modifier{
    when(sender){
        Sender.USER -> {
            if(windowSizeClass == androidx.window.core.layout.WindowWidthSizeClass.COMPACT){
                return this.fillMaxWidth(0.75f)
            } else return  this.width(400.dp)

        }
        Sender.AI -> {
            if(windowSizeClass == androidx.window.core.layout.WindowWidthSizeClass.COMPACT){
                return this.fillMaxWidth()
            } else return  this.width(600.dp)
        }
    }
}

fun Modifier.getWidthOfSuggestionBox(
    windowSizeClass: androidx.window.core.layout.WindowWidthSizeClass,
): Modifier {


    if (windowSizeClass == androidx.window.core.layout.WindowWidthSizeClass.COMPACT) {
        return this.fillMaxWidth()
    } else return this.width(400.dp)


}