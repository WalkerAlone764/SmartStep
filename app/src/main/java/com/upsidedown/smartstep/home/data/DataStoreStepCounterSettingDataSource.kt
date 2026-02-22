package com.upsidedown.smartstep.home.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.upsidedown.smartstep.home.domain.StepCounterSettingDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreStepCounterSettingDataSource(
    private val context: Context
) : StepCounterSettingDataSource {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "step_counter_preferences")

    companion object {
        private val IS_PAUSED = booleanPreferencesKey("is_paused")
    }

    override val isPaused: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            preferences[IS_PAUSED] ?: false
        }

    override suspend fun setPaused(isPaused: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_PAUSED] = isPaused
        }
    }
}
