package com.upsidedown.smartstep.home.presentation.step_counter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsidedown.smartstep.R
import com.upsidedown.smartstep.app.navigation.SmartStepMenu
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepDatePicker
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.upsidedown.smartstep.core.presentation.util.ObserveAsEvents
import com.upsidedown.smartstep.home.data.StepCounterService
import com.upsidedown.smartstep.home.presentation.step_counter.StepCounterAction.*
import com.upsidedown.smartstep.home.presentation.step_counter.components.DailyAverageCard
import com.upsidedown.smartstep.home.presentation.step_counter.components.DailyStep
import com.upsidedown.smartstep.home.presentation.step_counter.components.EditStepsDialog
import com.upsidedown.smartstep.home.presentation.step_counter.components.IgnoreBatteryOptimizationPermissionDialog
import com.upsidedown.smartstep.home.presentation.step_counter.components.MotionSenorDialog
import com.upsidedown.smartstep.home.presentation.step_counter.components.PhysicalActivityPermissionDialog
import com.upsidedown.smartstep.home.presentation.step_counter.components.SelectStepGoalDialog
import com.upsidedown.smartstep.home.presentation.step_counter.components.SmartStepExitDialog
import com.upsidedown.smartstep.home.presentation.step_counter.components.StepCounterCard
import com.upsidedown.smartstep.home.presentation.step_counter.components.StepCounterTopAppBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun StepCounterRoot(
    onClickPersonalSetting: () -> Unit,
    viewModel: StepCounterViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = LocalActivity.current

    val physicalActivityPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            Log.d("Permission", "Body Sensor permission launcher $isGranted")
            viewModel.onAction(StepCounterAction.OnResultRequestingPhysicalActivity(isGranted))
        }
    )

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            Log.d("Permission", "Notification permission launcher $isGranted")
            viewModel.onAction(StepCounterAction.OnResultRequestingNotificationPermission(isGranted))
        }
    )

    ObserveAsEvents(viewModel.event) { event ->
        when(event) {
            StepCounterEvent.ExitApp -> {
                val intent = Intent(context, StepCounterService::class.java).apply {
                    action = StepCounterService.ACTION_STOP
                }
                context.startService(intent)
                activity?.finish()
            }

            StepCounterEvent.CheckAllPermission -> {
                val activityRecognitionResult = context.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED
                Log.d("Permission", "Activity Recognition permission $activityRecognitionResult")
                val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                val ignoreBatteryOptimizationResult = powerManager.isIgnoringBatteryOptimizations(context.packageName)
                Log.d("Permission", "ignoreBatteryOptimizationResult permission $ignoreBatteryOptimizationResult")
                
                val notificationPermissionResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                } else {
                    true
                }

                viewModel.onAction(
                    OnCheckPermissionResult(
                        activityRecognitionResult = activityRecognitionResult,
                        backgroundLocationResult = ignoreBatteryOptimizationResult,
                        notificationPermissionResult = notificationPermissionResult
                    )
                )
                
                if (activityRecognitionResult && ignoreBatteryOptimizationResult) {
                    val intent = Intent(context, StepCounterService::class.java).apply {
                        action = StepCounterService.ACTION_START
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(intent)
                    } else {
                        context.startService(intent)
                    }
                }
            }

            StepCounterEvent.RequestPhysicalActivityPermission -> {
                physicalActivityPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            }

            StepCounterEvent.CheckCanShowRequestPhysicalActivityPermission -> {

                val canShowRationale =  activity?.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.ACTIVITY_RECOGNITION
                    )
                } ?: false

                Log.d("Physical Activity",canShowRationale.toString())
               viewModel.onAction(
                   OnCheckCanShowRationaleForPhysicalActivity(
                      canShowRationale
                   )
               )
            }

            StepCounterEvent.OpenSetting -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }

            StepCounterEvent.RequestForBatteryOptimization -> {
                context.requestForBatteryOptimization()
                val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                val ignoreBatteryOptimizationResult = powerManager.isIgnoringBatteryOptimizations(context.packageName)
               if (ignoreBatteryOptimizationResult) {
                   val intent = Intent(context, StepCounterService::class.java).apply {
                       action = StepCounterService.ACTION_START
                   }
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                       context.startForegroundService(intent)
                   } else {
                       context.startService(intent)
                   }
               }
            }
            
            StepCounterEvent.RequestNotificationPermission -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    viewModel.onAction(StepCounterAction.OnResultRequestingNotificationPermission(true))
                }
            }

            StepCounterEvent.StartStepCounterService -> {
                val intent = Intent(context, StepCounterService::class.java).apply {
                    action = StepCounterService.ACTION_START
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            }
            
            StepCounterEvent.StopStepCounterService -> {
                val intent = Intent(context, StepCounterService::class.java).apply {
                    action = StepCounterService.ACTION_STOP
                }
                context.startService(intent)
            }
        }

    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { state, event ->

            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onAction(StepCounterAction.OnResume)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    StepCounterScreen(
        state = state,
        onAction = { action ->
            when(action) {
                StepCounterAction.OnClickPersonalSettingMenu -> {
                    onClickPersonalSetting()
                }
                else -> viewModel.onAction(action)
            }

        }
    )
}

