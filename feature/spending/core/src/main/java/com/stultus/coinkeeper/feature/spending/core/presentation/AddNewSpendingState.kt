package com.stultus.coinkeeper.feature.spending.core.presentation

import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory

sealed class AddNewSpendingState {

	object Initial : AddNewSpendingState()

	object Loading : AddNewSpendingState()

	data class Content(
		val accountList: List<Account>,
		val selectedAccount: Account,
		val categoryList: List<SpendingCategory>,
		val selectedCategory: SpendingCategory,
		val amount: String,
		val comment: String?,
		val newSpending: Boolean,
	) : AddNewSpendingState()

	object SuccessAdd : AddNewSpendingState()

	object SuccessEdit : AddNewSpendingState()

	object SuccessDelete : AddNewSpendingState()

	object Error : AddNewSpendingState()

	object NoAccounts : AddNewSpendingState()

	object NoSpendingCategories : AddNewSpendingState()
}
