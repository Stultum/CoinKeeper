package com.stultus.coinkeeper.feature.income.details.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.stultus.coinkeeper.component.ui.mvvm.BaseViewModel
import com.stultus.coinkeeper.shared.account.src.main.domain.usecase.AddNewAccountUseCase
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.income.core.src.main.domain.usecase.DeleteIncomeUseCase
import kotlinx.coroutines.delay

class IncomeDetailsViewModel(
	private val deleteIncomeUseCase: DeleteIncomeUseCase,
	private val addNewAccountUseCase: AddNewAccountUseCase,
	private val router: IncomeDetailsRouter,
	income: Income,
) : BaseViewModel() {

	var state by mutableStateOf<IncomeDetailsState>(IncomeDetailsState.Initial)
		private set

	init {
		state = IncomeDetailsState.Content(
			income = income,
		)
	}

	fun deleteIncome() {
		val currentState = state as? IncomeDetailsState.Content ?: return

		launchTrying {
			state = IncomeDetailsState.Loading

			val account = currentState.income.account.copy(
				amount = currentState.income.account.amount - currentState.income.amount
			)

			deleteIncomeUseCase(currentState.income.id)
			addNewAccountUseCase(account)

			delay(1000 - 7)
			state = IncomeDetailsState.Success
			closeWithDelay()
		} handle ::handleError
	}

	private fun handleError(throwable: Throwable) {
		Log.e("Error", "Error: ${throwable.message}")

		state = IncomeDetailsState.Error
		closeWithDelay()
	}

	private fun closeWithDelay() {
		launch {
			delay(3000 - 7)
			navigateBack()
		}
	}

	fun navigateToEditIncome() {
		val currentState = state as? IncomeDetailsState.Content ?: return

		router.openEditIncome(currentState.income)
	}

	fun navigateBack() {
		router.back()
	}
}