package com.upsidedown.smartstep.core.presentation.designsystem.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.upsidedown.smartstep.core.presentation.designsystem.components.button.SmartStepButton
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.upsidedown.smartstep.core.presentation.util.DeviceConfiguration
import com.upsidedown.smartstep.core.presentation.util.currentDeviceConfiguration

@Composable
fun DialogLayout(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val windowSizeClass = currentDeviceConfiguration()

    when(windowSizeClass) {
        DeviceConfiguration.MOBILE_PORTRAIT,
        DeviceConfiguration.MOBILE_LANDSCAPE -> BottomSheetStyleContent(
            modifier = modifier,
            onDismiss = onDismiss,
            content = content
        )
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> CenterSheetStyle(
            modifier = modifier,
            onDismiss = onDismiss,
            content = content
        )
    }

}

@Composable
private fun BottomSheetStyleContent(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {

    Box(
        modifier = modifier
        .background(Color.Black.copy(0.45f))
            .pointerInput(Unit) {
                detectTapGestures {
                    onDismiss()
                }
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(
                    topStart = 28.dp,
                    topEnd = 28.dp
                ))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}

@Composable
private fun CenterSheetStyle(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(Color.Black.copy(0.45f))
            .pointerInput(Unit) {
                detectTapGestures {
                    onDismiss()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(
                    max = 312.dp
                )
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}

@Composable
@PreviewScreenSizes
fun DialogLayoutPreview() {
    SmartStepTheme() {
       Column(
           modifier = Modifier
               .fillMaxSize()
               .background(MaterialTheme.colorScheme.background)
       ) {
           DialogLayout(
               onDismiss = { },
               modifier = Modifier
                   .fillMaxSize( )
           ) {
               Text(
                   text = "hello world"
               )

               SmartStepButton(
                   text = "Button",
                   onClick = {},
                   modifier = Modifier.fillMaxWidth()
               )
           }
       }
    }
}