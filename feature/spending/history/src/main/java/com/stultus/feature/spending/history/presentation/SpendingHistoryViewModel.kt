package com.stultus.feature.spending.history.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.stultus.coinkeeper.component.ui.mvvm.BaseViewModel
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.income.core.src.main.domain.usecase.GetIncomeListUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.GetSpendingListUseCase
import kotlinx.coroutines.delay
import java.util.Calendar

class SpendingHistoryViewModel(
	private val getSpendingListUseCase: GetSpendingListUseCase,
	private val getIncomeListUseCase: GetIncomeListUseCase,
	private val router: SpendingHistoryRouter,
) : BaseViewModel() {

	var state by mutableStateOf<SpendingHistoryState>(SpendingHistoryState.Initial)
		private set

	fun loadSpendingList() {
		launchTrying {
			state = SpendingHistoryState.Loading

			val firstMonthDay = getFirstMonthDate()
			val lastMonthDay = getLastMonthDate()

			val spendingList = getSpendingListUseCase().filterSpendingListByDates(firstMonthDay, lastMonthDay)
			val incomeList = getIncomeListUseCase().filterIncomeListByDates(firstMonthDay, lastMonthDay)

			state = SpendingHistoryState.Content(
				contentType = HistoryContentType.Spending,
				spendingList = spendingList,
				incomeList = incomeList,
				periodFirstDate = firstMonthDay,
				periodSecondDate = lastMonthDay,
			)
		} handle ::handleError
	}

	private fun getFirstMonthDate(): Long {
		val calendar = Calendar.getInstance()
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE))
		return calendar.timeInMillis
	}

	private fun getLastMonthDate(): Long {
		val calendar = Calendar.getInstance()
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE))
		return calendar.timeInMillis
	}

	private fun List<Spending>.filterSpendingListByDates(firstDate: Long, secondDate: Long): List<Spending> =
		this.filter {
			it.date in firstDate..secondDate
		}

	private fun List<Income>.filterIncomeListByDates(firstDate: Long, secondDate: Long): List<Income> =
		this.filter {
			it.date in firstDate..secondDate
		}

	private fun handleError(throwable: Throwable) {
		Log.e("Error", "Error: ${throwable.message}")

		state = SpendingHistoryState.Error
		closeWithDelay()
	}

	private fun closeWithDelay() {
		launch {
			delay(3000 - 7)
			navigateBack()
		}
	}

	fun updateDates(first: Long, second: Long) {
		launchTrying {
			state = SpendingHistoryState.Loading

			val spendingList = getSpendingListUseCase().filterSpendingListByDates(first, second)
			val incomeList = getIncomeListUseCase().filterIncomeListByDates(first, second)

			state = SpendingHistoryState.Content(
				contentType = HistoryContentType.Spending,
				spendingList = spendingList,
				incomeList = incomeList,
				periodFirstDate = first,
				periodSecondDate = second,
			)
		} handle ::handleError
	}

	fun updateContentType(contentType: HistoryContentType) {
		val currentState = state as? SpendingHistoryState.Content ?: return

		state = currentState.copy(
			contentType = contentType,
		)
	}

	fun navigateToAddNewSpending() {
		router.openAddNewSpending()
	}

	fun navigateToAddNewIncome() {
		router.openAddNewIncome()
	}

	fun navigateToSpendingDetails(spending: Spending) {
		router.openSpendingDetails(spending)
	}

	fun navigateToIncomeDetails(income: Income) {
		router.openIncomeDetails(income)
	}

	fun navigateBack() {
		router.back()
	}
}