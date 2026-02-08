package com.upsidedown.smartstep.app.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsidedown.smartstep.R
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun SmartStepMenu(
    drawerState: DrawerState,
    onStepGoalClick: () -> Unit,
    onPersonalSettingsClick: () -> Unit,
    onExitClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                drawerContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.width(280.dp)
            ) {
                Column(
                    modifier = modifier
                        .fillMaxHeight()
                        .padding(vertical = 16.dp)
                ) {
                    SmartStepMenuItem(
                        text = stringResource(id = R.string.step_goal),
                        onClick = onStepGoalClick
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    SmartStepMenuItem(
                        text = stringResource(id = R.string.personal_settings),
                        onClick = onPersonalSettingsClick
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    SmartStepMenuItem(
                        text = stringResource(id = R.string.exit),
                        onClick = onExitClick,
                        textColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    ) {
        content()
    }
}

@Composable
private fun SmartStepMenuItem(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = textColor,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun SmartStepMenuPreview() {
    SmartStepTheme {
        SmartStepMenu(
            drawerState = rememberDrawerState(
                initialValue = DrawerValue.Open
            ),
            onStepGoalClick = {},
            onPersonalSettingsClick = {},
            onExitClick = {}
        )
    }
}
