package com.stultus.coinkeeper.app.navigation

import androidx.navigation.NavController
import com.stultus.coinkeeper.feature.account.list.presentation.AccountListRouter
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account

class AccountListRouterImpl(
	private val navController: NavController,
) : AccountListRouter {

	override fun back() {
		navController.popBackStack()
	}

	override fun goToAddNewAccount() {
		navController.navigate(NavigationTree.AddNewAccount.name)
	}

	override fun openEditAccount(account: Account) {
		val accountString = account.toJson()

		navController.navigate(NavigationTree.AddNewAccount.name + "/$accountString")
	}
}