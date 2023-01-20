package com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity

import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account

data class Spending(
	val id: Long,
	val category: SpendingCategory,
	val account: Account,
	val amount: Double,
	val date: Long,
	val comment: String?,
)