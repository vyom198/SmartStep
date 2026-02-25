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
import com.vs.smartstep.core.room.DailyStepDao
import com.vs.smartstep.main.data.StepService
import com.vs.smartstep.main.domain.StepProvider
import com.vs.smartstep.main.domain.userProfileStore
import com.vs.smartstep.main.presentation.util.getDaysAgoDate
import com.vs.smartstep.main.presentation.util.getTodayDate
import com.vs.smartstep.main.presentation.util.toMinutesRounded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SmartStepHomeViewModel(
    private val context: Context,
    private val userProfileStore: userProfileStore,
    private val stepProvider: StepProvider,
    private val dao: DailyStepDao
) : ViewModel() {

    private val todayDate = getTodayDate()
    private val eventChannel = Channel<SmartStepHomeEvent>()
    val events = eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(SmartStepHomeState())

    val state = _state
        .onStart {

            isIgnoringBatteryOptimizations(context)
            checkActivityPermission()
            toggleService()
            loadUIdata()
            getLast7DayDate()
            loadMetrics()
            loadStepsGoal()

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SmartStepHomeState()
        )

    private fun toggleService(){
        viewModelScope.launch(Dispatchers.Default) {
            val intent = Intent(context, StepService::class.java).apply {
                action = if (_state.value.isIgnoringBatteryOpti) StepService.Actions.START.toString() else StepService.Actions.STOP.toString()
            }
            if (_state.value.isIgnoringBatteryOpti) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                }else{
                    context.startService(intent)
                }
            } else {
                context.startService(intent)
            }
        }
    }
    private fun getLast7DayDate(){
        viewModelScope.launch {
             dao.getStepsInDateRange(
                startDate = getDaysAgoDate(6),
                endDate = getDaysAgoDate(1)
            ).collect { days->
                _state.update {
                    it.copy(
                        ListOfDays = days.map {it.toUI() } ,
                        avgSteps = days.sumOf { it.steps } + _state.value.stepCount / 7
                    )
                }
             }
        }

    }
    private fun loadMetrics() {
        viewModelScope.launch {
            userProfileStore.isMetric().collect { bool ->
                _state.update {
                    it.copy(
                        isMetric = bool
                    )
                }
            }
        }
        viewModelScope.launch {
            if(_state.value.isIgnoringBatteryOpti){
                _state.update {
                    it.copy(
                        playPause = true
                    )
                }
            }
        }
    }

    private fun loadUIdata() {
        viewModelScope.launch {
            dao.getDailyStepByDateFlow(todayDate).collect { item ->
                _state.update {
                    it.copy(
                        stepCount = item?.steps ?: 0,
                        totalTime = item?.timeTaken?.toMinutesRounded()?:0,
                        distanceTravelled = item?.distance ?:0.0 ,
                        kcalorie = item?.kcal ?: 0 ,
                        currentItem = item?.toUI()
                    )

                }
            }
        }
    }
    private fun loadStepsGoal() {
        viewModelScope.launch {
            userProfileStore.getStep().collect { step ->
                _state.update {
                    it.copy(
                        dailyGoal = step
                    )

                }
            }
        }
    }

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
                eventChannel.send(SmartStepHomeEvent.Granted(hasPermission))

            }
        }
    }

    private fun isIgnoringBatteryOptimizations(context: Context) {
        viewModelScope.launch {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val value = powerManager.isIgnoringBatteryOptimizations(context.packageName)

            _state.update {
                it.copy(
                    isIgnoringBatteryOpti = value
                )
            }
        }
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
                            showBackgroundRationale = false,
                        )
                    }
                }
            }

            SmartStepHomeAction.onClickFixCount -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showBackgroundRationale = true
                        )
                    }
                }
            }

            SmartStepHomeAction.OnDisposed -> {
                isIgnoringBatteryOptimizations(context)
                toggleService()
            }

            SmartStepHomeAction.stepGoalBottomSheet -> {
                _state.update {
                    it.copy(
                        stepGoalBS = !it.stepGoalBS
                    )
                }
            }

            is SmartStepHomeAction.saveStep -> {
                viewModelScope.launch {
                    userProfileStore.saveStep(action.steps)
                    if (_state.value.dailyGoal > 0) {
                        stepProvider.startListening()
                        _state.update {
                            it.copy(
                                playPause = true
                            )
                        }
                    }
                    _state.update {
                        it.copy(
                            stepGoalBS = false
                        )
                    }

                }
            }

            SmartStepHomeAction.startSensor -> {
                viewModelScope.launch(Dispatchers.IO) {
                    stepProvider.startListening()
                }

            }

            SmartStepHomeAction.onExitConfirm -> {
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        _state.update {
                            it.copy(
                                exitDialog = false
                            )
                        }
                    }
                    withContext(Dispatchers.IO) {
                        val job = async { stepProvider.stopListening() }
                        val intent = Intent(context, StepService::class.java).apply {
                             StepService.Actions.STOP.toString()
                        }
                        context.startService(intent)
                        job.await()
                        val activity = dao.getDailyStepByDate(todayDate)!!
                        dao.insertDailyStep(
                            activity.copy(
                                steps = 0,
                                stepGoal = _state.value.dailyGoal,
                                baseline = activity.lastSensorValue ,
                                manualSteps = 0,
                                timeTaken = 0 ,
                                distance = 0.0,
                                kcal = 0
                            )
                        )
                    }
                    eventChannel.send(SmartStepHomeEvent.TerminateApp)
                }
            }

            SmartStepHomeAction.onExitOrDismissClick -> {
                _state.update {
                    it.copy(
                        exitDialog = !it.exitDialog
                    )
                }
            }

            SmartStepHomeAction.onDismissBackgroundAccess -> {
                _state.update {
                    it.copy(
                        showBackgroundRationale = false
                    )
                }
            }

            SmartStepHomeAction.dismissOpenApp -> {
                _state.update {
                    it.copy(
                        openSettings = false
                    )
                }
            }

            SmartStepHomeAction.isEditingDialog -> {
                _state.update {
                    it.copy(
                        isEditingSteps = !it.isEditingSteps
                    )
                }
            }

            is SmartStepHomeAction.onSaveDate -> saveStepsOfSpecificDate(action.date, action.steps)
            SmartStepHomeAction.onResseting -> {
                _state.update {
                    it.copy(
                        isReseting = !it.isReseting
                    )
                }
            }

            SmartStepHomeAction.onResetConfirm -> resetSteps()
            SmartStepHomeAction.onPlayPause -> {
                viewModelScope.launch {
                    if (_state.value.playPause) {
                        stepProvider.stopListening()
                    } else {
                        stepProvider.startListening()
                    }
                }

                _state.update {
                    it.copy(
                        playPause = !it.playPause
                    )
                }
            }
        }
    }

    private fun resetSteps() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val activity = dao.getDailyStepByDate(todayDate)!!
                dao.insertDailyStep(
                    activity.copy(
                        steps = 0,
                        baseline = activity.lastSensorValue,
                        manualSteps = 0,
                        timeTaken = 0,
                        distance = 0.0,
                        kcal = 0
                    )
                )

            }
            _state.update {
                it.copy(
                    isReseting = false
                )
            }

        }
    }

    private fun saveStepsOfSpecificDate(date: String, steps: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (date == todayDate) {
                    val activity = dao.getDailyStepByDate(todayDate)!!
                    dao.insertDailyStep(
                        activity.copy(
                            stepGoal = _state.value.dailyGoal,
                            baseline = activity.lastSensorValue,
                            manualSteps = steps,
                        )
                    )
                } else {
                    dao.insertDailyStep(
                        com.vs.smartstep.core.room.DailyStep(
                            date = date,
                            steps = steps,
                            stepGoal = _state.value.dailyGoal,
                        )
                    )

                }
            }
            _state.update {
                it.copy(
                    isEditingSteps = false
                )
            }
        }

    }

    private fun openBackgroundDialog() {
        isIgnoringBatteryOptimizations(context)
        if (!_state.value.isIgnoringBatteryOpti) {
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