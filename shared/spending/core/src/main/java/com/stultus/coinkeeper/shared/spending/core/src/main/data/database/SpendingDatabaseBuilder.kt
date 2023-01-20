package com.stultus.coinkeeper.shared.spending.core.src.main.data.database

import android.content.Context
import androidx.room.Room

class SpendingDatabaseBuilder {

	private companion object {

		const val SPENDING_DATABASE_NAME = "spending_database"
	}

	fun build(context: Context): SpendingDatabase =
		Room.databaseBuilder(
			context,
			SpendingDatabase::class.java,
			SPENDING_DATABASE_NAME,
		).build()
}