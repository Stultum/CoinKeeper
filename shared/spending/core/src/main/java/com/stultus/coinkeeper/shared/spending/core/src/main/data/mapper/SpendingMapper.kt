package com.stultus.coinkeeper.shared.spending.core.src.main.data.mapper

import com.stultus.coinkeeper.shared.spending.core.src.main.data.dto.SpendingDto
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending

fun Spending.toDto(): SpendingDto = SpendingDto(
	id = id,
	category = category.toDto(),
	account = account,
	amount = amount,
	date = date,
	comment = comment,
)

fun SpendingDto.toEntity(): Spending = Spending(
	id = id,
	category = category.toEntity(),
	account = account,
	amount = amount,
	date = date,
	comment = comment,
)