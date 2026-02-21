package com.vs.smartstep.main.presentation.myprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vs.smartstep.core.theme.BackgroundSecondary
import com.vs.smartstep.core.theme.BackgroundTertiary
import com.vs.smartstep.core.theme.TextSecondary
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.core.theme.bodyMediumRegular
import com.vs.smartstep.core.theme.title_Medium
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber


@Composable
fun WeightPickerDialog(
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onOk: (Double, Int) -> Unit,
    weight : Int,
    selectedIndex : Int = 0
) {
    var selectedIndex by retain { mutableIntStateOf(selectedIndex) }
    var selectedWeight by retain { mutableIntStateOf(if (selectedIndex == 0) weight else kgFromLbs(weight)) }
    var selectedWeightInlbs by retain { mutableIntStateOf(if (selectedIndex == 1) weight else lbsFromKg(weight)) }
    val weightKgs = remember { (10..200).toList() }
    val weightlbs  = remember{
        (10..200)
            .map { kg ->
                lbsFromKg(kg)
            }
    }

    // Update ft/in when cm changes
    fun updateFromkg(kg:Int) {
        selectedWeight = kg
        selectedWeightInlbs = lbsFromKg(kg)
    }
    val wtState = rememberLazyListState(
        initialFirstVisibleItemIndex = weightKgs.indexOf(selectedWeight).coerceAtLeast(0)
    )
    val lbsInState = rememberLazyListState(
        initialFirstVisibleItemIndex = weightlbs.indexOf(selectedWeightInlbs).coerceAtLeast(0)
    )
    val snapBehaviorlbs =
        rememberSnapFlingBehavior(lazyListState = lbsInState, snapPosition = SnapPosition.Start)
    val snapBehaviorkg =
        rememberSnapFlingBehavior(lazyListState = wtState, snapPosition = SnapPosition.Start)
    // Update ft/in when cm changes

    fun updateFromlbs(lbs : Int) {
        selectedWeightInlbs = lbs
        selectedWeight = kgFromLbs(lbs)

    }
    LaunchedEffect(wtState) {
        snapshotFlow { wtState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                if (selectedIndex == 0) { // Only update when kg is selected
                    val kgValue = weightKgs[index]
                    updateFromkg(kgValue)
                }
            }
    }

    // Track scroll position for lbs list
    LaunchedEffect(lbsInState) {
        snapshotFlow { lbsInState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                if (selectedIndex == 1) { // Only update when lbs is selected
                    val lbsValue = weightlbs[index]
                    updateFromlbs(lbsValue)
                }
            }
    }

    LaunchedEffect(selectedIndex) {
        if (selectedIndex == 0) {
            // Sync kg list when switching to kg
            wtState.scrollToItem(weightKgs.indexOf(selectedWeight).coerceAtLeast(0))
        } else {
            // Sync lbs list when switching to lbs
            lbsInState.scrollToItem(weightlbs.indexOf(selectedWeightInlbs).coerceAtLeast(0))
        }
    }


    fun onUnitChange(newIndex: Int) {
        selectedIndex = newIndex

    }


    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .widthIn(328.dp)
                .heightIn(418.dp).clip(
                    RoundedCornerShape(28.dp)
                ).background(
                    color = BackgroundSecondary
                )


            ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
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
                    options = listOf("kg", "lbs")
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Box(
                modifier = Modifier.fillMaxWidth().height(176.dp),
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(top = 88.dp).height(44.dp),
                    color = BackgroundTertiary
                ) {}
                    if (selectedIndex == 0) {
                        LazyColumn(
                            state = wtState,
                            flingBehavior = snapBehaviorkg,
                            contentPadding = PaddingValues(top = 88.dp, bottom = 44.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(weightKgs) { number ->
                                val isSelected = weightKgs[wtState.firstVisibleItemIndex] == number
                                Row(
                                    modifier = Modifier.fillMaxWidth().height(44.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center


                                ) {
                                    Text(
                                        text = number.toString(),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.title_Medium,
                                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else TextSecondary,

                                    )
                                }

                            }
                        }
                    } else {
                        LazyColumn(
                            state = lbsInState,
                            flingBehavior = snapBehaviorlbs,
                            contentPadding = PaddingValues(top = 88.dp, bottom = 44.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(weightlbs) { number ->

                                val isSelected = weightlbs[lbsInState.firstVisibleItemIndex] == number

                                Row(
                                    modifier = Modifier.fillMaxWidth().height(44.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "${number}",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.title_Medium,
                                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else TextSecondary,
                                    )

                                }

                            }
                        }

                    }


            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp , end = 24.dp),
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
                        Timber.i("Final weight: $valueToReturn" +  "index : $selectedIndex")
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