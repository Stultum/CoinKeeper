package com.stultus.coinkeeper.feature.spending.core.di

import com.stultus.coinkeeper.feature.spending.core.presentation.AddNewSpendingViewModel
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val addNewSpendingModule = module {
	viewModel { (spending: Spending?) ->
		AddNewSpendingViewModel(
			insertSpendingUseCase = get(),
			getAccountListUseCase = get(),
			getSpendingCategoryListUseCase = get(),
			addNewAccountUseCase = get(),
			deleteSpendingUseCase = get(),
			router = get(),
			spending = spending,
		)
	}
}