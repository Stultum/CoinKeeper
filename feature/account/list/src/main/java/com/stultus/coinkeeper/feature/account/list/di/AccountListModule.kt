package com.stultus.coinkeeper.feature.account.list.di

import com.stultus.coinkeeper.feature.account.list.presentation.AccountListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountListModule = module {
	viewModel { AccountListViewModel(getAccountListUseCase = get(), router = get()) }
}