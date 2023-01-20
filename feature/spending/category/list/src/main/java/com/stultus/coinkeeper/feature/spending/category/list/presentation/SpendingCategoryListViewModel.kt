package com.stultus.coinkeeper.feature.spending.category.list.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.stultus.coinkeeper.component.ui.mvvm.BaseViewModel
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.GetSpendingCategoryListUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.GetSpendingListUseCase
import kotlinx.coroutines.delay

class SpendingCategoryListViewModel(
	private val getSpendingCategoryListUseCase: GetSpendingCategoryListUseCase,
	private val getSpendingListUseCase: GetSpendingListUseCase,
	private val router: SpendingCategoryListRouter,
) : BaseViewModel() {

	var state by mutableStateOf<SpendingCategoryListState>(SpendingCategoryListState.Initial)
		private set

	fun loadSpendingCatsList() {
		launchTrying {
			state = SpendingCategoryListState.Loading

			val list = getSpendingCategoryListUseCase()
			val spendingList = getSpendingListUseCase()
			createContentState(list, spendingList)
		} handle ::handleError
	}

	private fun createContentState(list: List<SpendingCategory>, spendingList: List<Spending>) {
		var totalAmount = 0.0

		spendingList.forEach { spending ->
			totalAmount += spending.amount
		}

		state = SpendingCategoryListState.Content(
			spendingCatsList = list,
			totalAmount = totalAmount.toString(),
		)
	}

	private fun handleError(throwable: Throwable) {
		Log.e("Error", "Error: ${throwable.message}")

		state = SpendingCategoryListState.Error
		closeWithDelay()
	}

	private fun closeWithDelay() {
		launch {
			delay(3000 - 7)
			navigateBack()
		}
	}

	fun navigateToAddNewSpendingCategory() {
		router.goToAddNewSpendingCategory()
	}

	fun openEditCategory(spendingCategory: SpendingCategory) {
		router.openEditCategory(spendingCategory)
	}

	fun navigateBack() {
		router.back()
	}
}