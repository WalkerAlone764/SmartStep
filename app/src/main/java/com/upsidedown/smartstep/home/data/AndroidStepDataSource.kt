package com.upsidedown.smartstep.home.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.upsidedown.smartstep.core.database.data.dao.StepDao
import com.upsidedown.smartstep.home.domain.StepDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.LocalDate
import kotlin.coroutines.resume

class AndroidStepDataSource(
    private val applicationScope: CoroutineScope,
    private val dao: StepDao,
    private val context: Context
): StepDataSource {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    override suspend fun listenSteps() = suspendCancellableCoroutine { continuation ->
        Log.d("TAG", "Registering sensor listener... ")

        val listener: SensorEventListener by lazy {
            object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event == null) return

                    val steps = event.values[0].toLong()
                    Log.d("AndroidStepDataSource suspend", "Steps since last reboot: $steps")
                    applicationScope.launch(Dispatchers.IO) {
                        dao.increaseStepCount(LocalDate.now(), steps.toInt())
                    }
                        continuation.resume(steps)

                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    Log.d("TAG", "Accuracy changed to: $accuracy")
                }
            }
        }

        val supportedAndEnabled = sensorManager.registerListener(listener,
            sensor, SensorManager.SENSOR_DELAY_UI)
        Log.d("AndroidStepDataSource suspend", "Sensor listener registered: $supportedAndEnabled")

        continuation.invokeOnCancellation {
            Log.d("TAG", "Unregistering sensor listener... ")
            sensorManager.unregisterListener(listener)

        }
    }
}