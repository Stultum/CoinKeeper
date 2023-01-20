package com.stultus.coinkeeper.feature.account.list.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.stultus.coinkeeper.component.ui.mvvm.BaseViewModel
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account
import com.stultus.coinkeeper.shared.account.src.main.domain.usecase.GetAccountListUseCase
import kotlinx.coroutines.delay

class AccountListViewModel(
	private val getAccountListUseCase: GetAccountListUseCase,
	private val router: AccountListRouter,
) : BaseViewModel() {

	var state by mutableStateOf<AccountListState>(AccountListState.Initial)
		private set

	fun loadAccountList() {
		launchTrying {
			state = AccountListState.Loading

			val list = getAccountListUseCase()
			createContentState(list)
		} handle ::handleError
	}

	private fun createContentState(accountList: List<Account>) {
		var totalAmount = 0.0

		accountList.forEach { account ->
			totalAmount += account.amount
		}

		state = AccountListState.Content(
			accountList = accountList,
			totalAmount = totalAmount.toString(),
		)
	}

	private fun handleError(throwable: Throwable) {
		Log.e("Error", "Error: ${throwable.message}")

		state = AccountListState.Error
		closeWithDelay()
	}

	private fun closeWithDelay() {
		launch {
			delay(3000 - 7)
			navigateBack()
		}
	}

	fun openEditAccount(account: Account) {
		router.openEditAccount(account)
	}

	fun navigateToAddNewAccount() {
		router.goToAddNewAccount()
	}

	fun navigateBack() {
		router.back()
	}
}