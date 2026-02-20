package com.vs.smartstep.main.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.vs.smartstep.main.domain.StepProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class StepDetector(context: Context) : StepProvider, SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    // Internal mutable flow
    private val _steps = MutableStateFlow(0)
    // Exposed read-only flow
    override val steps: StateFlow<Int> = _steps.asStateFlow()

    override fun startListening() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        } ?: Timber.e("Step Counter Sensor not available on this device")
    }

    override fun stopListening() {
        sensorManager.unregisterListener(this)
        Timber.i("Step Counter Sensor stopped")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val totalStepsSinceBoot = event.values[0].toInt()
            
            // Update the flow value
            _steps.value = totalStepsSinceBoot
            
            Timber.i("Step update emitted: $totalStepsSinceBoot")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}