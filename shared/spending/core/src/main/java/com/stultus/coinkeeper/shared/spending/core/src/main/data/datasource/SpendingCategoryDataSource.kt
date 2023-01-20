package com.stultus.coinkeeper.shared.spending.core.src.main.data.datasource

import com.stultus.coinkeeper.shared.spending.core.src.main.data.dao.SpendingCategoryDao
import com.stultus.coinkeeper.shared.spending.core.src.main.data.dto.SpendingCategoryDto

interface SpendingCategoryDataSource {

	suspend fun getList(): List<SpendingCategoryDto>

	suspend fun insert(categoryDto: SpendingCategoryDto)

	suspend fun delete(id: Long)
}

class SpendingCategoryDataSourceImpl(
	private val spendingCategoryDao: SpendingCategoryDao,
) : SpendingCategoryDataSource {

	override suspend fun getList(): List<SpendingCategoryDto> =
		spendingCategoryDao.getList()

	override suspend fun insert(categoryDto: SpendingCategoryDto) {
		spendingCategoryDao.insert(categoryDto)
	}

	override suspend fun delete(id: Long) {
		spendingCategoryDao.delete(id)
	}
}