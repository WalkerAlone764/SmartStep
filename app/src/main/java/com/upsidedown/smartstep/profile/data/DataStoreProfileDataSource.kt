package com.upsidedown.smartstep.profile.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.upsidedown.smartstep.core.presentation.util.Gender
import com.upsidedown.smartstep.profile.domain.HeightUnit
import com.upsidedown.smartstep.profile.domain.Profile
import com.upsidedown.smartstep.profile.domain.ProfileDataSource
import com.upsidedown.smartstep.profile.domain.WeightUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreProfileDataSource(
    private val context: Context
): ProfileDataSource {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_preferences")

    companion object {
        private val GENDER = stringPreferencesKey("gender")
        private val HEIGHT_CM = intPreferencesKey("height_cm")
        private val WEIGHT_KG = intPreferencesKey("weight_kg")
        private val HEIGHT_UNIT = stringPreferencesKey("height_unit")
        private val WEIGHT_UNIT = stringPreferencesKey("weight_unit")

        private val IS_SETUP_VISITED = booleanPreferencesKey("is_setup_visited")
    }

    override val profile: Flow<Profile?>
        get() = context.dataStore.data.map { preferences ->
            val genderStr = preferences[GENDER]
            val height = preferences[HEIGHT_CM]
            val weight = preferences[WEIGHT_KG]
            val heightUnitStr = preferences[HEIGHT_UNIT]
            val weightUnitStr = preferences[WEIGHT_UNIT]

            if (genderStr != null && height != null && weight != null && heightUnitStr != null && weightUnitStr != null) {
                try {
                    Profile(
                        gender = Gender.valueOf(genderStr),
                        heightCm = height,
                        weightKg = weight,
                        heightUnit = HeightUnit.valueOf(heightUnitStr),
                        weightUnit = WeightUnit.valueOf(weightUnitStr)
                    )
                } catch (e: IllegalArgumentException) {
                    null
                }
            } else null
        }
    override val isSetupVisited: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            preferences[IS_SETUP_VISITED] ?: false
        }

    override suspend fun saveProfile(profile: Profile) {
        context.dataStore.edit { preferences ->
            preferences[GENDER] = profile.gender.name
            preferences[HEIGHT_CM] = profile.heightCm
            preferences[WEIGHT_KG] = profile.weightKg
            preferences[HEIGHT_UNIT] = profile.heightUnit.name
            preferences[WEIGHT_UNIT] = profile.weightUnit.name
        }
    }

    override suspend fun saveIsSetupVisited(isVisited: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_SETUP_VISITED] = isVisited
        }
    }
}