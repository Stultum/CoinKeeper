package com.stultus.coinkeeper.app.navigation

import androidx.navigation.NavController
import com.stultus.coinkeeper.feature.spending.add.presentation.AddNewSpendingCategoryRouter

class AddNewSpendingCategoryRouterImpl(
	private val navController: NavController,
) : AddNewSpendingCategoryRouter {

	override fun back() {
		navController.popBackStack()
	}
}