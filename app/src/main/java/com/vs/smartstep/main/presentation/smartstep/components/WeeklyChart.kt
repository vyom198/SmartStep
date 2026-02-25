package com.vs.smartstep.main.presentation.smartstep.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.vs.smartstep.core.theme.bodyMediumRegular
import com.vs.smartstep.core.theme.bodySmallRegular18
import com.vs.smartstep.core.theme.title_Medium
import com.vs.smartstep.main.presentation.smartstep.DailyActivityUI
import com.vs.smartstep.main.presentation.util.toCommaString

@Composable
fun WeeklyChart(
    list : List<DailyActivityUI>,
    avgSteps : Int
) {
    Column(
        modifier = Modifier.width(380.dp).height(153.dp).clip(
            RoundedCornerShape(28.dp)
        ).background(
            color = MaterialTheme.colorScheme.primary
        ).padding(
            16.dp
        )
    ){
        Text(
            text = "Daily Average: ${avgSteps.toCommaString()} steps",
            style = MaterialTheme.typography.title_Medium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth().height(80.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            list.forEach { item ->
                DailyItemUI(item)
            }
        }
    }


}

@Composable
fun DailyItemUI(item: DailyActivityUI) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
       StepRing(
           progress = (item.steps.toFloat()/item.stepGoal.toFloat()).coerceIn(0f,1f),
       )
        Text(
            text = item.day,
            style = MaterialTheme.typography.bodyMediumRegular,
            color = Color.White
        )
        Text(
            text = item.steps.toCommaString(),
            style = MaterialTheme.typography.bodySmallRegular18,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun StepRing(
    progress: Float,
    modifier: Modifier = Modifier.size(40.dp),
    strokeWidth: Float = 13f,
    activeColor: Color = Color(0xff0DC600),
    trackColor: Color = Color.White
) {
    Canvas(modifier = modifier) {
        drawCircle(
            color = trackColor,
            style = Stroke(width = strokeWidth)
        )

        drawArc(
            color = activeColor,
            startAngle = -90f,
            sweepAngle = 360f * progress,
            useCenter = false,
            style = Stroke(
                width = 8f,
                cap = StrokeCap.Round
            )
        )
    }
}