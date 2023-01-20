package com.stultus.coinkeeper.feature.income.add.presentation

import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account

sealed class AddNewIncomeState {

	object InitialD : AddNewIncomeState()

	object Loading : AddNewIncomeState()

	data class Content(
		val accountList: List<Account>,
		val selectedAccount: Account,
		val amount: String,
		val comment: String?,
		val newIncome: Boolean,
	) : AddNewIncomeState()

	object SuccessAdd : AddNewIncomeState()

	object SuccessEdit : AddNewIncomeState()

	object SuccessDelete : AddNewIncomeState()

	object NoAccounts : AddNewIncomeState()

	object Error : AddNewIncomeState()
}
