package com.stultus.coinkeeper.shared.spending.core.src.main.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stultus.coinkeeper.shared.spending.core.src.main.data.dao.SpendingDao
import com.stultus.coinkeeper.shared.spending.core.src.main.data.dto.SpendingDto

@Database(
	entities = [
		SpendingDto::class,
	],
	version = 1,
	exportSchema = false,
)
abstract class SpendingDatabase : RoomDatabase() {

	abstract fun getSpendingDao(): SpendingDao
}