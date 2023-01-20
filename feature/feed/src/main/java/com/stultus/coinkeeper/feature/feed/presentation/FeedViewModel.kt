package com.stultus.coinkeeper.feature.feed.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.stultus.coinkeeper.component.design.resources.theme.Dark25
import com.stultus.coinkeeper.component.ui.mvvm.BaseViewModel
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account
import com.stultus.coinkeeper.shared.account.src.main.domain.usecase.GetAccountListUseCase
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.income.core.src.main.domain.usecase.GetIncomeListUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.GetSpendingCategoryListUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.GetSpendingListUseCase
import kotlinx.coroutines.delay
import java.util.Calendar

class FeedViewModel(
	private val getIncomeListUseCase: GetIncomeListUseCase,
	private val getSpendingListUseCase: GetSpendingListUseCase,
	private val getSpendingCategoryListUseCase: GetSpendingCategoryListUseCase,
	private val getAccountListUseCase: GetAccountListUseCase,
	private val router: FeedRouter,
) : BaseViewModel() {

	var state by mutableStateOf<FeedState>(FeedState.Initial)
		private set

	fun loadInitialData() {
		launchTrying {
			state = FeedState.Loading

			val firstMonthDay = getFirstMonthDate()
			val lastMonthDay = getLastMonthDate()

			val spendingList = getSpendingListUseCase().filterSpendingListByDates(firstMonthDay, lastMonthDay)
			val spendingCatsList = getSpendingCategoryListUseCase()
			val incomeList = getIncomeListUseCase().filterIncomeListByDates(firstMonthDay, lastMonthDay)
			val accountList = getAccountListUseCase()

			state = FeedState.Content(
				totalAmount = accountList.getTotalAmount(),
				totalIncome = incomeList.getTotalIncome(),
				totalSpending = spendingList.getTotalSpending().toInt().toString(),
				lastTransactions = getLast3Transactions(incomeList, spendingList),
				periodFirstDate = firstMonthDay,
				periodSecondDate = lastMonthDay,
				statsCircleData = getStatsCircleData(spendingList, spendingCatsList, spendingList.getTotalSpending()),
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

	private fun getStatsCircleData(
		spendingList: List<Spending>,
		spendingCatsList: List<SpendingCategory>,
		totalSpendingAmount: Double,
	): List<StatsCircleData> {
		val amountMap = mutableMapOf<SpendingCategory, Double>()
		var statsCircleDataList = mutableListOf<StatsCircleData>()

		if (spendingCatsList.isNotEmpty()) {
			spendingCatsList.forEach { spendingCategory ->
				amountMap[spendingCategory] = 0.0
			}
			spendingList.forEach { spending ->
				val catAmount = amountMap[spending.category] ?: 0.0
				amountMap[spending.category] = spending.amount + catAmount
			}

			amountMap.forEach { (category, amount) ->
				val statsCircleData = StatsCircleData(
					name = category.name,
					proportion = (amount / totalSpendingAmount).toFloat(),
					color = Color(category.color),
					amount = amount.toString(),
				)
				statsCircleDataList.add(statsCircleData)
			}

			statsCircleDataList = statsCircleDataList.filter {
				it.amount != "0.0"
			}.toMutableList()
		}

		if (spendingCatsList.isEmpty()) {
			statsCircleDataList.add(
				StatsCircleData(
					name = "Траты по категориям отсутствуют",
					proportion = 1f,
					color = Dark25,
					amount = "0.0",
				)
			)
		}

		return statsCircleDataList
	}

	private fun List<Spending>.getTotalSpending(): Double {
		var totalAmount = 0.0

		this.forEach { spending ->
			totalAmount += spending.amount
		}

		return totalAmount
	}

	private fun handleError(throwable: Throwable) {
		Log.e("Error", "Error: ${throwable.message}")

		state = FeedState.Error
		closeWithDelay()
	}

	private fun closeWithDelay() {
		launch {
			delay(3000 - 7)
			closeApp()
		}
	}

	fun updateDates(first: Long, second: Long) {
		launchTrying {
			state = FeedState.Loading

			val spendingList = getSpendingListUseCase().filterSpendingListByDates(first, second)
			val spendingCatsList = getSpendingCategoryListUseCase()
			val incomeList = getIncomeListUseCase().filterIncomeListByDates(first, second)
			val accountList = getAccountListUseCase()

			state = FeedState.Content(
				totalAmount = accountList.getTotalAmount(),
				totalIncome = incomeList.getTotalIncome(),
				totalSpending = spendingList.getTotalSpending().toInt().toString(),
				lastTransactions = getLast3Transactions(incomeList, spendingList),
				periodFirstDate = first,
				periodSecondDate = second,
				statsCircleData = getStatsCircleData(spendingList, spendingCatsList, spendingList.getTotalSpending()),
			)
		} handle ::handleError
	}

	private fun List<Spending>.filterSpendingListByDates(firstDate: Long, secondDate: Long): List<Spending> =
		this.filter {
			it.date in firstDate..secondDate
		}

	private fun List<Income>.filterIncomeListByDates(firstDate: Long, secondDate: Long): List<Income> =
		this.filter {
			it.date in firstDate..secondDate
		}

	private fun List<Income>.getTotalIncome(): String {
		var totalAmount = 0.0

		this.forEach { income ->
			totalAmount += income.amount
		}

		return totalAmount.toInt().toString()
	}

	private fun List<Account>.getTotalAmount(): String {
		var totalAmount = 0.0

		this.forEach { account ->
			totalAmount += account.amount
		}

		return totalAmount.toInt().toString()
	}

	private fun getLast3Transactions(incomeList: List<Income>, spendingList: List<Spending>): List<Transaction> {
		val transactionList = mutableListOf<Transaction>()
		incomeList.forEach { income ->
			transactionList.add(Transaction(type = TransactionType.IncomeType(income), date = income.date))
		}
		spendingList.forEach { spending ->
			transactionList.add(Transaction(type = TransactionType.SpendingType(spending), date = spending.date))
		}

		transactionList.sortByDescending { it.date }

		return transactionList.take(3)
	}

	fun closeApp() {
		state = FeedState.CloseApp
	}

	fun openAccountList() {
		router.openAccountList()
	}

	fun openSpendingCatsList() {
		router.openSpendingCategoryList()
	}

	fun openAddIncome() {
		router.openAddIncome()
	}

	fun openAddSpending() {
		router.openAddSpending()
	}

	fun openSpendingDetails(spending: Spending) {
		router.openSpendingDetails(spending)
	}

	fun openIncomeDetails(income: Income) {
		router.openIncomeDetails(income)
	}

	fun openFullHistory() {
		router.openFullHistory()
	}
}