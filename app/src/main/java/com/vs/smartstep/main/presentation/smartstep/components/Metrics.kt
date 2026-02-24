package com.vs.smartstep.main.presentation.smartstep.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vs.smartstep.R
import com.vs.smartstep.core.theme.BackgroundWhite20
import com.vs.smartstep.core.theme.bodyMediumRegular
import com.vs.smartstep.core.theme.title_Medium


@Composable
fun MetricsComp(
    time : Int,
    kcal : Int,
    distance : Double,
    isMetric : Boolean,
    unit : String = if(isMetric) "km" else "mi"
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MetricItem(
            icon = R.drawable.pin__location__direction,
            value = distance,
            isDistance = true,
            unit = unit
        )
        Spacer(modifier = Modifier.weight(1f))
        MetricItem(
            icon = R.drawable.weight_diet,
            value = kcal.toDouble(),
            unit = "kcal"
        )
        Spacer(modifier = Modifier.weight(1f))
        MetricItem(
            icon = R.drawable.time_clock,
            value = time.toDouble(),
            unit = "min"
        )
    }
}

@Composable
fun MetricItem(
    icon: Int,
    value: Double,
    isDistance : Boolean = false ,
    unit: String
) {
    val realValue = if(isDistance) value else value.toInt()
    Column(
        modifier = Modifier
            .widthIn(49.dp)
            .height(79.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        IconButton(
            onClick = { },
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = BackgroundWhite20,
                    shape = RoundedCornerShape(8.dp)
                ),
            shape = RoundedCornerShape(8.dp),
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$realValue",
                style = MaterialTheme.typography.title_Medium,
                color = Color.White
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.bodyMediumRegular,
                color = MaterialTheme.colorScheme.secondary
            )
        }

    }
}