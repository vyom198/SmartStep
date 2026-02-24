package com.vs.smartstep.main.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.vs.smartstep.core.room.DailyStepDao
import com.vs.smartstep.main.domain.StepProvider
import com.vs.smartstep.main.domain.userProfileStore
import com.vs.smartstep.main.presentation.util.getTodayDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class StepDetector(
    private val context: Context,
    private val userProfileStore: userProfileStore,
    private val dao : DailyStepDao,
    private val coroutineScope: CoroutineScope
) : StepProvider, SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    private var lastStepTimestamp = System.currentTimeMillis()
    private val todayDate = getTodayDate()
    private val _steps = MutableStateFlow(0)
    // Exposed read-only flow
    override val steps: StateFlow<Int> = _steps.asStateFlow()

    override fun startListening() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            Timber.i("Step Counter Sensor Started")
        } ?: Timber.e("Step Counter Sensor not available on this device")
    }

    override fun stopListening() {
        sensorManager.unregisterListener(this)
        Timber.i("Step Counter Sensor stopped")
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val totalStepsSinceBoot = event.values[0].toInt()

            _steps.value = totalStepsSinceBoot
            val now = System.currentTimeMillis()
            val timeDelta = now - lastStepTimestamp
            coroutineScope.launch {
               userProfileStore.addTime(timeDelta)

            }
            lastStepTimestamp = now

            Timber.i("Step update emitted: $totalStepsSinceBoot")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}