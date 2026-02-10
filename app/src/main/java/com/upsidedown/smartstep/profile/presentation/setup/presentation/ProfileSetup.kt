
package com.upsidedown.smartstep.profile.presentation.setup.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsidedown.smartstep.core.presentation.designsystem.components.button.SmartStepButton
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.upsidedown.smartstep.profile.presentation.setup.presentation.component.GenderOptionComponent
import com.upsidedown.smartstep.profile.presentation.setup.presentation.component.HeightOptionComponent
import com.upsidedown.smartstep.profile.presentation.setup.presentation.component.ProfileSetupTopBar
import com.upsidedown.smartstep.profile.presentation.setup.presentation.component.WeightOptionComponent
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileSetupRoot(
    viewModel: ProfileSetupViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileSetupScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ProfileSetupScreen(
    state: ProfileSetupState,
    onAction: (ProfileSetupAction) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            ProfileSetupTopBar(
                onSkipClick = {
                    onAction(ProfileSetupAction.OnSkip)
                }
            )
        }
    ) { innerPadding ->

        val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxSize()
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
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween

            ) {
               Column(
                   verticalArrangement = Arrangement.spacedBy(16.dp)
               ) {
                   Text(
                       text = "This information helps calculate your activity more accurately.",
                       style = MaterialTheme.typography.bodyLarge.copy(
                           textAlign = TextAlign.Center
                       ),
                       color = MaterialTheme.colorScheme.onSurface,
                       modifier = Modifier
                           .padding(
                               horizontal = 12.dp
                           )
                   )

                   //Box Form
                   Column(
                       modifier = Modifier
                           .background(Color.White, RoundedCornerShape(14.dp))
                           .border(
                               width = 1.dp,
                               color = MaterialTheme.colorScheme.outline,
                               shape = RoundedCornerShape(14.dp)
                           )
                           .fillMaxWidth()
                           .padding(16.dp),
                       verticalArrangement = Arrangement.spacedBy(8.dp)

                       ) {
                       GenderOptionComponent(
                           selectedGender = state.selectedGender,
                           onGenderSelected = {
                               onAction(ProfileSetupAction.OnChangeSelectedGender(it))
                           }
                       )
                       HeightOptionComponent(
                           isHeightMenuShown = state.isShowHeightMenu,
                           selectedHeightType = state.selectedHeightType,
                           heightTypeAsString = state.selectedHeightAsString(context),
                           onHeightTypeSelected = {
                               onAction(ProfileSetupAction.OnChangeHeightType(it))
                           },
                           onClick = {
                               onAction(ProfileSetupAction.OnClickHeightType)
                           },
                           onDismiss = {
                               onAction(ProfileSetupAction.OnDismissHeightTypeMenu)
                           }
                       )

                       WeightOptionComponent(
                           isWeightMenuShown = state.isShownWeightMenu,
                           selectedWeightType = state.selectedWeightType,
                            weightTypeAsString = state.selectedWeightAsString(context),
                           onWeightTypeSelected = {
                               onAction(ProfileSetupAction.OnChangeWeightType(it))
                           },
                           onClick = {
                               onAction(ProfileSetupAction.OnClickWeightType)
                           },
                           onDismiss = {
                               onAction(ProfileSetupAction.OnDismissWeightTypeMenu)

                           }
                       )

                   }
               }

                SmartStepButton(
                    text = "Start",
                    onClick = {
                        onAction(ProfileSetupAction.OnStartClick)
                    },
                    modifier = Modifier
                        .fillMaxWidth( )
                )

            }
        }
    }

}

@PreviewScreenSizes
@Composable
private fun Preview() {
    SmartStepTheme {
        ProfileSetupScreen(
            state = ProfileSetupState(),
            onAction = {}
        )
    }
}