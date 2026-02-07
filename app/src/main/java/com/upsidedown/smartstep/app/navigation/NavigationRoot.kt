package com.upsidedown.smartstep.app.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.upsidedown.smartstep.profile.presentation.setup.presentation.ProfileSetupRoot

@Composable
fun NavigationRoot(
    startRoutes: Routes,
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(startRoutes)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {

            entry<Routes.ProfileSetup> {
                ProfileSetupRoot()
            }

            entry<Routes.Home> {
                Text(
                    text = "Home",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            }

        }
    )

}