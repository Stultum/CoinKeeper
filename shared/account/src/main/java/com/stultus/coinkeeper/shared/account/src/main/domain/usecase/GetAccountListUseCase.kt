package com.stultus.coinkeeper.shared.account.src.main.domain.usecase

import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account
import com.stultus.coinkeeper.shared.account.src.main.domain.repository.AccountRepository

class GetAccountListUseCase(
	private val accountRepository: AccountRepository,
) {

	suspend operator fun invoke(): List<Account> =
		accountRepository.getAccountList()
}