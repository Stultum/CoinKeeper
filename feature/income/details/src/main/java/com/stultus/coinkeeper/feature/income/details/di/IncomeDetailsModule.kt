package com.stultus.coinkeeper.feature.income.details.di

import com.stultus.coinkeeper.feature.income.details.presentation.IncomeDetailsViewModel
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val incomeDetailsModule = module {
	viewModel { (income: Income) -> IncomeDetailsViewModel(deleteIncomeUseCase = get(), router = get(), income = income, addNewAccountUseCase = get()) }
}