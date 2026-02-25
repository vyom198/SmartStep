package com.vs.smartstep.main.data

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.vs.smartstep.R
import com.vs.smartstep.core.room.DailyStepDao
import com.vs.smartstep.main.domain.userProfileStore
import com.vs.smartstep.main.presentation.util.getTodayDate
import com.vs.smartstep.main.presentation.util.toCommaString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue


class StepService : Service() , KoinComponent {
    private val scope : CoroutineScope by inject()
    private val userProfileStore : userProfileStore by inject()
    private  val dao : DailyStepDao by inject()
    private val _calorie = MutableStateFlow(0)
    val calorie: StateFlow<Int> = _calorie.asStateFlow()

    private val _stepCount = MutableStateFlow(0)
    val stepCount: StateFlow<Int> = _stepCount.asStateFlow()


    private val _dailyGoal = MutableStateFlow(10000)
    val dailyGoal: StateFlow<Int> = _dailyGoal.asStateFlow()

    private fun loadData(){
        scope.launch {
            userProfileStore.getStep().collect { dailyGoal->
                _dailyGoal.update {
                    dailyGoal
                }


            }
        }
        scope.launch {
            dao.getDailyStepByDateFlow(getTodayDate()).collect { item ->
                _stepCount.update {
                    item.steps
                }
                _calorie.update {
                    item.kcal
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> {
                start()
            }
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("RemoteViewLayout")
    private fun start() {
        loadData()

        val collapsedView = RemoteViews(packageName, R.layout.notification_collapsed)
        val expandedView = RemoteViews(packageName, R.layout.notifiication_expand)

        // Set the step count dynamically
        collapsedView.setTextViewText(R.id.tvStepCount, stepCount.value.toCommaString())
        expandedView.setTextViewText(R.id.tvStepCount, stepCount.value.toCommaString())
        collapsedView.setTextViewText(R.id.tvCalorieCount, calorie.value.toCommaString())
        expandedView.setTextViewText(R.id.tvCalorieCount, calorie.value.toCommaString())
        // Set progress bar (e.g., 7842 steps out of a 10,000 goal)
        collapsedView.setProgressBar(R.id.notificationProgress, dailyGoal.value, stepCount.value, false)
        val notification = NotificationCompat.Builder(this, "step")
            .setSmallIcon(R.drawable.steps)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .build()

        startForeground(1, notification)
    }

    enum class Actions {
        START, STOP
    }
}