package com.stultus.coinkeeper.shared.account.src.main.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stultus.coinkeeper.shared.account.src.main.data.dto.AccountDto

@Dao
interface AccountDao {

	@Query("select * from account")
	suspend fun getAccountList(): List<AccountDto>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(accountDto: AccountDto)

	@Query("delete from account where :id == id")
	suspend fun delete(id: Long)
}