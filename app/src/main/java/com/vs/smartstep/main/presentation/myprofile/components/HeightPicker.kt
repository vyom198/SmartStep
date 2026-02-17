package com.vs.smartstep.main.presentation.myprofile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vs.smartstep.R
import com.vs.smartstep.core.theme.BackgroundSecondary
import com.vs.smartstep.core.theme.BackgroundTertiary
import com.vs.smartstep.core.theme.ButtonSecondary
import com.vs.smartstep.core.theme.StrokeMain
import com.vs.smartstep.core.theme.TextSecondary
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.core.theme.bodyMediumMedium
import com.vs.smartstep.core.theme.bodyMediumRegular
import com.vs.smartstep.core.theme.segmentedText
import com.vs.smartstep.core.theme.title_Medium
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.round


@Composable
fun HeightPickerDialog(
    onDismissRequest: () -> Unit,
    onCancel : () -> Unit ,
    onOk : (Double, Int) -> Unit,
    selectedCm : Int,


) {
    var selectedIndex by retain { mutableIntStateOf(0) }
    var selectedCm by retain { mutableDoubleStateOf(selectedCm.toDouble()) }
    var selectedFeet by retain { mutableIntStateOf(5) }
    var selectedInches by retain { mutableIntStateOf(9) }
    val listState = rememberLazyListState()

    val cmValues = remember { (10..300).toList() }
    val uniqueHeights = remember{
        (10..300)
            .map { cm ->
                val totalInches = cm / 2.54
                var feet = floor(totalInches / 12).toInt()
                var inches = round(totalInches % 12).toInt()
                if (inches == 12) {
                    feet += 1
                    inches = 0
                }
                Pair(feet, inches)
            }
            .toSet()
            .sortedBy { (feet, inches) -> feet * 12 + inches }
    }
    // Update ft/in when cm changes
    fun updateFromCm(cm: Double) {
        selectedCm = cm
        val totalInches = cm / 2.54
        var feet = floor(totalInches / 12).toInt()
        var inches = round(totalInches % 12).toInt()
        if (inches == 12) {
            feet += 1
            inches = 0
        }
        selectedFeet = feet
        selectedInches = inches
    }

    LaunchedEffect( selectedIndex) {
        if (selectedIndex == 0 && selectedCm > 0) {
            // Scroll in cm view
            val index = cmValues.indexOfFirst { abs(it - selectedCm) < 0.1 }
            if (index >= 0) {
                listState.scrollToItem(index )
            }
        } else if (selectedIndex == 1 && (selectedFeet > 0 || selectedInches > 0)) {
            // Scroll in ft/in view
            val index = uniqueHeights.indexOfFirst { pair ->
                pair.first == selectedFeet && pair.second == selectedInches
            }
            if (index >= 0) {
                listState.scrollToItem(index)
            }
        }
    }
    fun updateFromFtIn(feet: Int, inches: Int) {
        selectedFeet = feet
        selectedInches = inches
        selectedCm = round((feet * 12 + inches) * 2.54)
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
                text = "Height",
                style = MaterialTheme.typography.title_Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Used to calculate distance",
                style = MaterialTheme.typography.bodyMediumRegular,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(24.dp))
            SingleChoiceSegmentedButton(
                selectedIndex = selectedIndex,
                onSelectionChange = { onUnitChange(it) },
                options =  listOf("cm", "ft/in")
            )
            Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxWidth().height(176.dp)
                ) {
                    if(selectedIndex == 0){
                        items(cmValues) { number ->
                            val isSelected = selectedCm == number.toDouble()
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
                                        updateFromCm(number.toDouble())
                                    }
                                )
                            }

                        }
                    }else{
                        items(uniqueHeights) { pair->
                            val (feet, inches) = pair
                            val isSelected = selectedFeet == feet && selectedInches == inches

                            Row(
                                modifier = Modifier.fillMaxWidth().height(48.dp ).background(
                                    color = if(  isSelected) BackgroundTertiary else Color.Transparent
                                ).clickable{
                                    updateFromFtIn(feet, inches)
                                },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "${feet}",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.title_Medium,
                                    color = if(isSelected)MaterialTheme.colorScheme.onSurface else TextSecondary,
                                )
                                Spacer(modifier = Modifier.width(70.dp))
                                if(isSelected){
                                    Text(
                                        text = "ft",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.title_Medium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                }else{
                                    Text(
                                        text = "ft",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.title_Medium,
                                        color = Color.Transparent,
                                    )
                                }

                                Spacer(modifier = Modifier.width(40.dp))
                                Text(
                                    text = "${inches}",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.title_Medium,
                                    color = if(isSelected)MaterialTheme.colorScheme.onSurface else TextSecondary,
                                )
                                Spacer(modifier = Modifier.width(70.dp))
                                if(isSelected ){
                                    Text(
                                        text = "in",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.title_Medium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                }else{
                                    Text(
                                        text = "in",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.title_Medium,
                                        color = Color.Transparent,
                                    )
                                }

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

                            onOk(selectedCm, selectedIndex)
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

@Composable
fun SingleChoiceSegmentedButton(modifier: Modifier = Modifier,
                                 selectedIndex : Int = 0 ,
                                 onSelectionChange: (Int) -> Unit,
                                 options : List<String>
                                ) {

    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth(),

    ) {

        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                colors = SegmentedButtonDefaults.colors(
                     activeContainerColor = ButtonSecondary,
                     inactiveContainerColor = androidx.compose.ui.graphics.Color.Transparent

                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = StrokeMain
                ),
                onClick = { onSelectionChange(index)

                          },
                selected = index == selectedIndex,
                label = {
                    Text(text =label,
                         style = MaterialTheme.typography.bodyMediumMedium,
                         color = segmentedText
                        )
                },
                icon = {
                    if (index == selectedIndex) {
                        Icon(
                            painter = painterResource(R.drawable.check),
                            modifier = Modifier.size(16.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }



            )
        }
    }
}
