package com.upsidedown.smartstep.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsidedown.smartstep.app.navigation.NavigationRoot
import com.upsidedown.smartstep.app.navigation.Routes
import com.upsidedown.smartstep.core.presentation.designsystem.components.SmartStepDropdown
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepHeightPicker
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepHeightType
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.upsidedown.smartstep.profile.presentation.setup.presentation.ProfileSetupRoot
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartStepTheme {
                val viewmodel = koinViewModel<MainViewModel>()
                val state by viewmodel.state.collectAsStateWithLifecycle()

                AnimatedContent(
                    state.startDestination
                ) { destination ->
                    if (destination == null) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize()
                        )
                    } else {
                        NavigationRoot(
                            startRoutes = destination
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