package com.vs.smartstep.main.presentation.smartstep.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vs.smartstep.core.theme.BackgroundTertiary
import com.vs.smartstep.core.theme.TextWhite
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.core.theme.bodyMediumRegular
import com.vs.smartstep.core.theme.title_Medium
import com.vs.smartstep.main.presentation.smartstep.SmartStepHomeState
import timber.log.Timber
import java.util.prefs.BackingStoreException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepGoalBottomSheet(
    state: SmartStepHomeState,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    val listofSteps = remember {
        (1000..40000 step 1000).toList()
    }
    val initialIndex = listofSteps.indexOf(state.dailyGoal).coerceAtLeast(0)

    val pickerState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialIndex
    )
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = pickerState, snapPosition = SnapPosition.Start)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(352.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = "Step Goal",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.title_Medium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth().height(170.dp),
                contentAlignment = Alignment.TopCenter
            ) {

                Surface(
                    modifier = Modifier.fillMaxWidth().padding(top = 88.dp)
                        .height(44.dp),
                    color = BackgroundTertiary
                ) {}
                LazyColumn(
                    state = pickerState,
                    flingBehavior = snapBehavior,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 88.dp, bottom = 44.dp)
                ) {
                    items(listofSteps) { step ->
                        val isSelected = listofSteps[pickerState.firstVisibleItemIndex] == step
                        Row(
                            modifier = Modifier.fillMaxWidth().height(44.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = step.toString(),
                                style = MaterialTheme.typography.title_Medium,
                                letterSpacing = 1.sp,
                                color = if(isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(26.dp))
            Button(
                onClick = {
                    Timber.i("${listofSteps[pickerState.firstVisibleItemIndex ]}")
                    onConfirm(listofSteps[pickerState.firstVisibleItemIndex])

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = TextWhite
                )
            }
            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .width(90.dp)
                    .height(44.dp),
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

}