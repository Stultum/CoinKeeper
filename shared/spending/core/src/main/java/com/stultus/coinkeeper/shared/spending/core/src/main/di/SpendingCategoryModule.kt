package com.stultus.coinkeeper.shared.spending.core.src.main.di

import com.stultus.coinkeeper.shared.spending.core.src.main.data.database.SpendingCategoryDatabase
import com.stultus.coinkeeper.shared.spending.core.src.main.data.database.SpendingCategoryDatabaseBuilder
import com.stultus.coinkeeper.shared.spending.core.src.main.data.datasource.SpendingCategoryDataSource
import com.stultus.coinkeeper.shared.spending.core.src.main.data.datasource.SpendingCategoryDataSourceImpl
import com.stultus.coinkeeper.shared.spending.core.src.main.data.repository.SpendingCategoryRepositoryImpl
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.repository.SpendingCategoryRepository
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.DeleteSpendingCategoryUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.GetSpendingCategoryListUseCase
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.InsertSpendingCategoryUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val spendingCategoryModule = module {
	single { SpendingCategoryDatabaseBuilder().build(androidContext()) }
	factory { get<SpendingCategoryDatabase>().getSpendingCategoryDao() }
	factory<SpendingCategoryDataSource> { SpendingCategoryDataSourceImpl(get()) }
	factory<SpendingCategoryRepository> { SpendingCategoryRepositoryImpl(get()) }
	factory { GetSpendingCategoryListUseCase(get()) }
	factory { InsertSpendingCategoryUseCase(get()) }
	factory { DeleteSpendingCategoryUseCase(get()) }
}