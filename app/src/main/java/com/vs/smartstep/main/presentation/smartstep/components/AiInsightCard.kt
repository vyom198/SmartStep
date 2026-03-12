package com.vs.smartstep.main.presentation.smartstep.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vs.smartstep.R
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.core.theme.bodySmallRegular

@Composable
fun AIInsightCard(
    isConnected : Boolean,
    insights : String,
    onMoreClick : () -> Unit ,
    onReload: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(380.dp)
            .height(
                140.dp
            )
            .clip(
                RoundedCornerShape(24.dp)
            )
            .background(
                color = Color.White
            )
            .padding(
                16.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,

        ){
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.secondary
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ai_artificial_intelligence),
                    contentDescription = "AI Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)

                )

            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if(isConnected) stringResource(R.string.more) else stringResource(R.string.try_again),
                style = MaterialTheme.typography.bodyLargeMedium,
                color = MaterialTheme.colorScheme.primary

            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(if(isConnected) R.drawable.arrow_forward else R.drawable.reload),
                contentDescription = if(isConnected)"more" else "try_again",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp).clickable {
                    if(isConnected){
                        onMoreClick()
                    }else{
                        onReload()
                    }
                }

            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if(isConnected)insights else stringResource(R.string.connect_to_the_internet_to_get_ai_insights),
            maxLines = 2,
            style = MaterialTheme.typography.bodyLargeMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}