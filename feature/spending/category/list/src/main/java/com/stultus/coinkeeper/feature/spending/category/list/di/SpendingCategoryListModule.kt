package com.stultus.coinkeeper.feature.spending.category.list.di

import com.stultus.coinkeeper.feature.spending.category.list.presentation.SpendingCategoryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val spendingCategoryListModule = module {
	viewModel { SpendingCategoryListViewModel(getSpendingCategoryListUseCase = get(), getSpendingListUseCase = get(), router = get()) }
}