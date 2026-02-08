package com.upsidedown.smartstep.home.presentation.step_counter.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.upsidedown.smartstep.R
import com.upsidedown.smartstep.core.presentation.designsystem.components.button.SmartStepButton
import com.upsidedown.smartstep.core.presentation.designsystem.components.layout.DialogLayout
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun MotionSenorDialog(
    onDismiss: () -> Unit,
    onClickAllow: () -> Unit,
) {
    DialogLayout(
        onDismiss = onDismiss,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(64.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.location_direction),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "To count your steps,\nSmart Step needs access to your\nmotion sensors.",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(32.dp))

        SmartStepButton(
            text = "Allow access",
            onClick = onClickAllow,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@PreviewScreenSizes
@Composable
private fun Preview() {
    SmartStepTheme {
        MotionSenorDialog(
            onDismiss = {},
            onClickAllow = {}
        )
    }
}
