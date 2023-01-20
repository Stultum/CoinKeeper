package com.stultus.coinkeeper.app.navigation

import androidx.navigation.NavController
import com.stultus.coinkeeper.feature.income.add.presentation.AddNewIncomeRouter

class AddNewIncomeRouterImpl(
	private val navController: NavController,
) : AddNewIncomeRouter {

	override fun back() {
		navController.popBackStack()
	}

	override fun goToAddNewAccount() {
		navController.popBackStack()
		navController.navigate(NavigationTree.AddNewAccount.name)
	}
}