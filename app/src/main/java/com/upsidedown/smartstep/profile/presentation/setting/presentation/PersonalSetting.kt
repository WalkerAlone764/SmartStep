package com.upsidedown.smartstep.profile.presentation.setting.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsidedown.smartstep.core.presentation.designsystem.components.button.SmartStepButton
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepHeightType
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepWeightType
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.upsidedown.smartstep.core.presentation.util.ObserveAsEvents
import com.upsidedown.smartstep.profile.presentation.components.GenderOptionComponent
import com.upsidedown.smartstep.profile.presentation.components.HeightOptionComponent
import com.upsidedown.smartstep.profile.presentation.components.WeightOptionComponent
import com.upsidedown.smartstep.profile.presentation.setting.presentation.component.PersonalSettingTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun PersonalSettingRoot(
    onSuccessfullySave: () -> Unit,
    viewModel: PersonalSettingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) { event ->
        when(event) {
            PersonalSettingEvent.OnSuccessfullySave -> onSuccessfullySave()
        }
    }

    PersonalSettingScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun PersonalSettingScreen(
    state: PersonalSettingState,
    onAction: (PersonalSettingAction) -> Unit,
) {
    Scaffold(
        topBar = {
            PersonalSettingTopBar()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
                .padding(
                    horizontal = 16.dp
                )
                .padding(
                    top = 16.dp
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(
                        max = 394.dp
                    )
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GenderOptionComponent(
                        selectedGender = state.selectedGender,
                        onGenderSelected = {
                            onAction(PersonalSettingAction.OnGenderSelected(it))
                        }
                    )

                    HeightOptionComponent(
                        isHeightMenuShown = state.isHeightMenuShown,
                        selectedHeightType = state.selectedHeightType,
                        heightTypeAsString = state.selectedHeightType.let {
                            when (it) {
                                is SmartStepHeightType.CM -> "${it.selectedValue} cm"
                                is SmartStepHeightType.FtInch -> "${it.selectedFt} ft ${it.selectedInch} in"
                            }
                        },
                        onHeightTypeSelected = {
                            onAction(PersonalSettingAction.OnHeightSelected(it))
                        },
                        onClick = {
                            onAction(PersonalSettingAction.OnShowHeightMenu(true))
                        },
                        onDismiss = {
                            onAction(PersonalSettingAction.OnShowHeightMenu(false))
                        }
                    )

                    WeightOptionComponent(
                        isWeightMenuShown = state.isWeightMenuShown,
                        selectedWeightType = state.selectedWeightType,
                        weightTypeAsString = state.selectedWeightType.let {
                            when (it) {
                                is SmartStepWeightType.KG -> "${it.selectedKg} kg"
                                is SmartStepWeightType.LBS -> "${it.selectedLbs} lbs"
                            }
                        },
                        onWeightTypeSelected = {
                            onAction(PersonalSettingAction.OnWeightSelected(it))
                        },
                        onClick = {
                            onAction(PersonalSettingAction.OnShowWeightMenu(true))
                        },
                        onDismiss = {
                            onAction(PersonalSettingAction.OnShowWeightMenu(false))
                        }
                    )
                }

                Column(
                    verticalArrangement = Arrangement.Bottom
                ) {
                    SmartStepButton(
                        text = "Save",
                        onClick = {
                            onAction(PersonalSettingAction.OnClickSave)
                        },
                        modifier = Modifier
                            .fillMaxWidth()

                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SmartStepTheme {
        PersonalSettingScreen(
            state = PersonalSettingState(),
            onAction = {}
        )
    }
}