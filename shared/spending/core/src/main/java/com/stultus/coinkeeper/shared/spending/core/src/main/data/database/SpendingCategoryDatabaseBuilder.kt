package com.stultus.coinkeeper.shared.spending.core.src.main.data.database

import android.content.Context
import androidx.room.Room

class SpendingCategoryDatabaseBuilder {

	private companion object {

		const val SPENDING_CATEGORY_DATABASE_NAME = "spending_category_database"
	}

	fun build(context: Context): SpendingCategoryDatabase =
		Room.databaseBuilder(
			context,
			SpendingCategoryDatabase::class.java,
			SPENDING_CATEGORY_DATABASE_NAME,
		).build()
}