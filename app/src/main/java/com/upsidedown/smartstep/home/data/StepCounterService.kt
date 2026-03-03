package com.upsidedown.smartstep.home.data

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.app.ServiceCompat
import com.upsidedown.smartstep.R
import com.upsidedown.smartstep.app.MainActivity
import com.upsidedown.smartstep.home.domain.StepDataSource
import com.upsidedown.smartstep.home.domain.StepRepository
import com.upsidedown.smartstep.profile.domain.HeightUnit
import com.upsidedown.smartstep.profile.domain.ProfileDataSource
import com.upsidedown.smartstep.profile.domain.WeightUnit
import com.upsidedown.smartstep.core.presentation.util.Gender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.LocalDate
import kotlin.math.roundToInt

class StepCounterService : Service() {
    private val stepDataSource: StepDataSource by inject()
    private val stepRepository: StepRepository by inject()
    private val profileDataSource: ProfileDataSource by inject()
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "step_counter_channel"

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return START_STICKY
    }

    private fun start() {
        // Initial notification to start foreground
        val notification = createNotification(0, 0, 6000)
        startForeground(NOTIFICATION_ID, notification)

        serviceScope.launch {
            stepDataSource.listenSteps()
        }

        serviceScope.launch {
            combine(
                stepRepository.getAllStepsByDate(LocalDate.now()),
                profileDataSource.profile
            ) { steps, profile ->
                val totalSteps = steps.sumOf { it.count }
                val goalSteps = 6000 // Default or from profile if available
                
                // Calculate calories (simplified logic from ViewModel)
                val weightKg = if (profile?.weightUnit == WeightUnit.LBS) {
                    (profile.weight / 2.20462).roundToInt()
                } else {
                    profile?.weight ?: 70
                }
                val genderFactor = if (profile?.gender == Gender.FEMALE) 0.9 else 1.0
                val kcalPerStep = weightKg * 0.0005 * genderFactor
                val calories = (totalSteps * kcalPerStep).roundToInt()
                
                Triple(totalSteps, calories, goalSteps)
            }.collect { (steps, calories, goal) ->
                updateNotification(steps, calories, goal)
            }
        }
    }

    private fun updateNotification(steps: Int, calories: Int, goal: Int) {
        val notification = createNotification(steps, calories, goal)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotification(steps: Int, calories: Int, goal: Int): android.app.Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val collapsedView = RemoteViews(packageName, R.layout.notification_collapsed)
        collapsedView.setTextViewText(R.id.notification_steps, String.format("%,d", steps))
        collapsedView.setTextViewText(R.id.notification_calories, calories.toString())
        collapsedView.setProgressBar(R.id.notification_progress, goal, steps, false)

        val expandedView = RemoteViews(packageName, R.layout.notification_expanded)
        expandedView.setTextViewText(R.id.notification_steps, String.format("%,d", steps))
        expandedView.setTextViewText(R.id.notification_calories, calories.toString())
        expandedView.setProgressBar(R.id.notification_progress, goal, steps, false)
        expandedView.setOnClickPendingIntent(R.id.notification_action_button, pendingIntent)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_shoe)
            .setVisibility(VISIBILITY_PUBLIC)
            .setColor(Color.parseColor("#3A43B6"))
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setOngoing(true)
            .setSilent(true)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun stop() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}
