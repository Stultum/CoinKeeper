package com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase

import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.repository.SpendingCategoryRepository

class GetSpendingCategoryListUseCase(
	private val spendingCategoryRepository: SpendingCategoryRepository,
) {

	suspend operator fun invoke(): List<SpendingCategory> =
		spendingCategoryRepository.getList()
}