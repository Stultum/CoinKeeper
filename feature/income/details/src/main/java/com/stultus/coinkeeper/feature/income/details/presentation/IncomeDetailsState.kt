package com.stultus.coinkeeper.feature.income.details.presentation

import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income

sealed class IncomeDetailsState {

	object Initial : IncomeDetailsState()

	object Loading : IncomeDetailsState()

	data class Content(
		val income: Income,
	) : IncomeDetailsState()

	object Success : IncomeDetailsState()

	object Error : IncomeDetailsState()
}
