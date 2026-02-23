package com.vs.smartstep.main.presentation.smartstep.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.vs.smartstep.core.theme.StrokeMain
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.core.theme.bodyLargeRegular
import com.vs.smartstep.core.theme.bodyMediumRegular
import com.vs.smartstep.core.theme.bodySmallRegular
import com.vs.smartstep.core.theme.title_Medium
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


@Composable
fun StepEditingDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val dateFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("yyyy/MM/dd")
    } else {
        null
    }
    val date by retain{
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
    var steps by retain {
        mutableStateOf(0)
    }
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .clip(
                    RoundedCornerShape(28.dp)
                )
                .background(
                    color = BackgroundSecondary
                )
                .padding(16.dp),

        ) {
            Text(
                text = "Edit steps",
                style = MaterialTheme.typography.title_Medium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Calories,distance & duration will\nbe recalculated accordingly.",
                style = MaterialTheme.typography.bodyMediumRegular,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value =  date ,
                onValueChange = {},
                textStyle = MaterialTheme.typography.bodyLargeRegular,
                label = {
                    Text(
                        text = "Date",
                        style = MaterialTheme.typography.bodySmallRegular,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector =  Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                onEnabledDate = !onEnabledDate
                            },
                    )
                },
                readOnly = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StrokeMain,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = StrokeMain,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onPrimary

                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value =  steps.toString() ,
                onValueChange = {
                    steps = it.toInt()
                },
                textStyle = MaterialTheme.typography.bodyLargeRegular,
                label = {
                    Text(
                        text = "Steps",
                        style = MaterialTheme.typography.bodySmallRegular,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StrokeMain,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = StrokeMain,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onPrimary

                )
            )
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
                    modifier = Modifier.width(54.dp).height(44.dp),
                    onClick = {

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
            DatePickerDialog()
        }
    }
}

@Composable
fun DatePickerDialog() {

}