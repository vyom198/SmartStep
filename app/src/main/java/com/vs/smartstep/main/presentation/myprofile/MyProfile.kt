package com.vs.smartstep.main.presentation.myprofile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vs.smartstep.R
import com.vs.smartstep.core.theme.BackgroundSecondary
import com.vs.smartstep.core.theme.BackgroundWhite
import com.vs.smartstep.core.theme.StrokeMain
import com.vs.smartstep.core.theme.TextSecondary
import com.vs.smartstep.core.theme.TextWhite
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.core.theme.bodyLargeRegular
import com.vs.smartstep.core.theme.bodySmallRegular
import com.vs.smartstep.core.theme.title_Medium
import com.vs.smartstep.main.presentation.components.ObserveAsEvents
import com.vs.smartstep.main.presentation.myprofile.MyProfileAction
import com.vs.smartstep.main.presentation.myprofile.MyProfileState
import com.vs.smartstep.main.presentation.myprofile.MyProfileViewModel
import com.vs.smartstep.main.presentation.myprofile.components.HeightPickerDialog
import com.vs.smartstep.main.presentation.myprofile.components.WeightPickerDialog
import com.vs.smartstep.main.presentation.myprofile.components.getformattedHeight
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun MyProfileRoot(
    viewModel: MyProfileViewModel = koinViewModel(),
    NavigatetoApp : () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ObserveAsEvents(
        flow = viewModel.events,
    ) { event ->
        when (event) {
            MyProfileEvent.OnSaved -> {
                NavigatetoApp()
            }
        }
    }
    MyProfileScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    state: MyProfileState,
    onAction: (MyProfileAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundWhite
                ),
                title = {
                    Text(text = "My Profile",
                        style = MaterialTheme.typography.title_Medium,
                        color = MaterialTheme.colorScheme.onSurface
                        )
                },
                actions = {
                    if(!state.dataNotNull) {
                        TextButton(
                            modifier = Modifier.wrapContentSize(),
                            onClick = {
                                onAction(MyProfileAction.onSave)
                            }
                        ) {
                            Text(
                                text = "Skip",
                                style = MaterialTheme.typography.bodyLargeMedium,
                                color = MaterialTheme.colorScheme.primary

                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        val genders = listOf("Female", "Male")
        var enabled by retain {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = BackgroundSecondary
                )
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalAlignment = if(state.dataNotNull) Alignment.Start else Alignment.CenterHorizontally
        ) {
            if(state.dataNotNull){
                Text(text =
                    "Personal Settings",
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }else {
                Text(
                    text =
                        "This information helps calculate your activity more accurately.",
                    modifier = Modifier.width(340.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(16.dp ))
            Column(
                modifier = Modifier
                    .widthIn(min = 380.dp)
                    .heightIn(216.dp)
                    .clip(
                        RoundedCornerShape(14.dp)
                    )
                    .background(BackgroundWhite)
                    .border(
                        width = 1.dp,
                        color = StrokeMain,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(
                        horizontal = 16.dp, vertical = 16.dp

                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Column(

                ) {
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
                                color = Color(0xffD1D1D1),
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
                                text = state.currentGender,
                                style = MaterialTheme.typography.bodyLargeRegular,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = if (enabled) Icons.Default.KeyboardArrowUp else
                                Icons.Default.KeyboardArrowDown,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    enabled = !enabled

                                }
                        )
                    }

                BoxWithConstraints {
                    DropdownMenu(
                        expanded = enabled,
                        onDismissRequest = { enabled = false },
                        modifier = Modifier
                            .wrapContentHeight()
                            .width(maxWidth)

                            .clip(
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = BackgroundSecondary
                            )
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = Color(0xffD1D1D1)
                                ),
                                shape = RoundedCornerShape(8.dp)

                            ),
                        offset = DpOffset(y = 8 .dp, x = 0.dp),
                        shape = RoundedCornerShape(8.dp)

                    ) {
                        genders.forEach { item ->

                            DropdownMenuItem(
                                trailingIcon = {
                                    if (
                                        state.currentGender == item
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.check),
                                            modifier = Modifier.size(16.dp),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    } else {
                                        null
                                    }
                                },
                                text = {
                                    Text(
                                        text = item,
                                        style = MaterialTheme.typography.bodyLargeRegular,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                onClick = {
                                    enabled = false
                                    onAction(MyProfileAction.onSelectingGender(item))

                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }

                    }
                }
            }
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
                            color = Color(0xffD1D1D1),
                            shape = RoundedCornerShape(10.dp)

                        )
                        .padding(
                            horizontal = 16.dp,
                            vertical = 10.dp
                        )
                        .clickable {
                            onAction(MyProfileAction.selectingHeight)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text(
                            text = "Height",
                            style = MaterialTheme.typography.bodySmallRegular,
                            color = TextSecondary
                        )
                        Text(
                            text = getformattedHeight(state.selectedUnitforHeight, state.selectedHeightInCm),
                            style = MaterialTheme.typography.bodyLargeRegular,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
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
                            color = Color(0xffD1D1D1),
                            shape = RoundedCornerShape(10.dp)

                        )
                        .padding(
                            horizontal = 16.dp,
                            vertical = 10.dp
                        )
                        .clickable {
                            onAction(MyProfileAction.selectingWeight)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text(
                            text = "Weight",
                            style = MaterialTheme.typography.bodySmallRegular,
                            color = TextSecondary
                        )
                        Text(
                            text = "${state.selectedWeight} ${if(state.selectedUnitforWeight == 0) "kg" else "lbs"}",
                            style = MaterialTheme.typography.bodyLargeRegular,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                if(state.isHeightDialog){
                    HeightPickerDialog(onDismissRequest = {onAction(MyProfileAction.onDismissHeightDialog) },
                          onCancel = { onAction(MyProfileAction.onDismissHeightDialog) },
                          onOk = { height , index ->
                               Timber.i("Height : $height , Index : $index")
                                onAction(MyProfileAction.onSelectHeight(index, height))
                          },
                        state.selectedHeightInCm
                    )
                }

                if(state.isWeightDialog){
                    WeightPickerDialog(
                        onDismissRequest = { onAction(MyProfileAction.onDismissWeightDialog) },
                        onCancel = { onAction(MyProfileAction.onDismissWeightDialog) },
                        onOk = { weight, index ->
                            onAction(MyProfileAction.onSelectWeight(index, weight))
                        },
                        weight = state.selectedWeight,
                        selectedIndex = state.selectedUnitforWeight
                    )
                }


            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                onClick = {
                    onAction(MyProfileAction.onSave)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if(state.dataNotNull)"Save" else "Start",
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = TextWhite
                )

            }
        }
    }

}
