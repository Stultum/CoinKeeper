package com.stultus.coinkeeper.shared.spending.core.src.main.domain.repository

import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending

interface SpendingRepository {

	suspend fun getList(): List<Spending>

	suspend fun insert(spending: Spending)

	suspend fun delete(id: Long)
}