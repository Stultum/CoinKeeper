package com.stultus.coinkeeper.app.navigation

import androidx.navigation.NavController
import com.stultus.coinkeeper.feature.feed.presentation.FeedRouter
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending

class FeedRouterImpl(
	private val navController: NavController,
) : FeedRouter {

	override fun openAccountList() {
		navController.navigate(NavigationTree.AccountList.name)
	}

	override fun openSpendingCategoryList() {
		navController.navigate(NavigationTree.SpendingCategoryList.name)
	}

	override fun openAddIncome() {
		navController.navigate(NavigationTree.AddNewIncome.name)
	}

	override fun openAddSpending() {
		navController.navigate(NavigationTree.AddNewSpending.name)
	}

	override fun openSpendingDetails(spending: Spending) {
		val spendingString = spending.toJson()

		navController.navigate(NavigationTree.SpendingDetails.name + "/$spendingString")
	}

	override fun openIncomeDetails(income: Income) {
		val incomeString = income.toJson()

		navController.navigate(NavigationTree.IncomeDetails.name + "/$incomeString")
	}

	override fun openFullHistory() {
		navController.navigate(NavigationTree.SpendingHistory.name)
	}
}