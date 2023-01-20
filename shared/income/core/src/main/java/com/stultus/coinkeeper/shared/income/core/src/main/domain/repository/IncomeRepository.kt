package com.stultus.coinkeeper.shared.income.core.src.main.domain.repository

import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income

interface IncomeRepository {

	suspend fun getList(): List<Income>

	suspend fun insert(income: Income)

	suspend fun delete(id: Long)
}