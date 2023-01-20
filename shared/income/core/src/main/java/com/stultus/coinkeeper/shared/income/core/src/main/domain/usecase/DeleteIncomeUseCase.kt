package com.stultus.coinkeeper.shared.income.core.src.main.domain.usecase

import com.stultus.coinkeeper.shared.income.core.src.main.domain.repository.IncomeRepository

class DeleteIncomeUseCase(
	private val incomeRepository: IncomeRepository,
) {

	suspend operator fun invoke(id: Long) {
		incomeRepository.delete(id)
	}
}