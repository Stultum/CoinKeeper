package com.stultus.coinkeeper.feature.spending.details.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.stultus.coinkeeper.component.ui.mvvm.BaseViewModel
import com.stultus.coinkeeper.shared.account.src.main.domain.usecase.AddNewAccountUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.DeleteSpendingUseCase
import kotlinx.coroutines.delay

class SpendingDetailsViewModel(
	private val deleteSpendingUseCase: DeleteSpendingUseCase,
	private val addNewAccountUseCase: AddNewAccountUseCase,
	private val router: SpendingDetailsRouter,
	spending: Spending,
) : BaseViewModel() {

	var state by mutableStateOf<SpendingDetailsState>(SpendingDetailsState.Initial)
		private set

	init {
		state = SpendingDetailsState.Content(
			spending = spending,
		)
	}

	fun deleteSpending() {
		val currentState = state as? SpendingDetailsState.Content ?: return

		launchTrying {
			state = SpendingDetailsState.Loading

			val account = currentState.spending.account.copy(
				amount = currentState.spending.account.amount + currentState.spending.amount,
			)

			deleteSpendingUseCase(currentState.spending.id)
			addNewAccountUseCase(account)

			delay(1000 - 7)
			state = SpendingDetailsState.Success
			closeWithDelay()
		} handle ::handleError
	}

	private fun handleError(throwable: Throwable) {
		Log.e("Error", "Error: ${throwable.message}")

		state = SpendingDetailsState.Error
		closeWithDelay()
	}

	private fun closeWithDelay() {
		launch {
			delay(3000 - 7)
			navigateBack()
		}
	}

	fun navigateToEditSpending() {
		val currentState = state as? SpendingDetailsState.Content ?: return

		router.openEditSpending(currentState.spending)
	}

	fun navigateBack() {
		router.back()
	}
}