package com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase

import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.repository.SpendingCategoryRepository

class InsertSpendingCategoryUseCase(
	private val spendingCategoryRepository: SpendingCategoryRepository,
) {

	suspend operator fun invoke(category: SpendingCategory) {
		spendingCategoryRepository.insert(category)
	}
}