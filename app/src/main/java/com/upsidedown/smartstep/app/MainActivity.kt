package com.upsidedown.smartstep.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsidedown.smartstep.app.navigation.NavigationRoot
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartStepTheme {
                val viewmodel = koinViewModel<MainViewModel>()
                val state by viewmodel.state.collectAsStateWithLifecycle()

                splashScreen.setKeepOnScreenCondition {
                    state.startDestination == null
                }

                AnimatedContent(
                    state.startDestination,
                    label = "start_destination_animation"
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
    androidx.compose.material3.Text(
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