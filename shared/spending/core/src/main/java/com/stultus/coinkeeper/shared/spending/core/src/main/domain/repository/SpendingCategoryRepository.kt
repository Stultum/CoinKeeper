package com.stultus.coinkeeper.shared.spending.core.src.main.domain.repository

import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory

interface SpendingCategoryRepository {

	suspend fun getList(): List<SpendingCategory>

	suspend fun insert(category: SpendingCategory)

	suspend fun delete(id: Long)
}