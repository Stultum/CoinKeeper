package com.stultus.coinkeeper.shared.delete.domain.scenario

import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.DeleteSpendingCategoryUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.DeleteSpendingUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.GetSpendingListUseCase

class DeleteSpendingCategoryScenario(
	private val deleteSpendingCategoryUseCase: DeleteSpendingCategoryUseCase,
	private val getSpendingListUseCase: GetSpendingListUseCase,
	private val deleteSpendingUseCase: DeleteSpendingUseCase,
) {

	suspend operator fun invoke(id: Long) {
		deleteAllBindSpending(id)

		deleteSpendingCategoryUseCase.invoke(id)
	}

	private suspend fun deleteAllBindSpending(id: Long) {
		val spendingList = getSpendingListUseCase.invoke()

		spendingList.forEach { spending ->
			if (spending.category.id == id) {
				deleteSpendingUseCase(spending.id)
			}
		}
	}
}