package com.stultus.coinkeeper.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.stultus.coinkeeper.app.navigation.NavigationTree
import com.stultus.coinkeeper.app.navigation.fromJson
import com.stultus.coinkeeper.feature.account.add.ui.AddNewAccountScreen
import com.stultus.coinkeeper.feature.account.list.ui.AccountListScreen
import com.stultus.coinkeeper.feature.feed.ui.FeedScreen
import com.stultus.coinkeeper.feature.income.add.ui.AddNewIncomeScreen
import com.stultus.coinkeeper.feature.income.details.ui.IncomeDetailsScreen
import com.stultus.coinkeeper.feature.spending.add.ui.AddNewSpendingCategoryScreen
import com.stultus.coinkeeper.feature.spending.category.list.ui.SpendingCategoryListScreen
import com.stultus.coinkeeper.feature.spending.core.ui.AddNewSpendingScreen
import com.stultus.coinkeeper.feature.spending.details.ui.SpendingDetailsScreen
import com.stultus.coinkeeper.feature.splash.ui.SplashScreen
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory
import com.stultus.feature.spending.history.ui.SpendingHistoryScreen
import org.koin.java.KoinJavaComponent.inject

@Composable
fun ApplicationScreen() {
	val navController by inject<NavHostController>(NavController::class.java)

	NavHost(navController = navController, startDestination = NavigationTree.Splash.name) {
		composable(NavigationTree.AddNewAccount.name) { AddNewAccountScreen() }
		composable(NavigationTree.Splash.name) { SplashScreen() }
		composable(NavigationTree.AddNewSpendingCategory.name) { AddNewSpendingCategoryScreen() }
		composable(NavigationTree.AddNewIncome.name) { AddNewIncomeScreen() }
		composable(NavigationTree.AddNewSpending.name) { AddNewSpendingScreen() }
		composable(NavigationTree.AccountList.name) { AccountListScreen() }
		composable(NavigationTree.SpendingCategoryList.name) { SpendingCategoryListScreen() }
		composable(NavigationTree.SpendingHistory.name) { SpendingHistoryScreen() }
		composable(NavigationTree.Feed.name) { FeedScreen() }

		composable(NavigationTree.AddNewIncome.name + "/{income}") { navBackStack ->
			navBackStack.arguments?.getString("income")?.let { incomeJsonString ->
				val income = incomeJsonString.fromJson(Income::class.java)
				AddNewIncomeScreen(income = income)
			}
		}

		composable(NavigationTree.AddNewSpending.name + "/{spending}") { navBackStack ->
			navBackStack.arguments?.getString("spending")?.let { spendingJsonString ->
				val spending = spendingJsonString.fromJson(Spending::class.java)
				AddNewSpendingScreen(spending = spending)
			}
		}

		composable(NavigationTree.AddNewSpendingCategory.name + "/{category}") { navBackStack ->
			navBackStack.arguments?.getString("category")?.let { categoryJsonString ->
				val category = categoryJsonString.fromJson(SpendingCategory::class.java)
				AddNewSpendingCategoryScreen(spendingCategory = category)
			}
		}

		composable(NavigationTree.AddNewAccount.name + "/{account}") { navBackStack ->
			navBackStack.arguments?.getString("account")?.let { accountJsonString ->
				val account = accountJsonString.fromJson(Account::class.java)
				AddNewAccountScreen(account = account)
			}
		}
		composable(NavigationTree.SpendingDetails.name + "/{spending}") { navBackStack ->
			navBackStack.arguments?.getString("spending")?.let { spendingJsonString ->
				val spending = spendingJsonString.fromJson(Spending::class.java)
				SpendingDetailsScreen(spending = spending)
			}
		}

		composable(NavigationTree.IncomeDetails.name + "/{income}") { navBackStack ->
			navBackStack.arguments?.getString("income")?.let { incomeJsonString ->
				val income = incomeJsonString.fromJson(Income::class.java)
				IncomeDetailsScreen(income = income)
			}
		}
	}
}