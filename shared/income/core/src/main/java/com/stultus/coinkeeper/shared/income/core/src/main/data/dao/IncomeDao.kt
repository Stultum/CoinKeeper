package com.stultus.coinkeeper.shared.income.core.src.main.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stultus.coinkeeper.shared.income.core.src.main.data.dto.IncomeDto

@Dao
interface IncomeDao {

	@Query("select * from income")
	suspend fun getList(): List<IncomeDto>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(incomeDto: IncomeDto)

	@Query("delete from income where :id == id")
	suspend fun delete(id: Long)
}