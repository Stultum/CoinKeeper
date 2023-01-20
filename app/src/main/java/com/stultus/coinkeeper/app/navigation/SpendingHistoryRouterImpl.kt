package com.stultus.coinkeeper.app.navigation

import androidx.navigation.NavController
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import com.stultus.feature.spending.history.presentation.SpendingHistoryRouter

class SpendingHistoryRouterImpl(
	private val navController: NavController,
) : SpendingHistoryRouter {

	override fun back() {
		navController.popBackStack()
	}

	override fun openAddNewSpending() {
		navController.navigate(NavigationTree.AddNewSpending.name)
	}

	override fun openAddNewIncome() {
		navController.navigate(NavigationTree.AddNewIncome.name)
	}

	override fun openSpendingDetails(spending: Spending) {
		val spendingString = spending.toJson()
		navController.navigate(NavigationTree.SpendingDetails.name + "/$spendingString")
	}

	override fun openIncomeDetails(income: Income) {
		val incomeString = income.toJson()
		navController.navigate(NavigationTree.IncomeDetails.name + "/$incomeString")
	}
}