package com.stultus.coinkeeper.shared.spending.core.src.main.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stultus.coinkeeper.shared.spending.core.src.main.data.dto.SpendingCategoryDto

@Dao
interface SpendingCategoryDao {

	@Query("select * from spending_category")
	suspend fun getList(): List<SpendingCategoryDto>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(categoryDto: SpendingCategoryDto)

	@Query("delete from spending_category where :id == id")
	suspend fun delete(id: Long)
}