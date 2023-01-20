package com.stultus.coinkeeper.feature.account.list.presentation

import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account

interface AccountListRouter {

	fun back()

	fun goToAddNewAccount()

	fun openEditAccount(account: Account)
}