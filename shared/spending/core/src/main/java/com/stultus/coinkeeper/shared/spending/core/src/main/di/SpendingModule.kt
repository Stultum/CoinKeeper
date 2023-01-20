package com.stultus.coinkeeper.shared.spending.core.src.main.di

import com.stultus.coinkeeper.shared.spending.core.src.main.data.database.SpendingDatabase
import com.stultus.coinkeeper.shared.spending.core.src.main.data.database.SpendingDatabaseBuilder
import com.stultus.coinkeeper.shared.spending.core.src.main.data.datasource.SpendingDataSource
import com.stultus.coinkeeper.shared.spending.core.src.main.data.datasource.SpendingDataSourceImpl
import com.stultus.coinkeeper.shared.spending.core.src.main.data.repository.SpendingRepositoryImpl
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.repository.SpendingRepository
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.DeleteSpendingUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.GetSpendingListUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.InsertSpendingUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val spendingModule = module {
	single { SpendingDatabaseBuilder().build(androidContext()) }
	factory { get<SpendingDatabase>().getSpendingDao() }
	factory<SpendingDataSource> { SpendingDataSourceImpl(get()) }
	factory<SpendingRepository> { SpendingRepositoryImpl(get()) }
	factory { GetSpendingListUseCase(get()) }
	factory { InsertSpendingUseCase(get()) }
	factory { DeleteSpendingUseCase(get()) }
}