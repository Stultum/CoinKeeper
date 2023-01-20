package com.stultus.coinkeeper.shared.spending.core.src.main.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stultus.coinkeeper.shared.spending.core.src.main.data.dto.SpendingDto

@Dao
interface SpendingDao {

	@Query("select * from spending")
	suspend fun getList(): List<SpendingDto>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(spendingDto: SpendingDto)

	@Query("delete from spending where :id == id")
	suspend fun delete(id: Long)
}