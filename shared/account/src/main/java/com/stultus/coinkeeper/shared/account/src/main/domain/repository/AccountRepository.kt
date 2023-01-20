package com.stultus.coinkeeper.shared.account.src.main.domain.repository

import com.stultus.coinkeeper.shared.account.src.main.data.dto.AccountDto
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account

interface AccountRepository {

	suspend fun getAccountList(): List<Account>

	suspend fun addNewAccount(account: Account)

	suspend fun deleteAccount(id: Long)
}