@Composable
fun StepCounterScreen(
    state: StepCounterState,
    onAction: (StepCounterAction) -> Unit,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    SmartStepMenu(
        shouldShowStepCountingIssue = !state.hasIgnoreBatteryOptimizationPermission,
        drawerState = drawerState,
        onFixStopCountingStep = {
            scope.launch {
                drawerState.close()
                onAction(StepCounterAction.OnClickFixStopCountingStep)
            }
        },
        onStepGoalClick = {
            scope.launch {
                drawerState.close()
                onAction(StepCounterAction.OnClickStepGoalMenu)
            }
        },
        onPersonalSettingsClick = {
            scope.launch {
                drawerState.close()
                onAction(StepCounterAction.OnClickPersonalSettingMenu)
            }
        },
        onEditStepsClick = {
            scope.launch {
                drawerState.close()
                onAction(StepCounterAction.OnClickEditSteps)
            }
        },
        onExitClick = {
            onAction(StepCounterAction.OnClickExit)
            scope.launch {
                drawerState.close()
            }
        },
    ) {
        Scaffold(
            topBar = {
                StepCounterTopAppBar(
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                StepCounterCard(
                    currentSteps = state.currentSteps,
                    formattedCurrentSteps = state.formattedCurrentSteps,
                    goalSteps = state.goalSteps,
                    isPaused = state.isPaused,
                    distance = state.distance,
                    distanceUnit = state.distanceUnit,
                    calories = state.calories,
                    timeMin = state.timeMin,
                    onEditClick = { onAction(StepCounterAction.OnClickStepGoalMenu) },
                    onPausePlayClick = { onAction(StepCounterAction.OnClickPauseResume) },
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                AnimatedVisibility(state.pastSevenDaysSteps.isNotEmpty()) {
                    val dailySteps by remember(state.pastSevenDaysSteps) {
                        derivedStateOf {
                            state.pastSevenDaysSteps.map { map ->
                                val date = map["date"] as LocalDate
                                val steps = map["step"] as Int
                                DailyStep(
                                    day = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                                    steps = steps
                                )
                            }
                        }
                    }
                    val averageSteps by remember(dailySteps) {
                        derivedStateOf {
                            if (dailySteps.isNotEmpty()) dailySteps.sumOf { it.steps } / dailySteps.size else 0
                        }
                    }

                    DailyAverageCard(
                        dailySteps = dailySteps,
                        averageSteps = averageSteps,
                        goalSteps = state.goalSteps,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (state.isExitDialogShown) {
        SmartStepExitDialog(
            topIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_power),
                    contentDescription = null
                )
            },
            content = {
                Text(
                    text = stringResource(R.string.exit_dialog_message),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        textAlign = TextAlign.Center
                    )
                )
            },
            onConfirm = {
                onAction(StepCounterAction.OnClickOk)
            },
            onDismissRequest = {
                onAction(StepCounterAction.OnDismissCloseDialog)
            }
        )
    }

    if (state.isActivityRecognitionPermissionRationaleDialogShown) {
        MotionSenorDialog(
            onDismiss = {
            },
            onClickAllow = {
                onAction(StepCounterAction.OnClickAllowPhysicalActivity)
            }
        )
    }

    if (state.isActivityRecognitionPermissionSettingDialogShown) {
        PhysicalActivityPermissionDialog(
            onDismiss = {
            },
            onClickOpenSettings = {
                onAction(StepCounterAction.OnClickOpenSettings)
            }
        )
    }


    if (state.isIgnoreBatteryOptimizationDialogShown) {
        IgnoreBatteryOptimizationPermissionDialog(
            onDismiss = {
                onAction(StepCounterAction.OnDismissIgnoreBatteryOptimizationDialog)
            },
            onClickAllow = {
                onAction(StepCounterAction.OnClickAllowIgnoreBatteryOptimization)
            }
        )
    }

    if (state.isStepGoalSelectionDialogShown) {
        SelectStepGoalDialog(
            initialValue = state.goalSteps,
            onDismiss = {
                onAction(StepCounterAction.OnDismissStepGoalSelectionDialog)
            },
            onSave = {
                onAction(StepCounterAction.OnSaveStepGoal(it))
            }
        )
    }

    if (state.isEditStepsDialogShown) {
        EditStepsDialog(
            steps = state.stepsInEdit,
            onDismiss = {
                onAction(StepCounterAction.OnDismissEditStepsDialog)
            },
            onClickDate = {
                onAction(StepCounterAction.OnClickDate)
            },
            onSave = { date, time, steps ->
                onAction(StepCounterAction.OnSaveEditSteps(date, time, steps))
            },
            date = state.formattedSelectedDate,
            showTimeColumn = false
        )
    }

    if (state.isDatePickerDialogShown) {
        Dialog(onDismissRequest = { onAction(StepCounterAction.OnDismissDatePickerDialog) }) {
            SmartStepDatePicker(
                initialDate = state.selectedDateInEdit,
                onDismiss = { onAction(StepCounterAction.OnDismissDatePickerDialog) },
                onConfirm = { onAction(StepCounterAction.OnDateSelected(it)) }
            )
        }
    }
}

private fun Context.requestForBatteryOptimization() {
    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
        data = Uri.fromParts("package", packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

@PreviewScreenSizes
@Composable
private fun Preview() {
    SmartStepTheme {
        StepCounterScreen(
            state = StepCounterState(
                isPaused = false,
                currentSteps = 4523,
                goalSteps = 6000
            ),
            onAction = {}
        )
    }
}
