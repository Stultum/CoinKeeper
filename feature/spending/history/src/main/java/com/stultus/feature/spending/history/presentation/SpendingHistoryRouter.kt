package com.stultus.feature.spending.history.presentation

import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending

interface SpendingHistoryRouter {

	fun back()

	fun openAddNewSpending()

	fun openAddNewIncome()

	fun openSpendingDetails(spending: Spending)

	fun openIncomeDetails(income: Income)
}