package com.stultus.coinkeeper.shared.spending.core.src.main.data.datasource

import com.stultus.coinkeeper.shared.spending.core.src.main.data.dao.SpendingDao
import com.stultus.coinkeeper.shared.spending.core.src.main.data.dto.SpendingDto

interface SpendingDataSource {

	suspend fun getList(): List<SpendingDto>

	suspend fun insert(spendingDto: SpendingDto)

	suspend fun delete(id: Long)
}

class SpendingDataSourceImpl(
	private val spendingDao: SpendingDao,
) : SpendingDataSource {

	override suspend fun getList(): List<SpendingDto> =
		spendingDao.getList()

	override suspend fun insert(spendingDto: SpendingDto) {
		spendingDao.insert(spendingDto)
	}

	override suspend fun delete(id: Long) {
		spendingDao.delete(id)
	}
}