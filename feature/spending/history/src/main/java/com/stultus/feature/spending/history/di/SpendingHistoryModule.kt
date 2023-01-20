package com.stultus.feature.spending.history.di

import com.stultus.feature.spending.history.presentation.SpendingHistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val spendingHistoryModule = module {
	viewModel { SpendingHistoryViewModel(getSpendingListUseCase = get(), getIncomeListUseCase = get(), router = get()) }
}