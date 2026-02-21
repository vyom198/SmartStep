package com.vs.smartstep.main.presentation.smartstep.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun AllowBackgroundBottomSheet(
    onClick: () -> Unit,
    sheetState: SheetState,
    onDismissRequest: () -> Unit

) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    if (windowSizeClass == WindowWidthSizeClass.COMPACT ) {
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = sheetState,
            // Remove the height/background from here
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = "Background access recommended",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.title_Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(26.dp))
                Text(
                    text = "Background access helps Smart Step track your activity more reliably.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMediumRegular,
                    color = MaterialTheme.colorScheme.onSecondary
                )

                Spacer(modifier = Modifier.height(40.dp))
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
                        text = "Continue",
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = TextWhite
                    )
                }
            }
        }
    } else Dialog(
        onDismissRequest = onDismissRequest,

        ) {
        AllowBgContentNotCompact(
            onClick,
            onDismissRequest = onDismissRequest
        )
    }

}

@Composable
fun AllowBgContentCompact(onClick: () -> Unit,

) {

}

@Composable
fun AllowBgContentNotCompact(
                             onClick : () ->Unit ,
                             onDismissRequest: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(312.dp)
            .height(244.dp).background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(28.dp)
            ).clip(
                RoundedCornerShape(28.dp)
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {

            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(20.dp)
                    .align(
                        AbsoluteAlignment.Right
                    )
                    .clickable {
                        onDismissRequest()
                    }
            )


        Text(
            text = "Background access\n" +
                    "recommended",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.title_Medium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Background access helps Smart\nStep track your activity more\nreliably.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMediumRegular,
            color = MaterialTheme.colorScheme.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))
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
                text = "Continue",
                style = MaterialTheme.typography.bodyLargeMedium,
                color = TextWhite
            )
        }
    }
}