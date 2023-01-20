package com.stultus.coinkeeper.shared.spending.core.src.main.data.repository

import com.stultus.coinkeeper.shared.spending.core.src.main.data.datasource.SpendingDataSource
import com.stultus.coinkeeper.shared.spending.core.src.main.data.dto.SpendingDto
import com.stultus.coinkeeper.shared.spending.core.src.main.data.mapper.toDto
import com.stultus.coinkeeper.shared.spending.core.src.main.data.mapper.toEntity
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.repository.SpendingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpendingRepositoryImpl(
	private val spendingDataSource: SpendingDataSource,
) : SpendingRepository {

	override suspend fun getList(): List<Spending> =
		withContext(Dispatchers.IO) {
			spendingDataSource.getList().map(SpendingDto::toEntity)
		}

	override suspend fun insert(spending: Spending) {
		withContext(Dispatchers.IO) {
			spendingDataSource.insert(spending.toDto())
		}
	}

	override suspend fun delete(id: Long) {
		withContext(Dispatchers.IO) {
			spendingDataSource.delete(id)
		}
	}
}