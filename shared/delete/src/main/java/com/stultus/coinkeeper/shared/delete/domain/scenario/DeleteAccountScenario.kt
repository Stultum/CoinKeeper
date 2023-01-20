package com.stultus.coinkeeper.shared.delete.domain.scenario

import com.stultus.coinkeeper.shared.account.src.main.domain.usecase.DeleteAccountUseCase
import com.stultus.coinkeeper.shared.income.core.src.main.domain.usecase.DeleteIncomeUseCase
import com.stultus.coinkeeper.shared.income.core.src.main.domain.usecase.GetIncomeListUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.DeleteSpendingUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.GetSpendingListUseCase

class DeleteAccountScenario(
	private val deleteAccountUseCase: DeleteAccountUseCase,
	private val deleteSpendingUseCase: DeleteSpendingUseCase,
	private val deleteIncomeUseCase: DeleteIncomeUseCase,
	private val getSpendingListUseCase: GetSpendingListUseCase,
	private val getIncomeListUseCase: GetIncomeListUseCase,
) {

	suspend operator fun invoke(id: Long) {
		deleteAllBindIncome(id)
		deleteAllBindSpending(id)

		deleteAccountUseCase(id)
	}

	private suspend fun deleteAllBindSpending(id: Long) {
		val spendingList = getSpendingListUseCase.invoke()

		spendingList.forEach { spending ->
			if (spending.account.id == id) {
				deleteSpendingUseCase(spending.id)
			}
		}
	}

	private suspend fun deleteAllBindIncome(id: Long) {
		val incomeList = getIncomeListUseCase.invoke()

		incomeList.forEach { income ->
			if (income.account.id == id) {
				deleteIncomeUseCase(income.id)
			}
		}
	}
}