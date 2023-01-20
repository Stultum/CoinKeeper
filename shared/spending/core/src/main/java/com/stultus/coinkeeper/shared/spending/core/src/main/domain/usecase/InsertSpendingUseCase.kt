package com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase

import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.repository.SpendingRepository

class InsertSpendingUseCase(
	private val spendingRepository: SpendingRepository,
) {

	suspend operator fun invoke(spending: Spending) {
		spendingRepository.insert(spending)
	}
}