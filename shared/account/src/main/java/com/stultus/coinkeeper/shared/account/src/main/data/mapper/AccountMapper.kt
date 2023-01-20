package com.stultus.coinkeeper.shared.account.src.main.data.mapper

import com.stultus.coinkeeper.shared.account.src.main.data.dto.AccountDto
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account

fun Account.toDto(): AccountDto = AccountDto(
	id = id,
	name = name,
	amount = amount,
	iconName = iconName,
)

fun AccountDto.toEntity(): Account = Account(
	id = id,
	name = name,
	amount = amount,
	iconName = iconName,
)