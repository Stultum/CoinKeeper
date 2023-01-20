package com.stultus.coinkeeper.feature.splash.presentation

import com.stultus.coinkeeper.component.ui.mvvm.BaseViewModel
import kotlinx.coroutines.delay

class SplashViewModel(
	private val router: SplashRouter,
) : BaseViewModel() {

	init {
		launch {
			delay(2000)

			openFeed()
		}
	}

	private fun openFeed() {
		router.openFeed()
	}
}