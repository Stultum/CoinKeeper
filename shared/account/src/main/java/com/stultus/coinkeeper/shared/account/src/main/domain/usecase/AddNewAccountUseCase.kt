package com.stultus.coinkeeper.shared.account.src.main.domain.usecase

import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account
import com.stultus.coinkeeper.shared.account.src.main.domain.repository.AccountRepository

class AddNewAccountUseCase(
	private val accountRepository: AccountRepository,
) {

	suspend operator fun invoke(account: Account) {
		accountRepository.addNewAccount(account)
	}
}