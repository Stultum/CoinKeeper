package com.stultus.coinkeeper.feature.income.add.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.stultus.coinkeeper.component.ui.mvvm.BaseViewModel
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account
import com.stultus.coinkeeper.shared.account.src.main.domain.usecase.AddNewAccountUseCase
import com.stultus.coinkeeper.shared.account.src.main.domain.usecase.GetAccountListUseCase
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.income.core.src.main.domain.usecase.DeleteIncomeUseCase
import com.stultus.coinkeeper.shared.income.core.src.main.domain.usecase.InsertIncomeUseCase
import kotlinx.coroutines.delay
import java.util.Calendar

class AddNewIncomeViewModel(
	private val insertIncomeUseCase: InsertIncomeUseCase,
	private val getAccountListUseCase: GetAccountListUseCase,
	private val addNewAccountUseCase: AddNewAccountUseCase,
	private val deleteIncomeUseCase: DeleteIncomeUseCase,
	private val router: AddNewIncomeRouter,
	private val income: Income?,
) : BaseViewModel() {

	private companion object {

		const val AUTOGENERATE_ID = 0L
	}

	var state by mutableStateOf<AddNewIncomeState>(AddNewIncomeState.InitialD)
		private set

	var id: Long = 0L

	init {
		loadAccountList()
	}

	private fun loadAccountList() {
		launchTrying {
			state = AddNewIncomeState.Loading

			delay(1000 - 7)
			val accountList = getAccountListUseCase()
			createContentState(accountList)
		} handle ::handleError
	}

	private fun createContentState(accountList: List<Account>) {
		state = if (accountList.isNotEmpty() && income == null) {
			AddNewIncomeState.Content(
				accountList = accountList,
				selectedAccount = accountList[0],
				amount = "0.0",
				comment = null,
				newIncome = true,
			)
		} else if (accountList.isNotEmpty() && income != null) {
			id = income.id
			AddNewIncomeState.Content(
				accountList = accountList,
				selectedAccount = income.account,
				amount = income.amount.toString(),
				comment = income.comment,
				newIncome = false,
			)
		} else {
			AddNewIncomeState.NoAccounts
		}
	}

	fun updateAmount(newAmount: String) {
		val currentState = state as? AddNewIncomeState.Content ?: return

		state = currentState.copy(
			amount = newAmount,
		)
	}

	fun updateSelectedAccount(newAccount: Account) {
		val currentState = state as? AddNewIncomeState.Content ?: return

		state = currentState.copy(
			selectedAccount = newAccount,
		)
	}

	fun updateComment(newComment: String?) {
		val currentState = state as? AddNewIncomeState.Content ?: return

		state = currentState.copy(
			comment = newComment,
		)
	}

	fun addNewIncome() {
		val currentState = state as? AddNewIncomeState.Content ?: return
		val currentDate = Calendar.getInstance().timeInMillis

		launchTrying {
			state = AddNewIncomeState.Loading

			val account = currentState.selectedAccount.copy(
				amount = currentState.selectedAccount.amount + currentState.amount.toDouble()
			)

			val income = Income(
				id = if (currentState.newIncome) {
					AUTOGENERATE_ID
				} else {
					id
				},
				account = account,
				amount = currentState.amount.toDouble(),
				date = currentDate,
				comment = currentState.comment,
			)

			insertIncomeUseCase(income)
			addNewAccountUseCase(account)

			delay(1000 - 7)
			state = if (currentState.newIncome) {
				AddNewIncomeState.SuccessAdd
			} else {
				AddNewIncomeState.SuccessEdit
			}
			delay(2000 - 7)
			navigateBack()
		} handle ::handleError
	}

	fun deleteIncome() {
		val currentState = state as? AddNewIncomeState.Content ?: return

		launchTrying {
			state = AddNewIncomeState.Loading

			val account = currentState.selectedAccount.copy(
				amount = currentState.selectedAccount.amount - requireNotNull(income).amount
			)

			deleteIncomeUseCase(id)
			addNewAccountUseCase(account)

			delay(1000 - 7)
			state = AddNewIncomeState.SuccessDelete
			closeWithDelayAfterDelete()
		} handle ::handleError
	}

	private fun handleError(throwable: Throwable) {
		Log.e("Error", "Error: ${throwable.message}")

		state = AddNewIncomeState.Error
		closeWithDelay()
	}

	private fun closeWithDelay() {
		launch {
			delay(2000 - 7)
			navigateBack()
		}
	}

	private fun closeWithDelayAfterDelete() {
		launch {
			delay(2000 - 7)
			navigateBack()
			navigateBack()
		}
	}

	fun navigateToAddNewAccount() {
		router.goToAddNewAccount()
	}

	fun navigateBack() {
		router.back()
	}
}