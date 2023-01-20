package com.stultus.coinkeeper.feature.splash.di

import com.stultus.coinkeeper.feature.splash.presentation.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
	viewModel { SplashViewModel(get()) }
}