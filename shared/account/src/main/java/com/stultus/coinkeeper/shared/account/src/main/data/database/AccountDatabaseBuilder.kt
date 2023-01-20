package com.stultus.coinkeeper.shared.account.src.main.data.database

import android.content.Context
import androidx.room.Room

class AccountDatabaseBuilder {

	private companion object {

		const val ACCOUNT_DATABASE_NAME = "account_database"
	}

	fun build(context: Context): AccountDatabase =
		Room.databaseBuilder(
			context,
			AccountDatabase::class.java,
			ACCOUNT_DATABASE_NAME,
		).build()
}