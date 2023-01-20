package com.stultus.coinkeeper.shared.spending.core.src.main.data.repository

import com.stultus.coinkeeper.shared.spending.core.src.main.data.datasource.SpendingCategoryDataSource
import com.stultus.coinkeeper.shared.spending.core.src.main.data.dto.SpendingCategoryDto
import com.stultus.coinkeeper.shared.spending.core.src.main.data.mapper.toDto
import com.stultus.coinkeeper.shared.spending.core.src.main.data.mapper.toEntity
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.repository.SpendingCategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpendingCategoryRepositoryImpl(
	private val spendingCategoryDataSource: SpendingCategoryDataSource,
) : SpendingCategoryRepository {

	override suspend fun getList(): List<SpendingCategory> =
		withContext(Dispatchers.IO) {
			spendingCategoryDataSource.getList().map(SpendingCategoryDto::toEntity)
		}

	override suspend fun insert(category: SpendingCategory) {
		withContext(Dispatchers.IO) {
			spendingCategoryDataSource.insert(category.toDto())
		}
	}

	override suspend fun delete(id: Long) {
		withContext(Dispatchers.IO) {
			spendingCategoryDataSource.delete(id)
		}
	}
}