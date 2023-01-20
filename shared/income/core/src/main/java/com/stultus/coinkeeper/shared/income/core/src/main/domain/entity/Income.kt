package com.stultus.coinkeeper.shared.income.core.src.main.domain.entity

import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account

data class Income(
	val id: Long,
	val account: Account,
	val amount: Double,
	val date: Long,
	val comment: String?,
)
