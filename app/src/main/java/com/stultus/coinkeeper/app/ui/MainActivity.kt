package com.stultus.coinkeeper.app.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.stultus.coinkeeper.app.di.routersModule
import com.stultus.coinkeeper.component.design.resources.theme.CoinKeeperTheme
import com.stultus.coinkeeper.feature.account.add.di.addNewAccountModule
import com.stultus.coinkeeper.feature.account.list.di.accountListModule
import com.stultus.coinkeeper.feature.feed.di.feedModule
import com.stultus.coinkeeper.feature.income.add.di.addNewIncomeModule
import com.stultus.coinkeeper.feature.income.details.di.incomeDetailsModule
import com.stultus.coinkeeper.feature.spending.add.di.addNewSpendingCategoryModule
import com.stultus.coinkeeper.feature.spending.category.list.di.spendingCategoryListModule
import com.stultus.coinkeeper.feature.spending.core.di.addNewSpendingModule
import com.stultus.coinkeeper.feature.spending.details.di.spendingDetailsModule
import com.stultus.coinkeeper.feature.splash.di.splashModule
import com.stultus.coinkeeper.shared.account.src.main.di.accountModule
import com.stultus.coinkeeper.shared.delete.di.deleteModule
import com.stultus.coinkeeper.shared.income.core.src.main.di.incomeModule
import com.stultus.coinkeeper.shared.spending.core.src.main.di.spendingCategoryModule
import com.stultus.coinkeeper.shared.spending.core.src.main.di.spendingModule
import com.stultus.feature.spending.history.di.spendingHistoryModule
import org.koin.java.KoinJavaComponent

class MainActivity : FragmentActivity() {

	private companion object {

		val modulesList = listOf(
			routersModule,
			accountModule,
			spendingCategoryModule,
			incomeModule,
			spendingModule,
			addNewAccountModule,
			addNewSpendingCategoryModule,
			addNewIncomeModule,
			addNewSpendingModule,
			accountListModule,
			spendingCategoryListModule,
			spendingHistoryModule,
			spendingDetailsModule,
			incomeDetailsModule,
			feedModule,
			deleteModule,
			splashModule,
		)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		KoinJavaComponent.getKoin().loadModules(modulesList)

		setContent {
			CoinKeeperTheme {
				ApplicationScreen()
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()

		KoinJavaComponent.getKoin().unloadModules(modulesList)
	}
}