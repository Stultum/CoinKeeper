package com.stultus.coinkeeper.shared.income.core.src.main.domain.usecase

import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.income.core.src.main.domain.repository.IncomeRepository

class InsertIncomeUseCase(
	private val incomeRepository: IncomeRepository,
) {

	suspend operator fun invoke(income: Income) {
		incomeRepository.insert(income)
	}
}