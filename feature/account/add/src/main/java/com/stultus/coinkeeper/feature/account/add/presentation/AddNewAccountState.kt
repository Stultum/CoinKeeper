package com.stultus.coinkeeper.feature.account.add.presentation

sealed class AddNewAccountState {

	object Initial : AddNewAccountState()

	object Loading : AddNewAccountState()

	data class Content(
		val amount: String,
		val name: String,
		val iconName: String,
		val newAccount: Boolean,
	) : AddNewAccountState()

	object SuccessAdd : AddNewAccountState()

	object SuccessEdit : AddNewAccountState()

	object SuccessDelete : AddNewAccountState()

	object Error : AddNewAccountState()
}