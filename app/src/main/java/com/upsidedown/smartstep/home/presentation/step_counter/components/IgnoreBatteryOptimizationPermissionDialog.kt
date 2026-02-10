package com.upsidedown.smartstep.home.presentation.step_counter.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsidedown.smartstep.core.presentation.designsystem.components.button.SmartStepButton
import com.upsidedown.smartstep.core.presentation.designsystem.components.layout.DialogLayout
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun IgnoreBatteryOptimizationPermissionDialog(
    onDismiss: () -> Unit,
    onClickAllow: () -> Unit,
    modifier: Modifier = Modifier
) {
    DialogLayout(
        onDismiss = onDismiss,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Background access recommended",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Background access helps Smart Step track your activity more reliably.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.inverseOnSurface
        )
        Spacer(modifier = Modifier.height(32.dp))
        SmartStepButton(
            text = "Continue",
            onClick = onClickAllow,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SmartStepTheme {
        IgnoreBatteryOptimizationPermissionDialog(
            onDismiss = {},
            onClickAllow = {}
        )
    }
}
