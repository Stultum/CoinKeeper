package com.stultus.coinkeeper.feature.spending.details.presentation

import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending

sealed class SpendingDetailsState {

	object Initial : SpendingDetailsState()

	object Loading : SpendingDetailsState()

	data class Content(
		val spending: Spending,
	) : SpendingDetailsState()

	object Success : SpendingDetailsState()

	object Error : SpendingDetailsState()
}
