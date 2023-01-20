package com.stultus.coinkeeper.shared.income.core.src.main.domain.usecase

import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.income.core.src.main.domain.repository.IncomeRepository

class GetIncomeListUseCase(
	private val incomeRepository: IncomeRepository,
) {

	suspend operator fun invoke(): List<Income> =
		incomeRepository.getList()
}