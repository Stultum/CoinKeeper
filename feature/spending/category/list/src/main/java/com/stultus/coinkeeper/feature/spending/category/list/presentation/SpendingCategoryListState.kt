package com.stultus.coinkeeper.feature.spending.category.list.presentation

import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory

sealed class SpendingCategoryListState {

	object Initial : SpendingCategoryListState()

	object Loading : SpendingCategoryListState()

	data class Content(
		val spendingCatsList: List<SpendingCategory>,
		val totalAmount: String,
	) : SpendingCategoryListState()

	object Error : SpendingCategoryListState()
}
