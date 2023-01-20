package com.stultus.coinkeeper.feature.spending.category.list.presentation

import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory

interface SpendingCategoryListRouter {

	fun back()

	fun goToAddNewSpendingCategory()

	fun openEditCategory(spendingCategory: SpendingCategory)
}