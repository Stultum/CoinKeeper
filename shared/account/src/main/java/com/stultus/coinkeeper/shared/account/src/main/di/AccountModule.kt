package com.stultus.coinkeeper.shared.account.src.main.di

import com.stultus.coinkeeper.shared.account.src.main.data.database.AccountDatabase
import com.stultus.coinkeeper.shared.account.src.main.data.database.AccountDatabaseBuilder
import com.stultus.coinkeeper.shared.account.src.main.data.datasource.AccountDataSource
import com.stultus.coinkeeper.shared.account.src.main.data.datasource.AccountDataSourceImpl
import com.stultus.coinkeeper.shared.account.src.main.data.repository.AccountRepositoryImpl
import com.stultus.coinkeeper.shared.account.src.main.domain.repository.AccountRepository
import com.stultus.coinkeeper.shared.account.src.main.domain.usecase.AddNewAccountUseCase
import com.stultus.coinkeeper.shared.account.src.main.domain.usecase.DeleteAccountUseCase
import com.stultus.coinkeeper.shared.account.src.main.domain.usecase.GetAccountListUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val accountModule = module {

	single { AccountDatabaseBuilder().build(androidContext()) }
	factory { get<AccountDatabase>().getAccountDao() }
	factory<AccountDataSource> { AccountDataSourceImpl(get()) }
	factory<AccountRepository> { AccountRepositoryImpl(get()) }
	factory { DeleteAccountUseCase(get()) }
	factory { GetAccountListUseCase(get()) }
	factory { AddNewAccountUseCase(get()) }
}