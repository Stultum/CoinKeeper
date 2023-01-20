package com.stultus.coinkeeper.shared.delete.di

import com.stultus.coinkeeper.shared.delete.domain.scenario.DeleteAccountScenario
import com.stultus.coinkeeper.shared.delete.domain.scenario.DeleteSpendingCategoryScenario
import org.koin.dsl.module

val deleteModule = module {
	factory { DeleteAccountScenario(get(), get(), get(), get(), get()) }
	factory { DeleteSpendingCategoryScenario(get(), get(), get()) }
}