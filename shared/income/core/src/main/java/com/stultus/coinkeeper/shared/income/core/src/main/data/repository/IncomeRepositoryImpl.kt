package com.stultus.coinkeeper.shared.income.core.src.main.data.repository

import com.stultus.coinkeeper.shared.income.core.src.main.data.datasource.IncomeDataSource
import com.stultus.coinkeeper.shared.income.core.src.main.data.dto.IncomeDto
import com.stultus.coinkeeper.shared.income.core.src.main.data.mapper.toDto
import com.stultus.coinkeeper.shared.income.core.src.main.data.mapper.toEntity
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.income.core.src.main.domain.repository.IncomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IncomeRepositoryImpl(
	private val incomeDataSource: IncomeDataSource,
) : IncomeRepository {

	override suspend fun getList(): List<Income> =
		withContext(Dispatchers.IO) {
			incomeDataSource.getList().map(IncomeDto::toEntity)
		}

	override suspend fun insert(income: Income) {
		withContext(Dispatchers.IO) {
			incomeDataSource.insert(income.toDto())
		}
	}

	override suspend fun delete(id: Long) {
		withContext(Dispatchers.IO) {
			incomeDataSource.delete(id)
		}
	}
}