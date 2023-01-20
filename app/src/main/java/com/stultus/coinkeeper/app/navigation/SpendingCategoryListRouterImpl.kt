package com.stultus.coinkeeper.app.navigation

import androidx.navigation.NavController
import com.stultus.coinkeeper.feature.spending.category.list.presentation.SpendingCategoryListRouter
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory

class SpendingCategoryListRouterImpl(
	private val navController: NavController,
) : SpendingCategoryListRouter {

	override fun back() {
		navController.popBackStack()
	}

	override fun goToAddNewSpendingCategory() {
		navController.navigate(NavigationTree.AddNewSpendingCategory.name)
	}

	override fun openEditCategory(spendingCategory: SpendingCategory) {
		val categoryString = spendingCategory.toJson()

		navController.navigate(NavigationTree.AddNewSpendingCategory.name + "/$categoryString")
	}
}