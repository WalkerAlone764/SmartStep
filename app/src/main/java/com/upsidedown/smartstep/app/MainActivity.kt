package com.upsidedown.smartstep.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.upsidedown.smartstep.core.presentation.designsystem.components.SmartStepDropdown
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepHeightPicker
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepHeightType
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartStepTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    var selectedGender by remember { mutableStateOf("Female",) }
                    Box(modifier = Modifier
                        .background(Color.White)
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)) {
                        SmartStepDropdown(
                            label = "Gender",
                            selectedValue = selectedGender,
                            options = listOf("Female", "Male", "Other"),
                            onOptionSelected = { selectedGender = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartStepTheme {
        Greeting("Android")
    }
}