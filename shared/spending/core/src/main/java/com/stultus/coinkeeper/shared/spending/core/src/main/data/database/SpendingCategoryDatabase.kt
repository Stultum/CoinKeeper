package com.stultus.coinkeeper.shared.spending.core.src.main.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stultus.coinkeeper.shared.spending.core.src.main.data.dao.SpendingCategoryDao
import com.stultus.coinkeeper.shared.spending.core.src.main.data.dto.SpendingCategoryDto

@Database(
	entities = [
		SpendingCategoryDto::class,
	],
	version = 1,
	exportSchema = false,
)
abstract class SpendingCategoryDatabase : RoomDatabase() {

	abstract fun getSpendingCategoryDao(): SpendingCategoryDao
}