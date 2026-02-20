package com.vs.smartstep.main.presentation.smartstep.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vs.smartstep.R
import com.vs.smartstep.core.theme.BackgroundSecondary
import com.vs.smartstep.core.theme.TextPrimary
import com.vs.smartstep.core.theme.TextSecondary
import com.vs.smartstep.core.theme.TextWhite
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.core.theme.bodyLargeRegular
import com.vs.smartstep.core.theme.bodyMediumRegular
import com.vs.smartstep.core.theme.title_Medium

@Composable
fun ExitDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,

) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .widthIn(312.dp)
                .heightIn(214.dp).clip(
                    RoundedCornerShape(28.dp)
                ).background(
                    color = BackgroundSecondary
                )
                .padding(horizontal = 16.dp, vertical = 24.dp),
               verticalArrangement = Arrangement.spacedBy(24.dp),
               horizontalAlignment = Alignment.CenterHorizontally

            ) {

            Icon(
                    painter = painterResource(R.drawable.power__turn_on),
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(34.dp)
            )

            Text(
                text = "The app will fully close and will " +
                        "not run in the background.",
                textAlign = TextAlign.Center,
                maxLines = 2,
                style = MaterialTheme.typography.bodyLargeRegular,
                color = TextSecondary
            )
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Ok",
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = TextWhite
                )
            }
        }
    }
}