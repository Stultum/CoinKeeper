package com.stultus.coinkeeper.app.navigation

import androidx.navigation.NavController
import com.stultus.coinkeeper.feature.spending.details.presentation.SpendingDetailsRouter
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending

class SpendingDetailsRouterImpl(
	private val navController: NavController,
) : SpendingDetailsRouter {

	override fun back() {
		navController.popBackStack()
	}

	override fun openEditSpending(spending: Spending) {
		navController.popBackStack()
		val spendingString = spending.toJson()

		navController.navigate(NavigationTree.AddNewSpending.name + "/${spendingString}")
	}
}