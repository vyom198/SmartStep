package com.vs.smartstep.main.presentation.smartstep.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vs.smartstep.core.theme.BackgroundSecondary
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.core.theme.bodyLargeRegular


@Composable
fun ResettingDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,

    ) {

    Dialog(
        onDismissRequest = onDismiss,

        ) {
        Column(
            modifier = Modifier
                .width(312.dp)
                .height(
                    164.dp
                )
                .clip(
                    RoundedCornerShape(28.dp)
                )
                .background(
                    color = BackgroundSecondary
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween

        ) {

            Text(
                text = "Are you sure you want to reset today’s steps?",
                style = MaterialTheme.typography.bodyLargeRegular,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
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
                    onClick = onConfirm
                ) {
                    Text(
                        text = "Reset",
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }


        }

    }

}