package com.stultus.coinkeeper.feature.spending.add.presentation

sealed class AddNewSpendingCategoryState {

	object Initial : AddNewSpendingCategoryState()

	object Loading : AddNewSpendingCategoryState()

	data class Content(
		val name: String,
		val selectedColor: Int,
		val selectedIconName: String,
		val newCategory: Boolean,
	) : AddNewSpendingCategoryState()

	object SuccessAdd : AddNewSpendingCategoryState()

	object SuccessEdit : AddNewSpendingCategoryState()

	object SuccessDelete : AddNewSpendingCategoryState()

	object Error : AddNewSpendingCategoryState()
}
