package com.vs.smartstep.main.presentation.smartstep


import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vs.smartstep.R
import com.vs.smartstep.core.theme.BackgroundWhite20
import com.vs.smartstep.core.theme.bodyLargeMedium
import com.vs.smartstep.core.theme.segmentedText
import com.vs.smartstep.core.theme.title_Accent
import com.vs.smartstep.core.theme.title_Medium
import com.vs.smartstep.main.presentation.components.ObserveAsEvents
import com.vs.smartstep.main.presentation.smartstep.components.AllowAccessBottomS
import com.vs.smartstep.main.presentation.smartstep.components.AllowBackgroundBottomSheet
import com.vs.smartstep.main.presentation.smartstep.components.OpenAppBottomSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber


@Composable
fun SmartStepHomeRoot(
    viewModel: SmartStepHomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(
         viewModel.events,
    ) { event ->
        when(event){
            is SmartStepHomeEvent.Granted -> {
                   if(event.hasPermission){

                       if(state.count == 2 ){

                           viewModel.onAction(SmartStepHomeAction.onHasActivityPermission)
                       }
                   }else{
                       if(state.count == 2){
                           viewModel.onAction(SmartStepHomeAction.ResetAfterSettings)
                       }
                   }
            }
        }
    }
    SmartStepHomeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartStepHomeScreen(
    state: SmartStepHomeState,
    onAction: (SmartStepHomeAction) -> Unit,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val show = activity?.shouldShowRequestPermissionRationale(Manifest.permission.ACTIVITY_RECOGNITION)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->

        onAction(SmartStepHomeAction.UpdatePermissionStatus(isGranted))

    }
    LaunchedEffect(Unit) {
        if(!state.hasActivityPermission && state.count < 2 ){
            Timber.i("i am getting called ")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissionLauncher.launch(
                    Manifest.permission.ACTIVITY_RECOGNITION
                )
            }
        }

    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.background,
            ) {

                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp)

                ) {

                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Fix the “Stop Counting Steps” issue",
                                style = MaterialTheme.typography.bodyLargeMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        selected = false,
                        onClick = {

                        }
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(

                        label = {
                            Text(
                                text = "Step Goal",
                                style = MaterialTheme.typography.bodyLargeMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        selected = false,
                        onClick = {

                        }
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(

                        label = {
                            Text(
                                text = "Personal Settings",
                                style = MaterialTheme.typography.bodyLargeMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        selected = false,
                        onClick = {

                        }
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Exit",
                                style = MaterialTheme.typography.bodyLargeMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        selected = false,
                        onClick = {

                        }
                    )

                }

            }
        },
    ) {

        Scaffold(
            modifier = Modifier.fillMaxSize().background(
                color = MaterialTheme.colorScheme.background
            ),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Smart Step",
                            style = MaterialTheme.typography.title_Medium,
                            color = segmentedText
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,

                        ),
                    navigationIcon = {
                        Icon(
                            painter = painterResource(R.drawable.menu_burger_square_1),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(34.dp).clickable {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                    }
                )
            }

        ) { paddingValues ->

            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Spacer(modifier = Modifier.weight(1f))
                Card(
                    modifier = Modifier.width(380.dp).height(208.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(
                            16.dp
                        ),


                        ) {
                        IconButton(
                            onClick = { },
                            modifier = Modifier.size(38.dp).background(
                                color = BackgroundWhite20,
                                shape = RoundedCornerShape(8.dp)
                            ),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.sneakers),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "4,523",
                            style = MaterialTheme.typography.title_Accent,
                            color = Color.White
                        )
                        Text(
                            text = "/6000 Steps",
                            style = MaterialTheme.typography.title_Medium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().height(
                                12.dp
                            ).clip(
                                RoundedCornerShape(99)
                            ).background(
                                BackgroundWhite20
                            ).padding(horizontal = 1.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(0.4f).height(
                                    8.dp
                                ).clip(
                                    RoundedCornerShape(99)
                                ).background(
                                    Color.White
                                )
                            )

                        }
                    }


                }
                Spacer(modifier = Modifier.weight(1f))

            }
            if (state.shouldShowAllow && show!! && state.count == 1) {
                AllowAccessBottomS(
                    onClick = {
                        permissionLauncher.launch(
                            Manifest.permission.ACTIVITY_RECOGNITION
                        )
                        onAction(SmartStepHomeAction.DismissDialog)
                    }

                )
            }
            if (state.openSettings) {
                OpenAppBottomSheet(
                    onClick = {
                        onAction(SmartStepHomeAction.OpenAppSettings)
                    }
                )
            }

            if (state.showBackgroundRationale) {
                AllowBackgroundBottomSheet(
                    onClick = {
                        onAction(SmartStepHomeAction.onClickContinueBackground)
                    }
                )
            }

        }
    }


}

