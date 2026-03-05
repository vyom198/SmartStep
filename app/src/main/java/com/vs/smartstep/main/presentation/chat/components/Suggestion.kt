package com.vs.smartstep.main.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vs.smartstep.core.theme.StrokeMain
import com.vs.smartstep.core.theme.bodyLargeMedium


@Composable
fun Suggestions(
    isEnabled : Boolean,
    onSuggestionClick: (String) -> Unit ,

) {

    Column(
        modifier = Modifier.fillMaxWidth().height(
            160.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SuggestionItem(
            title = "Recommend workout",
            isEnabled,
            onClick = {
                onSuggestionClick("Recommend workout")
            }
        )
        SuggestionItem(
            title = "Explain today’s trend",
            isEnabled,
            onClick = {
                onSuggestionClick("Explain today’s trend")
            }
        )
        SuggestionItem(
            title = "How to reach today’s goal ?",
            isEnabled,
            onClick = {
                onSuggestionClick("How to reach today’s goal ?")
            }
        )

    }

}

@Composable
fun SuggestionItem(
    title : String,
    isEnabled : Boolean,
    onClick : () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().height(48.dp).clip(
            RoundedCornerShape(10.dp)
        ).border(
            width = 1.dp,
            shape = RoundedCornerShape(10.dp),
            color = StrokeMain,
        ).background(
            color = MaterialTheme.colorScheme.surface
        ).clickable{
          if(isEnabled){
              onClick()
          }
        }.padding(
            horizontal = 16.dp
        ),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLargeMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}