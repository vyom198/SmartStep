package com.vs.smartstep.main.presentation.smartstep.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.window.core.layout.WindowWidthSizeClass
import com.vs.smartstep.core.theme.TextWhite
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.core.theme.bodyMediumRegular
import com.vs.smartstep.core.theme.title_Medium


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenAppBottomSheet(
    onClick: () -> Unit,
    onDismissOpenApp : () -> Unit

    ) {

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val isCompact = windowSizeClass == WindowWidthSizeClass.COMPACT

    if(isCompact) {
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = rememberModalBottomSheetState(),
            // Remove the height/background from here
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp) .padding(16.dp) // Set height here


                ) {
                OpenAppContent(
                    onClick,
                    isCompact = isCompact

                )
            }
        }
    }else{
        Dialog(onDismissRequest = onDismissOpenApp) {
            Column(
                modifier = Modifier
                    .width(312.dp )
                    .height(288.dp).background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(28.dp)
                    ) // Set height here
                    .padding(24.dp)

                ) {
                OpenAppContent(
                    onClick,
                    isCompact = isCompact
                )
            }

        }
    }
}

@Composable
fun OpenAppContent(
    onClick: () -> Unit,
    isCompact: Boolean
) {
    val spacer = if(isCompact)26.dp else 16.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Enable access manually",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.title_Medium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(spacer))
        Text(
            text = "To track your steps, please enable the\n" +
                    "permission in your device settings.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMediumRegular,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }

    Spacer(modifier = Modifier.height(spacer))
    Text(
        text = "1. Open Permissions",
        style = MaterialTheme.typography.bodyLargeMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "2. Tap Physical activity",
        style = MaterialTheme.typography.bodyLargeMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "3. Select Allow",
        style = MaterialTheme.typography.bodyLargeMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )
    Spacer(modifier = Modifier.height(spacer))
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = "Open settings",
            style = MaterialTheme.typography.bodyLargeMedium,
            color = TextWhite
        )
    }
}