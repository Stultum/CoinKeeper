package com.stultus.coinkeeper.shared.account.src.main.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stultus.coinkeeper.shared.account.src.main.data.dao.AccountDao
import com.stultus.coinkeeper.shared.account.src.main.data.dto.AccountDto

@Database(
	entities = [
		AccountDto::class,
	],
	version = 1,
	exportSchema = false,
)
abstract class AccountDatabase : RoomDatabase() {

	abstract fun getAccountDao(): AccountDao
}