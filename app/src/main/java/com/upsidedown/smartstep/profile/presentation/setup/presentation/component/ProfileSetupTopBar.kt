@file:OptIn(ExperimentalMaterial3Api::class)

package com.upsidedown.smartstep.profile.presentation.setup.presentation.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsidedown.smartstep.R
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepWeightType
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.upsidedown.smartstep.core.presentation.designsystem.theme.bodyLargeMedium
import com.upsidedown.smartstep.core.presentation.util.DeviceConfiguration
import com.upsidedown.smartstep.core.presentation.util.currentDeviceConfiguration

@Composable
fun ProfileSetupTopBar(
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val deviceConfiguration = currentDeviceConfiguration()

    val background = when(deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> Color.White
        else -> Color.Transparent
    }
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = background
        ),
        title = {
            Text(
                text = stringResource(id = R.string.my_profile),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        actions = {
            TextButton(
                onClick = onSkipClick,
            ) {
                Text(
                    text = stringResource(id = R.string.skip),
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Preview
@Composable
private fun ProfileSetupTopBarPreview() {
    SmartStepTheme {
        ProfileSetupTopBar(
            onSkipClick = {}
        )
    }
}
