package com.stultus.coinkeeper.shared.income.core.src.main.data.database

import android.content.Context
import androidx.room.Room

class IncomeDatabaseBuilder {

	private companion object {

		const val INCOME_DATABASE_NAME = "income_database"
	}

	fun build(context: Context): IncomeDatabase =
		Room.databaseBuilder(
			context,
			IncomeDatabase::class.java,
			INCOME_DATABASE_NAME,
		).build()
}