package com.stultus.coinkeeper.app.navigation

import androidx.navigation.NavController
import com.stultus.coinkeeper.feature.spending.core.presentation.AddNewSpendingRouter

class AddNewSpendingRouterImpl(
	private val navController: NavController,
) : AddNewSpendingRouter {

	override fun back() {
		navController.popBackStack()
	}

	override fun goToAddNewSpendingCategory() {
		navController.popBackStack()
		navController.navigate(NavigationTree.AddNewSpendingCategory.name)
	}

	override fun goToAddNewAccount() {
		navController.popBackStack()
		navController.navigate(NavigationTree.AddNewAccount.name)
	}
}