package com.upsidedown.smartstep.home.presentation.step_counter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upsidedown.smartstep.R
import com.upsidedown.smartstep.app.navigation.SmartStepMenu
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.upsidedown.smartstep.core.presentation.util.ObserveAsEvents
import com.upsidedown.smartstep.home.presentation.step_counter.components.SmartStepExitDialog
import com.upsidedown.smartstep.home.presentation.step_counter.components.StepCounterCard
import com.upsidedown.smartstep.home.presentation.step_counter.components.StepCounterTopAppBar
import kotlinx.coroutines.launch

@Composable
fun StepCounterRoot(
    viewModel: StepCounterViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) { event ->
        when(event) {
            StepCounterEvent.ExitApp -> {
                //TODO: Handle exit
            }
        }

    }

    StepCounterScreen(
        state = state,
        onAction = { action ->
            viewModel.onAction(action)
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
        drawerState = drawerState,
        onStepGoalClick = { },
        onPersonalSettingsClick = { },
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
            StepCounterCard(
                currentSteps = state.currentSteps,
                formattedCurrentSteps = state.formattedCurrentSteps,
                goalSteps = state.goalSteps,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .wrapContentSize()
            )
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
}

@PreviewScreenSizes
@Composable
private fun Preview() {
    SmartStepTheme {
        StepCounterScreen(
            state = StepCounterState(
                currentSteps = 4523,
                goalSteps = 6000
            ),
            onAction = {}
        )
    }
}
