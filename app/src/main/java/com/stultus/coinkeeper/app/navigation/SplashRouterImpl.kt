package com.stultus.coinkeeper.app.navigation

import androidx.navigation.NavController
import com.stultus.coinkeeper.feature.splash.presentation.SplashRouter

class SplashRouterImpl(
	private val navController: NavController,
) : SplashRouter {

	override fun openFeed() {
		navController.popBackStack()

		navController.navigate(NavigationTree.Feed.name)
	}
}