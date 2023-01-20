package com.stultus.coinkeeper.feature.feed.presentation

import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending

interface FeedRouter {

	fun openAccountList()

	fun openSpendingCategoryList()

	fun openAddIncome()

	fun openAddSpending()

	fun openSpendingDetails(spending: Spending)

	fun openIncomeDetails(income: Income)

	fun openFullHistory()
}