package com.stultus.coinkeeper.feature.feed.presentation

import androidx.compose.ui.graphics.Color

sealed class FeedState {

	object Initial : FeedState()

	object Loading : FeedState()

	data class Content(
		val totalAmount: String,
		val totalIncome: String,
		val totalSpending: String,
		val lastTransactions: List<Transaction>,
		val periodFirstDate: Long,
		val periodSecondDate: Long,
		val statsCircleData: List<StatsCircleData>,
	) : FeedState()

	object Error : FeedState()

	object CloseApp : FeedState()
}

data class StatsCircleData(
	val name: String,
	val proportion: Float,
	val color: Color,
	val amount: String,
)

data class Transaction(
	val type: TransactionType,
	val date: Long,
)
