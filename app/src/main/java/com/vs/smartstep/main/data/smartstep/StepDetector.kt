package com.vs.smartstep.main.data.smartstep

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.vs.smartstep.core.room.DailyStep
import com.vs.smartstep.core.room.DailyStepDao
import com.vs.smartstep.main.domain.smartStep.StepProvider
import com.vs.smartstep.main.domain.smartStep.userProfileStore
import com.vs.smartstep.main.presentation.util.calculateCalories
import com.vs.smartstep.main.presentation.util.calculateDistance
import com.vs.smartstep.main.presentation.util.getDaysAgoDate
import com.vs.smartstep.main.presentation.util.getTodayDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs

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
    private var stepDifference = 0

    override fun startListening() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            Timber.Forest.i("Step Counter Sensor Started")
        } ?: Timber.Forest.e("Step Counter Sensor not available on this device")
    }

    override fun stopListening() {
        sensorManager.unregisterListener(this)
        Timber.Forest.i("Step Counter Sensor stopped")
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {

            coroutineScope.launch(Dispatchers.Default) {
                val totalStepsSinceBoot = event.values[0].toInt()
                val now = System.currentTimeMillis()
                val existingActivity = dao.getDailyStepByDate(todayDate)
                val timeDelta = now - lastStepTimestamp
                if (existingActivity != null) {
                    stepDifference +=  (totalStepsSinceBoot - existingActivity.lastSensorValue).toInt()
                    Timber.Forest.d("Step Difference: $stepDifference")
                    val dailySteps = if (totalStepsSinceBoot > 0) {
                        if (existingActivity.manualSteps > 0) {
                            existingActivity.manualSteps + abs(totalStepsSinceBoot - existingActivity.baseline.toInt())
                        } else {
                            abs(totalStepsSinceBoot - existingActivity.baseline.toInt())
                        }
                    } else {
                        existingActivity.steps
                    }
                    if(stepDifference > 10){
                        val calorie = calculateCalories(
                            dailySteps,
                            userProfileStore.getWeightWithUnit().second,
                            userProfileStore.getGender(),
                            userProfileStore.getWeightWithUnit().first
                        )
                        val distanceTravelled = calculateDistance(
                            dailySteps,
                            userProfileStore.getHeightWithUnit().second.toDouble(),
                            userProfileStore.getWeightWithUnit().first
                        )
                        Timber.Forest.d("Calorie: $calorie")
                        Timber.Forest.d("Distance: $distanceTravelled")
                        dao.insertDailyStep(
                            existingActivity.copy(
                                kcal = calorie,
                                distance = distanceTravelled,
                                steps = dailySteps,
                                lastSensorValue = totalStepsSinceBoot.toLong(),
                                timeTaken = existingActivity.timeTaken + timeDelta,
                            )
                        )
                        stepDifference = 0
                    }else{
                        dao.insertDailyStep(
                            existingActivity.copy(
                                steps = dailySteps,
                                lastSensorValue = totalStepsSinceBoot.toLong(),
                                timeTaken = existingActivity.timeTaken + timeDelta,
                            )
                        )
                    }


                } else {
                    val yesterdayActivity = dao.getDailyStepByDate(getDaysAgoDate(1))
                    val newActivity = DailyStep(
                        date = todayDate,
                        steps = 0,
                        stepGoal = userProfileStore.getStep().first(),
                        lastSensorValue = totalStepsSinceBoot.toLong(),
                        timeTaken = 0,
                        baseline = yesterdayActivity?.lastSensorValue
                            ?: totalStepsSinceBoot.toLong(),
                        manualSteps = 0,
                        distance = 0.0,
                        kcal = 0
                    )

                    dao.insertDailyStep(newActivity)
                }

                lastStepTimestamp = now
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}