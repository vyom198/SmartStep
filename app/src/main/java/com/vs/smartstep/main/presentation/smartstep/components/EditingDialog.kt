package com.vs.smartstep.main.presentation.smartstep.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vs.smartstep.core.theme.BackgroundSecondary
import com.vs.smartstep.core.theme.BackgroundTertiary
import com.vs.smartstep.core.theme.StrokeMain
import com.vs.smartstep.core.theme.TextSecondary
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.core.theme.bodyLargeRegular
import com.vs.smartstep.core.theme.bodyMediumRegular
import com.vs.smartstep.core.theme.bodySmallRegular
import com.vs.smartstep.core.theme.title_Medium
import com.vs.smartstep.main.presentation.util.parseDateFromString
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


@Composable
fun StepEditingDialog(
    onDismiss: () -> Unit,
    onClickSave : (String , Int) -> Unit
) {
    val dateFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("yyyy/MM/dd")
    } else {
        null
    }
    var date by retain{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mutableStateOf(LocalDate.now().format(dateFormatter))
        } else {
            val calendar = Calendar.getInstance()
            val legacyFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            mutableStateOf(legacyFormat.format(calendar.time))
        }
    }
    var onEnabledDate by retain {
        mutableStateOf(false)
    }
    var stepText by retain {
        mutableStateOf("0")
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .width(340.dp).wrapContentHeight()
                .clip(
                    RoundedCornerShape(28.dp)
                )
                .background(
                    color = BackgroundSecondary
                )
                .padding(horizontal = 16.dp, vertical = 20.dp),

        ) {

                Text(
                    text = "Edit steps",
                    style = MaterialTheme.typography.title_Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Calories,distance & duration will be recalculated accordingly.",
                style = MaterialTheme.typography.bodyMediumRegular,
                maxLines = 2,
                // Remove the manual \n and BoxWithConstraints
                // Use softWrap and Overflow to force the behavior
                softWrap = true,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSecondary
            )



            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = BackgroundSecondary
                    )
                    .border(
                        width = 1.dp,
                        color = StrokeMain,
                        shape = RoundedCornerShape(10.dp)

                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(
                        text = "Gender",
                        style = MaterialTheme.typography.bodySmallRegular,
                        color = TextSecondary
                    )
                    Text(
                        text = date.toString(),
                        style = MaterialTheme.typography.bodyLargeRegular,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                           onEnabledDate = !onEnabledDate
                        }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = BackgroundSecondary
                    )
                    .border(
                        width = 1.dp,
                        color = StrokeMain,
                        shape = RoundedCornerShape(10.dp)

                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier =  Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Steps",
                        style = MaterialTheme.typography.bodySmallRegular,
                        color = TextSecondary
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        BasicTextField(
                            value = stepText,
                            onValueChange = {
                                stepText = it
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            textStyle = MaterialTheme.typography.bodyLargeRegular.copy(
                                color = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier
                                .height(24.dp),

                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Row {

                                    innerTextField()
                                }

                            }
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                TextButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = onDismiss

                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                TextButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = {
                        onClickSave(date , stepText.toInt())
                    }

                ) {
                    Text(
                        text = "Save",
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        if(onEnabledDate){
            DatePickerDialog(
                onDismiss = {
                    onEnabledDate = false
                },
                onConfirm = {
                    date = it
                    onEnabledDate = false
                },
                date = date
            )
        }
    }
}

@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    date : String
) {
    val date by retain {
        mutableStateOf(date)
    }
    val dateParts by remember {
        mutableStateOf(parseDateFromString(date))
    }
    val currentYear by retain {  mutableStateOf(dateParts.first)}
    val currentMonth by retain {  mutableStateOf(dateParts.second)}
    val currentDay by retain {  mutableStateOf(dateParts.third)}

    val listOfYears = remember {
        (2025..2050 ).toList()
    }
    val listOfMonths = remember {
        (1..12 ).toList()
    }
    val listOfDates = remember {
        (1..31).toList()
    }


// Find initial indices for each picker
    val initialYearIndex = listOfYears.indexOf(currentYear).coerceAtLeast(0)
    val initialMonthIndex = listOfMonths.indexOf(currentMonth).coerceAtLeast(0)
    val initialDayIndex = listOfDates.indexOf(currentDay).coerceAtLeast(0)

// Create three separate picker states
    val yearPickerState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialYearIndex
    )
    val monthPickerState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialMonthIndex
    )
    val dayPickerState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialDayIndex
    )
    val yearSnapBehavior = rememberSnapFlingBehavior(
        lazyListState = yearPickerState,
        snapPosition = SnapPosition.Start
    )
    val monthSnapBehavior = rememberSnapFlingBehavior(
        lazyListState = monthPickerState,
        snapPosition = SnapPosition.Start
    )
    val daySnapBehavior = rememberSnapFlingBehavior(
        lazyListState = dayPickerState,
        snapPosition = SnapPosition.Start
    )
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .width(328.dp)
                .height(332.dp).background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(28.dp)
                )

        ) {
            Text(
                text = "Date",
                style = MaterialTheme.typography.title_Medium,
                modifier = Modifier.padding(start = 24.dp , top = 24.dp ),
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
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly, // Add this
                    verticalAlignment = Alignment.CenterVertically // Add this

                ) {
                    LazyColumn(
                        state = yearPickerState,
                        modifier = Modifier.weight(1f),
                        flingBehavior = yearSnapBehavior,
                        contentPadding = PaddingValues(top = 88.dp, bottom = 44.dp)
                    ) {
                        items(listOfYears) { year ->
                            val isSelected = listOfYears[yearPickerState.firstVisibleItemIndex] == year
                            Row(
                                modifier = Modifier.fillMaxWidth().height(44.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = year.toString(),
                                    style = MaterialTheme.typography.title_Medium,
                                    letterSpacing = 1.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                    LazyColumn(
                        state = monthPickerState,
                        flingBehavior = monthSnapBehavior,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(top = 88.dp, bottom = 44.dp)
                    ) {
                        items(listOfMonths) { month ->
                              val isSelected = listOfMonths[monthPickerState.firstVisibleItemIndex] == month
                            Row(
                                modifier = Modifier.fillMaxWidth().height(44.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = month.toString().padStart(2, '0'),
                                    style = MaterialTheme.typography.title_Medium,
                                    letterSpacing = 1.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                    LazyColumn(
                        state = dayPickerState,
                        flingBehavior = daySnapBehavior,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(top = 88.dp, bottom = 44.dp)
                    ) {
                        items(listOfDates) { date ->
                              val isSelected = listOfDates[dayPickerState.firstVisibleItemIndex] == date
                            Row(
                                modifier = Modifier.fillMaxWidth().height(44.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text =  date.toString().padStart(2, '0'),
                                    style = MaterialTheme.typography.title_Medium,
                                    letterSpacing = 1.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                }


            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp

                ),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = onDismiss

                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                TextButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = {
                        val date = "${listOfYears[yearPickerState.firstVisibleItemIndex]}/${
                            listOfMonths[monthPickerState.firstVisibleItemIndex].toString().padStart(2, '0')
                        }/${
                            listOfDates[dayPickerState.firstVisibleItemIndex].toString().padStart(2, '0')
                        }"
                        Timber.d("date : $date")
                        onConfirm(date)

                    }

                ) {
                    Text(
                        text = "Save",
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

    }
}