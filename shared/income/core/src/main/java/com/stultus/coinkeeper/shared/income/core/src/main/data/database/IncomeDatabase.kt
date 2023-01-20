package com.stultus.coinkeeper.shared.income.core.src.main.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stultus.coinkeeper.shared.income.core.src.main.data.dao.IncomeDao
import com.stultus.coinkeeper.shared.income.core.src.main.data.dto.IncomeDto

@Database(
	entities = [
		IncomeDto::class,
	],
	version = 1,
	exportSchema = false,
)
abstract class IncomeDatabase : RoomDatabase() {

	abstract fun getIncomeDao(): IncomeDao
}