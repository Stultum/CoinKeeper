package com.stultus.coinkeeper.app.di

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import com.stultus.coinkeeper.app.navigation.AccountListRouterImpl
import com.stultus.coinkeeper.app.navigation.AddNewAccountRouterImpl
import com.stultus.coinkeeper.app.navigation.AddNewIncomeRouterImpl
import com.stultus.coinkeeper.app.navigation.AddNewSpendingCategoryRouterImpl
import com.stultus.coinkeeper.app.navigation.AddNewSpendingRouterImpl
import com.stultus.coinkeeper.app.navigation.FeedRouterImpl
import com.stultus.coinkeeper.app.navigation.IncomeDetailsRouterImpl
import com.stultus.coinkeeper.app.navigation.SpendingCategoryListRouterImpl
import com.stultus.coinkeeper.app.navigation.SpendingDetailsRouterImpl
import com.stultus.coinkeeper.app.navigation.SpendingHistoryRouterImpl
import com.stultus.coinkeeper.app.navigation.SplashRouterImpl
import com.stultus.coinkeeper.feature.account.add.presentation.AddNewAccountRouter
import com.stultus.coinkeeper.feature.account.list.presentation.AccountListRouter
import com.stultus.coinkeeper.feature.feed.presentation.FeedRouter
import com.stultus.coinkeeper.feature.income.add.presentation.AddNewIncomeRouter
import com.stultus.coinkeeper.feature.income.details.presentation.IncomeDetailsRouter
import com.stultus.coinkeeper.feature.spending.add.presentation.AddNewSpendingCategoryRouter
import com.stultus.coinkeeper.feature.spending.category.list.presentation.SpendingCategoryListRouter
import com.stultus.coinkeeper.feature.spending.core.presentation.AddNewSpendingRouter
import com.stultus.coinkeeper.feature.spending.details.presentation.SpendingDetailsRouter
import com.stultus.coinkeeper.feature.splash.presentation.SplashRouter
import com.stultus.feature.spending.history.presentation.SpendingHistoryRouter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val routersModule = module {
	single<NavController> {
		NavHostController(androidContext()).apply {
			navigatorProvider.addNavigator(ComposeNavigator())
			navigatorProvider.addNavigator(DialogNavigator())
		}
	}

	factory<AddNewAccountRouter> { AddNewAccountRouterImpl(get()) }
	factory<AddNewSpendingCategoryRouter> { AddNewSpendingCategoryRouterImpl(get()) }
	factory<AddNewIncomeRouter> { AddNewIncomeRouterImpl(get()) }
	factory<AddNewSpendingRouter> { AddNewSpendingRouterImpl(get()) }
	factory<AccountListRouter> { AccountListRouterImpl(get()) }
	factory<SpendingCategoryListRouter> { SpendingCategoryListRouterImpl(get()) }
	factory<SpendingHistoryRouter> { SpendingHistoryRouterImpl(get()) }
	factory<SpendingDetailsRouter> { SpendingDetailsRouterImpl(get()) }
	factory<IncomeDetailsRouter> { IncomeDetailsRouterImpl(get()) }
	factory<FeedRouter> { FeedRouterImpl(get()) }
	factory<SplashRouter> { SplashRouterImpl(get()) }
}