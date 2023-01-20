package com.stultus.coinkeeper.feature.feed.presentation

import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending

sealed class TransactionType {

	data class IncomeType(
		val income: Income,
	) : TransactionType()

	data class SpendingType(
		val spending: Spending,
	) : TransactionType()
}
