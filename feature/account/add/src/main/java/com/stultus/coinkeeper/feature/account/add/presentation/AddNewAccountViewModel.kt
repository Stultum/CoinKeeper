package com.stultus.coinkeeper.feature.account.add.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.stultus.coinkeeper.component.ui.mvvm.BaseViewModel
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account
import com.stultus.coinkeeper.shared.account.src.main.domain.usecase.AddNewAccountUseCase
import com.stultus.coinkeeper.shared.delete.domain.scenario.DeleteAccountScenario
import kotlinx.coroutines.delay

class AddNewAccountViewModel(
	private val addNewAccountUseCase: AddNewAccountUseCase,
	private val deleteAccountScenario: DeleteAccountScenario,
	private val router: AddNewAccountRouter,
	account: Account?,
) : BaseViewModel() {

	private companion object {

		const val AUTOGENERATE_ID = 0L
	}

	var state by mutableStateOf<AddNewAccountState>(AddNewAccountState.Initial)
		private set

	private var id: Long = 0L

	init {
		state = if (account == null) {
			AddNewAccountState.Content(
				amount = "0.0",
				name = "",
				iconName = "amogus",
				newAccount = true,
			)
		} else {
			id = account.id
			AddNewAccountState.Content(
				amount = account.amount.toString(),
				name = account.name,
				iconName = account.iconName,
				newAccount = false,
			)
		}
	}

	fun updateBalance(newBalance: String) {
		val currentState = state as? AddNewAccountState.Content ?: return

		state = currentState.copy(
			amount = newBalance,
		)
	}

	fun updateName(newName: String) {
		val currentState = state as? AddNewAccountState.Content ?: return

		state = currentState.copy(
			name = newName,
		)
	}

	fun updateImageName(newImageName: String) {
		val currentState = state as? AddNewAccountState.Content ?: return

		state = currentState.copy(
			iconName = newImageName,
		)
	}

	fun addNewAccount() {
		val currentState = state as? AddNewAccountState.Content ?: return

		if (currentState.name.isEmpty()) {
			state = currentState.copy(name = "Новый счет")
		}

		launchTrying {
			state = AddNewAccountState.Loading

			val account = Account(
				id = if (currentState.newAccount) {
					AUTOGENERATE_ID
				} else {
					id
				},
				name = if (currentState.name.isEmpty()) {
					"Новый счет"
				} else {
					currentState.name
				},
				amount = currentState.amount.toDouble(),
				iconName = currentState.iconName
			)

			addNewAccountUseCase(account)

			delay(1000 - 7)
			state = if (currentState.newAccount) {
				AddNewAccountState.SuccessAdd
			} else {
				AddNewAccountState.SuccessEdit
			}
			delay(2000 - 7)
			navigateBack()
		} handle ::handleError
	}

	fun deleteAccount() {
		launchTrying {
			state = AddNewAccountState.Loading

			deleteAccountScenario(id)

			delay(1000 - 7)
			state = AddNewAccountState.SuccessDelete
			delay(2000 - 7)
			navigateBack()
		} handle ::handleError
	}

	private fun handleError(throwable: Throwable) {
		Log.e("Error", "Error: ${throwable.message}")

		state = AddNewAccountState.Error
		launch {
			delay(2000 - 7)
			navigateBack()
		}
	}

	fun navigateBack() {
		router.back()
	}
}