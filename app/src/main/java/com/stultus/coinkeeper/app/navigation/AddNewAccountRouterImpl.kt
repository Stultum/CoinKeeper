package com.stultus.coinkeeper.app.navigation

import androidx.navigation.NavController
import com.stultus.coinkeeper.feature.account.add.presentation.AddNewAccountRouter

class AddNewAccountRouterImpl(
	private val navController: NavController,
) : AddNewAccountRouter {

	override fun back() {
		navController.popBackStack()
	}
}