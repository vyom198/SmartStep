package com.vs.smartstep.main.presentation.smartstep

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vs.smartstep.main.domain.userProfileStore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class SmartStepHomeViewModel(
    private val context: Context,
    private val userProfileStore: userProfileStore
) : ViewModel() {

    private val eventChannel = Channel<SmartStepHomeEvent>()
    val events = eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(SmartStepHomeState())
    val state = _state
        .onStart {
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        isIgnoringBatteryOpti = isIgnoringBatteryOptimizations(context)
                    )
                }
            }

            checkActivityPermission()

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SmartStepHomeState()
        )

    private fun checkActivityPermission() {
        viewModelScope.launch {
            userProfileStore.getPermissionCount().collect { count ->
                _state.update {
                    it.copy(
                        count = count
                    )
                }

            }
        }
        viewModelScope.launch {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) == PackageManager.PERMISSION_GRANTED
                _state.update {
                    it.copy(
                        hasActivityPermission = hasPermission
                    )
                }
                Timber.i("has permission : ${hasPermission}" )
                eventChannel.send(SmartStepHomeEvent.Granted(hasPermission))

            }
        }
    }

    private fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }
    fun onResume() {
        checkActivityPermission()
    }
    fun onAction(action: SmartStepHomeAction) {
        when (action) {

            SmartStepHomeAction.DismissDialog -> {
                _state.update {
                    it.copy(
                        shouldShowAllow = false
                    )
                }
            }

            is SmartStepHomeAction.UpdatePermissionStatus -> {
                viewModelScope.launch {
                    if (action.isGranted) {
                        _state.update {
                            it.copy(
                                hasActivityPermission = true,
                            )
                        }

                            openBackgroundDialog()

                    } else {
                        if (_state.value.count < 2) {
                            userProfileStore.addPermissionCount()
                        }

                        _state.update {
                            it.copy(
                                shouldShowAllow = it.count == 1,
                            )
                        }
                        _state.update {
                            it.copy(
                                openSettings = it.count == 2
                            )

                        }
                    }
                }

            }


            SmartStepHomeAction.OpenAppSettings -> {
                viewModelScope.launch {
                    openAppSettings()
                }
            }

            SmartStepHomeAction.ResetAfterSettings -> {
                _state.update {
                    it.copy(
                        openSettings = true
                    )
                }


            }

            SmartStepHomeAction.onHasActivityPermission -> {
                    _state.update {
                        it.copy(
                            openSettings = false
                        )
                    }
                openBackgroundDialog()
            }

            SmartStepHomeAction.onClickContinueBackground -> {
                viewModelScope.launch {


                        ignoreBatteryOptimizaiton()

                    _state.update {
                        it.copy(
                            showBackgroundRationale = false

                        )
                    }
                }
            }
        }
    }

    private fun openBackgroundDialog() {
        val exempt = isIgnoringBatteryOptimizations(context)
        if(!exempt) {
            viewModelScope.launch {
                if (!userProfileStore.getIsbackgroundAsked()) {
                    _state.update {
                        it.copy(
                            showBackgroundRationale = true
                        )
                    }
                    userProfileStore.askedBackgroundPermission(true)
                }
            }
        }

    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)


    }

    private fun ignoreBatteryOptimizaiton() {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }


}