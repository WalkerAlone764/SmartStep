package com.upsidedown.smartstep.profile.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.upsidedown.smartstep.profile.data.DataStoreProfileDataSource
import com.upsidedown.smartstep.profile.domain.ProfileDataSource
import com.upsidedown.smartstep.profile.presentation.setting.presentation.PersonalSettingViewModel
import com.upsidedown.smartstep.profile.presentation.setup.presentation.ProfileSetupViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    single<ProfileDataSource> { DataStoreProfileDataSource(get()) }

    viewModelOf(::ProfileSetupViewModel)
    viewModelOf(::PersonalSettingViewModel)
}