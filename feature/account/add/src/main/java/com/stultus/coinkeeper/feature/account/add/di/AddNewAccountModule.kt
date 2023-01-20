package com.stultus.coinkeeper.feature.account.add.di

import com.stultus.coinkeeper.feature.account.add.presentation.AddNewAccountViewModel
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val addNewAccountModule = module {
	viewModel { (account: Account?) -> AddNewAccountViewModel(addNewAccountUseCase = get(), deleteAccountScenario = get(), router = get(), account = account) }
}