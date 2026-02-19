package com.vs.smartstep.main.presentation.smartstep.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.main.presentation.smartstep.SmartStepHomeState

@Composable
fun SmartStepDrawerSheet(
    state : SmartStepHomeState,
    onClickFixCount : () -> Unit,
    onClickStepGoal : () -> Unit,
    onClickPersonalSettings : () -> Unit,
    onClickExit : () -> Unit,

) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.background,
    ) {

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp)

        ) {
            Spacer(modifier = Modifier.height(12.dp))
            if(!state.isIgnoringBatteryOpti) {
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Fix the “Stop Counting Steps” issue",
                            style = MaterialTheme.typography.bodyLargeMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    selected = false,
                    onClick = onClickFixCount
                )
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
            }
            Spacer(modifier = Modifier.height(12.dp))
            NavigationDrawerItem(

                label = {
                    Text(
                        text = "Step Goal",
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                selected = false,
                onClick = onClickStepGoal
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))
            NavigationDrawerItem(

                label = {
                    Text(
                        text = "Personal Settings",
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                selected = false,
                onClick = onClickPersonalSettings
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))
            NavigationDrawerItem(
                label = {
                    Text(
                        text = "Exit",
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                selected = false,
                onClick = onClickExit
            )

        }

    }
}