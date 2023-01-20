package com.stultus.coinkeeper.feature.feed.di

import com.stultus.coinkeeper.feature.feed.presentation.FeedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val feedModule = module {
	viewModel {
		FeedViewModel(
			getIncomeListUseCase = get(),
			getSpendingListUseCase = get(),
			getSpendingCategoryListUseCase = get(),
			getAccountListUseCase = get(),
			router = get(),
		)
	}
}