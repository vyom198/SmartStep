package com.vs.smartstep.main.presentation.myprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vs.smartstep.core.theme.BackgroundSecondary
import com.vs.smartstep.core.theme.BackgroundTertiary
import com.vs.smartstep.core.theme.TextSecondary
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.core.theme.bodyMediumRegular
import com.vs.smartstep.core.theme.title_Medium
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.roundToInt


@Composable
fun WeightPickerDialog(
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onOk: (Double, Int) -> Unit,
    weight : Int,
    selectedIndex : Int = 0
) {
    var selectedIndex by retain { mutableIntStateOf(selectedIndex) }
    var selectedWeight by retain { mutableIntStateOf(if(selectedIndex == 0) weight else (weight/2.20462).toInt()) }
    var selectedWeightInlbs by retain { mutableIntStateOf(weight) }
    val listState = rememberLazyListState()

    val weightKgs = remember { (10..200).toList() }
    val weightlbs  = remember{
        (10..200)
            .map { kg ->
                (kg * 2.20462).toInt()
            }
    }
    // Update ft/in when cm changes
    fun updateFromkg(kg: Double) {
         selectedWeight = kg.roundToInt()
         val lbs = (kg * 2.20462).roundToInt()
         selectedWeightInlbs = lbs
    }

    LaunchedEffect( selectedIndex) {
        if (selectedIndex == 0 && selectedWeight > 0) {
            // Scroll in cm view
            val index = weightKgs.indexOfFirst { abs(it - selectedWeight) < 0.1 }
            if (index >= 0) {
                listState.scrollToItem(index )
            }
        } else if (selectedIndex == 1 && selectedWeightInlbs > 0) {
            // Scroll in ft/in view
            val index = weightlbs.indexOfFirst { weight ->
                weight == selectedWeightInlbs
            }
            if (index >= 0) {
                listState.scrollToItem(index)
            }
        }
    }
    fun updateFromlbs(lbs : Int) {
          selectedWeightInlbs = lbs
          selectedWeight = (lbs / 2.20462).roundToInt()
    }

    fun onUnitChange(newIndex: Int) {
        selectedIndex = newIndex

    }


    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .width(328.dp)
                .height(418.dp).clip(
                    RoundedCornerShape(28.dp)
                ).background(
                    color = BackgroundSecondary
                )
                .padding(24.dp),

            ) {
            Text(
                text = "Weight",
                style = MaterialTheme.typography.title_Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Used to calculate calories",
                style = MaterialTheme.typography.bodyMediumRegular,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(24.dp))
            SingleChoiceSegmentedButton(
                selectedIndex = selectedIndex,
                onSelectionChange = { onUnitChange(it) },
                options =  listOf("kg", "lbs")
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth().height(176.dp)
            ) {
                if(selectedIndex == 0){
                    items(weightKgs) { number ->
                        val isSelected = selectedWeight == number
                        Row (
                            modifier = Modifier.fillMaxWidth().height(48.dp).background(
                                color = if( isSelected) BackgroundTertiary else Color.Transparent
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center


                        ){
                            Text(
                                text = number.toString(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.title_Medium,
                                color = if(isSelected) MaterialTheme.colorScheme.onSurface else TextSecondary,
                                modifier = Modifier.clickable{
                                   updateFromkg(number.toDouble())
                                }
                            )
                        }

                    }
                }else{
                    items(weightlbs) { number->

                        val isSelected = selectedWeightInlbs == number

                        Row(
                            modifier = Modifier.fillMaxWidth().height(48.dp ).background(
                                color = if(  isSelected) BackgroundTertiary else Color.Transparent
                            ).clickable{
                                updateFromlbs(number)
                            },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${number.toInt()}",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.title_Medium,
                                color = if(isSelected)MaterialTheme.colorScheme.onSurface else TextSecondary,
                            )

                        }

                    }

                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                TextButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = onCancel

                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                TextButton(
                    modifier = Modifier.width(54.dp).height(44.dp),
                    onClick = {
                            val valueToReturn = when (selectedIndex) {
                                0 -> selectedWeight
                                else -> selectedWeightInlbs
                            }
                        onOk(valueToReturn.toDouble(), selectedIndex)
                    }

                ) {
                    Text(
                        text = "Ok",
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

    }

}