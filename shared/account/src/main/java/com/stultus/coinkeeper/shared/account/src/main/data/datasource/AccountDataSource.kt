package com.stultus.coinkeeper.shared.account.src.main.data.datasource

import com.stultus.coinkeeper.shared.account.src.main.data.dao.AccountDao
import com.stultus.coinkeeper.shared.account.src.main.data.dto.AccountDto

interface AccountDataSource {

	suspend fun getAccountList(): List<AccountDto>

	suspend fun addNewAccount(accountDto: AccountDto)

	suspend fun deleteAccount(id: Long)
}

class AccountDataSourceImpl(
	private val accountDao: AccountDao,
) : AccountDataSource {

	override suspend fun getAccountList(): List<AccountDto> =
		accountDao.getAccountList()

	override suspend fun addNewAccount(accountDto: AccountDto) {
		accountDao.insert(accountDto)
	}

	override suspend fun deleteAccount(id: Long) {
		accountDao.delete(id)
	}
}