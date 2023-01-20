package com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase

import com.stultus.coinkeeper.shared.spending.core.src.main.domain.repository.SpendingRepository

class DeleteSpendingUseCase(
	private val spendingRepository: SpendingRepository,
) {

	suspend operator fun invoke(id: Long) {
		spendingRepository.delete(id)
	}
}