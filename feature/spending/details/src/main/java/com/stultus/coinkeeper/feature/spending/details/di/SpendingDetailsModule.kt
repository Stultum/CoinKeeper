package com.stultus.coinkeeper.feature.spending.details.di

import com.stultus.coinkeeper.feature.spending.details.presentation.SpendingDetailsViewModel
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val spendingDetailsModule = module {
	viewModel { (spending: Spending) ->
		SpendingDetailsViewModel(
			deleteSpendingUseCase = get(),
			router = get(),
			spending = spending,
			addNewAccountUseCase = get(),
		)
	}
}