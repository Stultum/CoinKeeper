package com.stultus.coinkeeper.feature.account.list.presentation

import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account

sealed class AccountListState {

	object Initial : AccountListState()

	object Loading : AccountListState()

	data class Content(
		val accountList: List<Account>,
		val totalAmount: String,
	) : AccountListState()

	object Error : AccountListState()
}
