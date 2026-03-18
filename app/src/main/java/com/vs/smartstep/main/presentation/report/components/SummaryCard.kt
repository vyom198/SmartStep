package com.vs.smartstep.main.presentation.report.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.core.theme.bodyLargeRegular
import com.vs.smartstep.core.theme.bodyMediumRegular
import com.vs.smartstep.core.theme.title_Accent

@Composable
fun SummaryCard() {
    Card(
        modifier = Modifier
            .width(380.dp)
            .height(180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    24.dp
                ),

            ) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "steps",
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = Color.White
                )
                Text(
                    text = "This Week",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            Text(
                text = "573",
                style = MaterialTheme.typography.title_Accent,
                color = Color.White
            )
            Text(
                text = "Daily average: 191 steps",
                style = MaterialTheme.typography.bodyMediumRegular,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}