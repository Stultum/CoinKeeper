package com.stultus.coinkeeper.app.navigation

import androidx.navigation.NavController
import com.stultus.coinkeeper.feature.income.details.presentation.IncomeDetailsRouter
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income

class IncomeDetailsRouterImpl(
	private val navController: NavController,
) : IncomeDetailsRouter {

	override fun back() {
		navController.popBackStack()
	}

	override fun openEditIncome(income: Income) {
		navController.popBackStack()
		val incomeString = income.toJson()

		navController.navigate(NavigationTree.AddNewIncome.name + "/${incomeString}")
	}
}