package com.vs.smartstep.main.data

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.vs.smartstep.R
import com.vs.smartstep.app.MainActivity
import com.vs.smartstep.core.room.DailyStepDao
import com.vs.smartstep.main.domain.StepProvider
import com.vs.smartstep.main.domain.userProfileStore
import com.vs.smartstep.main.presentation.util.getTodayDate
import com.vs.smartstep.main.presentation.util.toCommaString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber


class StepService : Service() , KoinComponent {
    private val userProfileStore : userProfileStore by inject()
    private  val dao : DailyStepDao by inject()
    private val stepDetector : StepProvider by inject()
    private val scope = CoroutineScope(Dispatchers.Main )

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> {
                stepDetector.startListening()
                start()
            }
            Actions.STOP.toString() -> {
                stopForeground(true)
                stopSelf()
                return START_NOT_STICKY
            }
        }
      return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("RemoteViewLayout")
    private fun start() {


            val mainIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }


            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )



                scope.launch (Dispatchers.Default) {
                    combine(
                        dao.getDailyStepByDateFlow(getTodayDate()),
                        userProfileStore.getStep()
                    ) { item, goal ->
                        Timber.d(buildString {
                            append(item.toString())
                            append(goal.toString())
                        })
                        updateNotification(
                            pendingIntent,
                            steps = item?.steps?:0,
                            kcal = item?.kcal?:0,
                            goal = goal
                        )
                    }.collect()

            }

    }
    private fun updateNotification(pendingIntent: PendingIntent,steps: Int, kcal: Int, goal: Int) {
        val collapsedView = RemoteViews(packageName, R.layout.notification_collapsed)
        val expandedView = RemoteViews(packageName, R.layout.notifiication_expand)


        collapsedView.setTextViewText(R.id.tvStepCount, steps.toCommaString())
        expandedView.setTextViewText(R.id.tvStepCount, steps.toCommaString())
        collapsedView.setTextViewText(R.id.tvCalorieCount, kcal.toCommaString())
        expandedView.setTextViewText(R.id.tvCalorieCount, kcal.toCommaString())

        expandedView.setProgressBar(R.id.notificationProgress, goal, steps, false)
        collapsedView.setProgressBar(R.id.notificationProgress, goal, steps, false)


        expandedView.setOnClickPendingIntent(R.id.btnOpenApp, pendingIntent)

        val notification = NotificationCompat.Builder(this, "step")
            .setSmallIcon(R.drawable.steps)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification)
        startForeground(1, notification)
    }
    enum class Actions {
        START, STOP
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel() // Cancel all coroutines when service stops
    }
}