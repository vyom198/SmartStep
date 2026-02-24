package com.vs.smartstep.main.presentation.smartstep

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.LocalRetainedValuesStoreProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vs.smartstep.core.room.*
import com.vs.smartstep.core.room.DailyStepDao
import com.vs.smartstep.main.domain.StepProvider
import com.vs.smartstep.main.domain.userProfileStore
import com.vs.smartstep.main.presentation.util.calculateCalories
import com.vs.smartstep.main.presentation.util.calculateDistance
import com.vs.smartstep.main.presentation.util.getTodayDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

class SmartStepHomeViewModel(
    private val context: Context,
    private val userProfileStore: userProfileStore,
    private val stepProvider: StepProvider,
    private val dao: DailyStepDao
) : ViewModel() {

    private val todayDate = getTodayDate()
    var lastProcessedSteps = 0
    private val eventChannel = Channel<SmartStepHomeEvent>()
    val events = eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(SmartStepHomeState())

    val state = _state
        .onStart {

            isIgnoringBatteryOptimizations(context)
            checkActivityPermission()
            loadSteps()
            loadCalories()
            UpdateSteps()
            loadMetrics()
            loadStepsGoal()

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SmartStepHomeState()
        )

    private fun loadMetrics() {
        viewModelScope.launch {
            userProfileStore.totalTime.collect { time ->
                val min = (time / 1000 / 60).toInt()
                _state.update {
                    it.copy(
                        totalTime = min
                    )
                }
            }



        }
        viewModelScope.launch {
            userProfileStore.isMetric().collect { bool ->
                Timber.i("isMetric : $bool")
                _state.update {
                    it.copy(
                        isMetric = bool
                    )
                }
            }
        }
    }

    private fun UpdateSteps() {
        viewModelScope.launch(Dispatchers.Default) {

            combine(
                stepProvider.steps,
                userProfileStore.baselineFlow,
                userProfileStore.manualStepsFlow
            ) { sensorValue, baseline, manualSteps ->
                Timber.d("Combine triggered - sensor: $sensorValue, baseline: $baseline, manual: $manualSteps")
                val dailySteps =  if(sensorValue > 0 ) {
                   if (manualSteps > 0) {
                        manualSteps + abs(sensorValue - baseline)
                    } else {
                        abs(sensorValue - baseline)
                    }
                }else{
                     dao.getDailyStepByDate(todayDate)?.steps ?: 0
                }
                val activity = dao.getDailyStepByDate(todayDate)
                if (activity != null){
                    dao.insertDailyStep(
                        activity.copy(
                            steps = dailySteps,
                            baseline =  0
                        )
                    )
                }else{
                    dao.insertDailyStep(
                        DailyStep(
                            date = todayDate,
                            steps = dailySteps,
                            stepGoal = _state.value.dailyGoal,
                            baseline = 0
                        ))
                }

                val stepDifference = dailySteps - lastProcessedSteps
                val shouldUpdate = when {
                    stepDifference >= 10 -> true
                    manualSteps > 0 -> true
                    else -> false
                }
                Pair(dailySteps, shouldUpdate )
            }.flowOn(Dispatchers.Default)
                .collect { (dailySteps, shouldUpdate) ->

                    if (shouldUpdate) {
                        withContext(Dispatchers.Default) {
                            val distance = calculateDistance(
                                steps = dailySteps,
                                heightCm = userProfileStore.getHeightWithUnit().second.toDouble(),
                                Unit = userProfileStore.getWeightWithUnit().first
                            )
                            val kcal = calculateCalories(
                                dailySteps,
                                userProfileStore.getWeightWithUnit().second,
                                gender = userProfileStore.getGender(),
                                userProfileStore.getWeightWithUnit().first
                            )

                            withContext(Dispatchers.Main) {
                                _state.update {
                                    it.copy(
                                        distanceTravelled = distance,
                                        kcalorie = kcal
                                    )
                                }
                            }
                        }
                        lastProcessedSteps = dailySteps
                    }

                }
        }



    }
    private fun loadCalories(){
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val distance = calculateDistance(
                    steps = _state.value.stepCount,
                    heightCm = userProfileStore.getHeightWithUnit().second.toDouble(),
                    Unit = userProfileStore.getWeightWithUnit().first
                )
                val kcal = calculateCalories(
                    _state.value.stepCount,
                    userProfileStore.getWeightWithUnit().second,
                    gender = userProfileStore.getGender(),
                    userProfileStore.getWeightWithUnit().first
                )

                withContext(Dispatchers.Main) {
                    _state.update {
                        it.copy(
                            distanceTravelled = distance,
                            kcalorie = kcal
                        )
                    }
                }
            }
        }
    }
    private fun loadSteps(){
        viewModelScope.launch {
            dao.getDailyStepByDateFlow(todayDate).collect { item ->
                Timber.d("steps from room: ${item.steps}")
                _state.update {
                    it.copy(
                        stepCount = item.steps
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
                Timber.i("has permission : ${hasPermission}")
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
                        job.await()
                        dao.insertDailyStep(
                            DailyStep(
                                date = todayDate,
                                steps = 0,
                                stepGoal = _state.value.dailyGoal,
                                baseline = 0
                            )
                        )
                        userProfileStore.saveBaseline(stepProvider.steps.first())
                        userProfileStore.saveManualEdit(0)
                        userProfileStore.resetTime()

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
                userProfileStore.saveBaseline(stepProvider.steps.first())
                userProfileStore.saveManualEdit(0)
                val activity = dao.getDailyStepByDate(todayDate)
                if (activity != null) {
                    dao.insertDailyStep(
                        activity.copy(
                            steps = 0
                        )
                    )
                } else {
                    dao.insertDailyStep(
                        DailyStep(
                            date = todayDate,
                            stepGoal = userProfileStore.getStep().first(),
                            steps = _state.value.stepCount,
                            baseline = 0
                        )
                    )


                }
                userProfileStore.resetTime()
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
                    userProfileStore.saveManualEdit(steps)
                    userProfileStore.saveBaseline(stepProvider.steps.first())
                    dao.insertDailyStep(
                        com.vs.smartstep.core.room.DailyStep(
                            date = date,
                            steps = _state.value.stepCount,
                            stepGoal = _state.value.dailyGoal,
                            baseline = 0
                        )
                    )
                } else {
                    dao.insertDailyStep(
                        com.vs.smartstep.core.room.DailyStep(
                            date = date,
                            steps = steps,
                            stepGoal = _state.value.dailyGoal,
                            baseline = 0
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