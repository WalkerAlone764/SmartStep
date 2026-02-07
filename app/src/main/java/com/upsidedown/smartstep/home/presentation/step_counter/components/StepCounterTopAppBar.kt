@file:OptIn(ExperimentalMaterial3Api::class)

package com.upsidedown.smartstep.home.presentation.step_counter.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsidedown.smartstep.R
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun StepCounterTopAppBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Text(
                text = stringResource(id = R.string.smart_step),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.menu),
                    contentDescription = null,
                    modifier = Modifier
                )
            }
        }
    )

}

@Preview
@Composable
private fun StepCounterTopAppBarPreview() {
    SmartStepTheme {
        StepCounterTopAppBar(
            onClick = {}
        )
    }
}