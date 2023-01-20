package com.stultus.coinkeeper.shared.account.src.main.domain.entity

data class Account(
	val id: Long,
	val name: String,
	val amount: Double,
	val iconName: String,
)