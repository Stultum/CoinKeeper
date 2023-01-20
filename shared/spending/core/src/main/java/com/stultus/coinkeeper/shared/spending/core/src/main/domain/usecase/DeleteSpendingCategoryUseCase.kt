package com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase

import com.stultus.coinkeeper.shared.spending.core.src.main.domain.repository.SpendingCategoryRepository

class DeleteSpendingCategoryUseCase(
	private val spendingCategoryRepository: SpendingCategoryRepository,
) {

	suspend operator fun invoke(id: Long) {
		spendingCategoryRepository.delete(id)
	}
}