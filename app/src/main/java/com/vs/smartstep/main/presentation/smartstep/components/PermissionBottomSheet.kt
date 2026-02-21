package com.vs.smartstep.main.presentation.smartstep.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.window.core.layout.WindowWidthSizeClass
import com.vs.smartstep.R
import com.vs.smartstep.core.theme.TextWhite
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.core.theme.bodyLargeRegular
import com.vs.smartstep.core.theme.title_Medium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllowAccessBottomS(
                       onClick : () -> Unit,
                       onDismiss : () -> Unit

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
                    .height(260.dp) // Set height here
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PermissionContent(
                    onClick = onClick,
                    isCompact = isCompact
                )
            }
        }
    }else{
        Dialog(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .width(312.dp)
                    .height(238.dp).background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(28.dp)
                    ) // Set height here
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                  PermissionContent(
                      onClick = onClick,
                      isCompact = isCompact
                  )
            }
        }
    }
}

@Composable
fun PermissionContent(
    onClick: () -> Unit,
    isCompact: Boolean
) {
    val spacer = if(isCompact)26.dp else 16.dp
    Box(
        modifier = Modifier.size(44.dp).clip(
            shape = RoundedCornerShape(8.dp)
        ).background(
            color = Color.White

        ), contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.pin__location__direction),
            contentDescription = null,
            tint = Color(0xff4A4459),
            modifier = Modifier.size(24.dp)
        )
    }

    Spacer(modifier = Modifier.height(spacer))
    Text(
        text = "To count your steps,\nSmart Step needs access to your\nmotion sensors.",
        textAlign = TextAlign.Center,
        style = if(isCompact)MaterialTheme.typography.title_Medium else MaterialTheme.typography.bodyLargeRegular,
        color = MaterialTheme.colorScheme.onPrimary
    )
    Spacer(modifier = Modifier.height(spacer))
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(44.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = "Allow access",
            style = MaterialTheme.typography.bodyLargeMedium,
            color = TextWhite
        )
    }
}