package com.stultus.coinkeeper.shared.account.src.main.data.repository

import com.stultus.coinkeeper.shared.account.src.main.data.datasource.AccountDataSource
import com.stultus.coinkeeper.shared.account.src.main.data.dto.AccountDto
import com.stultus.coinkeeper.shared.account.src.main.data.mapper.toDto
import com.stultus.coinkeeper.shared.account.src.main.data.mapper.toEntity
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account
import com.stultus.coinkeeper.shared.account.src.main.domain.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepositoryImpl(
	private val accountDataSource: AccountDataSource,
) : AccountRepository {

	override suspend fun getAccountList(): List<Account> =
		withContext(Dispatchers.IO) {
			accountDataSource.getAccountList().map(AccountDto::toEntity)
		}

	override suspend fun addNewAccount(account: Account) {
		withContext(Dispatchers.IO) {
			accountDataSource.addNewAccount(account.toDto())
		}
	}

	override suspend fun deleteAccount(id: Long) {
		withContext(Dispatchers.IO) {
			accountDataSource.deleteAccount(id)
		}
	}
}