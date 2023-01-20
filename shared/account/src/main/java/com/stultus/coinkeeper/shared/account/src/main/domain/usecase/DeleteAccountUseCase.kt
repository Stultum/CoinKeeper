package com.stultus.coinkeeper.shared.account.src.main.domain.usecase

import com.stultus.coinkeeper.shared.account.src.main.domain.repository.AccountRepository

class DeleteAccountUseCase(
	private val accountRepository: AccountRepository,
) {

	suspend operator fun invoke(id: Long) {
		accountRepository.deleteAccount(id)
	}
}