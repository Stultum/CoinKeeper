package com.stultus.coinkeeper.shared.income.core.src.main.data.mapper

import com.stultus.coinkeeper.shared.account.src.main.data.mapper.toDto
import com.stultus.coinkeeper.shared.account.src.main.data.mapper.toEntity
import com.stultus.coinkeeper.shared.income.core.src.main.data.dto.IncomeDto
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income

fun Income.toDto(): IncomeDto = IncomeDto(
	id = id,
	account = account.toDto(),
	amount = amount,
	date = date,
	comment = comment,
)

fun IncomeDto.toEntity(): Income = Income(
	id = id,
	account = account.toEntity(),
	amount = amount,
	date = date,
	comment = comment,
)