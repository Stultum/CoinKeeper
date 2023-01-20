package com.stultus.coinkeeper.shared.income.core.src.main.di

import com.stultus.coinkeeper.shared.income.core.src.main.data.database.IncomeDatabase
import com.stultus.coinkeeper.shared.income.core.src.main.data.database.IncomeDatabaseBuilder
import com.stultus.coinkeeper.shared.income.core.src.main.data.datasource.IncomeDataSource
import com.stultus.coinkeeper.shared.income.core.src.main.data.datasource.IncomeDataSourceImpl
import com.stultus.coinkeeper.shared.income.core.src.main.data.repository.IncomeRepositoryImpl
import com.stultus.coinkeeper.shared.income.core.src.main.domain.repository.IncomeRepository
import com.stultus.coinkeeper.shared.income.core.src.main.domain.usecase.DeleteIncomeUseCase
import com.stultus.coinkeeper.shared.income.core.src.main.domain.usecase.GetIncomeListUseCase
import com.stultus.coinkeeper.shared.income.core.src.main.domain.usecase.InsertIncomeUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val incomeModule = module {
	single { IncomeDatabaseBuilder().build(androidContext()) }
	factory { get<IncomeDatabase>().getIncomeDao() }
	factory<IncomeDataSource> { IncomeDataSourceImpl(get()) }
	factory<IncomeRepository> { IncomeRepositoryImpl(get()) }
	factory { GetIncomeListUseCase(get()) }
	factory { InsertIncomeUseCase(get()) }
	factory { DeleteIncomeUseCase(get()) }
}