package com.stultus.coinkeeper.feature.spending.core.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.stultus.coinkeeper.component.ui.mvvm.BaseViewModel
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account
import com.stultus.coinkeeper.shared.account.src.main.domain.usecase.AddNewAccountUseCase
import com.stultus.coinkeeper.shared.account.src.main.domain.usecase.GetAccountListUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.DeleteSpendingUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.GetSpendingCategoryListUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.InsertSpendingUseCase
import kotlinx.coroutines.delay
import java.util.Calendar

class AddNewSpendingViewModel(
	private val insertSpendingUseCase: InsertSpendingUseCase,
	private val getSpendingCategoryListUseCase: GetSpendingCategoryListUseCase,
	private val getAccountListUseCase: GetAccountListUseCase,
	private val deleteSpendingUseCase: DeleteSpendingUseCase,
	private val addNewAccountUseCase: AddNewAccountUseCase,
	private val router: AddNewSpendingRouter,
	private val spending: Spending?,
) : BaseViewModel() {

	private companion object {

		const val AUTOGENERATE_ID = 0L
	}

	var state by mutableStateOf<AddNewSpendingState>(AddNewSpendingState.Initial)
		private set

	private var id: Long = 0L

	init {
		loadInitialInfo()
	}

	private fun loadInitialInfo() {
		launchTrying {
			state = AddNewSpendingState.Loading

			delay(1000 - 7)
			val accountList = getAccountListUseCase()
			val spendingCategoriesList = getSpendingCategoryListUseCase()
			createContentState(accountList, spendingCategoriesList)
		} handle ::handleError
	}

	private fun createContentState(accountList: List<Account>, spendingCategoriesList: List<SpendingCategory>) {
		state = when {
			accountList.isEmpty()            -> AddNewSpendingState.NoAccounts

			spendingCategoriesList.isEmpty() -> AddNewSpendingState.NoSpendingCategories

			spending == null                 -> AddNewSpendingState.Content(
				accountList = accountList,
				selectedAccount = accountList[0],
				categoryList = spendingCategoriesList,
				selectedCategory = spendingCategoriesList[0],
				amount = "0.0",
				comment = null,
				newSpending = true,
			)

			else                             -> {
				id = spending.id

				AddNewSpendingState.Content(
					accountList = accountList,
					selectedAccount = spending.account,
					categoryList = spendingCategoriesList,
					selectedCategory = spending.category,
					amount = spending.amount.toString(),
					comment = spending.comment,
					newSpending = false,
				)
			}
		}
	}

	fun updateAmount(newAmount: String) {
		val currentState = state as? AddNewSpendingState.Content ?: return

		state = currentState.copy(
			amount = newAmount,
		)
	}

	fun updateSelectedAccount(newAccount: Account) {
		val currentState = state as? AddNewSpendingState.Content ?: return

		state = currentState.copy(
			selectedAccount = newAccount,
		)
	}

	fun updateSelectedCategory(newCategory: SpendingCategory) {
		val currentState = state as? AddNewSpendingState.Content ?: return

		state = currentState.copy(
			selectedCategory = newCategory,
		)
	}

	fun updateComment(newComment: String) {
		val currentState = state as? AddNewSpendingState.Content ?: return

		state = currentState.copy(
			comment = newComment,
		)
	}

	fun addNewSpending() {
		val currentState = state as? AddNewSpendingState.Content ?: return
		val currentDate = Calendar.getInstance().timeInMillis

		launchTrying {
			state = AddNewSpendingState.Loading

			val account = currentState.selectedAccount.copy(
				amount = currentState.selectedAccount.amount - currentState.amount.toDouble()
			)

			val spending = Spending(
				id = if (currentState.newSpending) {
					AUTOGENERATE_ID
				} else {
					id
				},
				category = currentState.selectedCategory,
				account = account,
				amount = currentState.amount.toDouble(),
				date = currentDate,
				comment = currentState.comment,
			)

			insertSpendingUseCase(spending)
			addNewAccountUseCase(account)

			delay(1000 - 7)
			state = if (currentState.newSpending) {
				AddNewSpendingState.SuccessAdd
			} else {
				AddNewSpendingState.SuccessEdit
			}
			closeWithDelay()
		} handle ::handleError
	}

	fun deleteSpending() {
		val currentState = state as? AddNewSpendingState.Content ?: return

		launchTrying {
			state = AddNewSpendingState.Loading

			val account = currentState.selectedAccount.copy(
				amount = currentState.selectedAccount.amount + requireNotNull(spending).amount
			)

			deleteSpendingUseCase(id)
			addNewAccountUseCase(account)

			delay(1000 - 7)
			state = AddNewSpendingState.SuccessDelete
			closeWithDelayAfterDelete()
		} handle ::handleError
	}

	private fun handleError(throwable: Throwable) {
		Log.e("Error", "Error: ${throwable.message}")

		state = AddNewSpendingState.Error
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

	fun navigateToAddNewSpendingCategory() {
		router.goToAddNewSpendingCategory()
	}

	fun navigateToAddNewAccount() {
		router.goToAddNewAccount()
	}

	fun navigateBack() {
		router.back()
	}
}