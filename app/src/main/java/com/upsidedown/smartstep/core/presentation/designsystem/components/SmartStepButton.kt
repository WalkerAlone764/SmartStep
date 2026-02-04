package com.upsidedown.smartstep.core.presentation.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.upsidedown.smartstep.core.presentation.designsystem.theme.bodyLargeMedium
import com.upsidedown.smartstep.core.presentation.designsystem.theme.bodyMediumMedium

sealed interface SmartStepButtonStyle {
    data object PRIMARY: SmartStepButtonStyle
    data object SECONDARY: SmartStepButtonStyle
    data object TEXT: SmartStepButtonStyle
}
@Composable
fun SmartStepButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: SmartStepButtonStyle = SmartStepButtonStyle.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val buttonColor = when (style) {
        SmartStepButtonStyle.PRIMARY -> ButtonDefaults.buttonColors()
        SmartStepButtonStyle.SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
        SmartStepButtonStyle.TEXT -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary
        )
    }

    val borderStyle = when {
        style == SmartStepButtonStyle.SECONDARY -> {
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
            )
        }
        else -> null
    }

//    if (style == SmartStepButtonStyle.TEXT) {
//        TextButton(
//            onClick = onClick
//        ) {
//            Text(
//                text = text,
//                style = MaterialTheme.typography.bodyLargeMedium
//            )
//        }
//    } else {

        

        Button(
            onClick = onClick,
            enabled = enabled,
            shape = RoundedCornerShape(10.dp),
            colors = buttonColor,
            border = borderStyle,
            modifier = modifier
        ) {

            Box {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(15.dp)
                        .alpha(if (isLoading) 1f else 0f),
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLargeMedium,
                    modifier = Modifier
                        .alpha(if (isLoading) 0f else 1f)
                )
            }
        }
//    }

}

@Preview(showBackground = true)
@Composable
private fun PrimaryPreview() {
    SmartStepTheme {
        SmartStepButton(
            text = "Hello",
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SecondaryPreview() {
    SmartStepTheme {
        SmartStepButton(
            text = "Hello",
            onClick = {},
            style = SmartStepButtonStyle.SECONDARY,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextPreview() {
    SmartStepTheme {
        SmartStepButton(
            text = "Hello",
            onClick = {},
            style = SmartStepButtonStyle.TEXT,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}

