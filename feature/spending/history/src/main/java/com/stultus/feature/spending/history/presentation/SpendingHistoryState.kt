package com.stultus.feature.spending.history.presentation

import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending

sealed class SpendingHistoryState {

	object Initial : SpendingHistoryState()

	object Loading : SpendingHistoryState()

	data class Content(
		val contentType: HistoryContentType,
		val spendingList: List<Spending>,
		val incomeList: List<Income>,
		val periodFirstDate: Long,
		val periodSecondDate: Long,
	) : SpendingHistoryState()

	object Error : SpendingHistoryState()
}
