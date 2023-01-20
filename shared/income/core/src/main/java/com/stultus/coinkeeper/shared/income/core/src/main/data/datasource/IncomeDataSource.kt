package com.stultus.coinkeeper.shared.income.core.src.main.data.datasource

import com.stultus.coinkeeper.shared.income.core.src.main.data.dao.IncomeDao
import com.stultus.coinkeeper.shared.income.core.src.main.data.dto.IncomeDto

interface IncomeDataSource {

	suspend fun getList(): List<IncomeDto>

	suspend fun insert(incomeDto: IncomeDto)

	suspend fun delete(id: Long)
}

class IncomeDataSourceImpl(
	private val incomeDao: IncomeDao,
) : IncomeDataSource {

	override suspend fun getList(): List<IncomeDto> =
		incomeDao.getList()

	override suspend fun insert(incomeDto: IncomeDto) {
		incomeDao.insert(incomeDto)
	}

	override suspend fun delete(id: Long) {
		incomeDao.delete(id)
	}
}