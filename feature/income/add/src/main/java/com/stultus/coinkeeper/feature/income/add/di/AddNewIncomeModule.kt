package com.stultus.coinkeeper.feature.income.add.di

import com.stultus.coinkeeper.feature.income.add.presentation.AddNewIncomeViewModel
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val addNewIncomeModule = module {
	viewModel { (income: Income?) ->
		AddNewIncomeViewModel(
			insertIncomeUseCase = get(),
			getAccountListUseCase = get(),
			router = get(),
			deleteIncomeUseCase = get(),
			addNewAccountUseCase = get(),
			income = income,
		)
	}
}