package com.upsidedown.smartstep.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes: NavKey {

    @Serializable
    data object ProfileSetup: Routes

    @Serializable
    data object Home: Routes
}