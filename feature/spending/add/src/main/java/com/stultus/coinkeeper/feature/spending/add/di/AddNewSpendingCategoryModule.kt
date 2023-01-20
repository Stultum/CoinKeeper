package com.stultus.coinkeeper.feature.spending.add.di

import com.stultus.coinkeeper.feature.spending.add.presentation.AddNewSpendingCategoryViewModel
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val addNewSpendingCategoryModule = module {
	viewModel { (spendingCategory: SpendingCategory?) ->
		AddNewSpendingCategoryViewModel(
			insertSpendingCategoryUseCase = get(),
			addNewSpendingCategoryRouter = get(),
			deleteSpendingCategoryScenario = get(),
			spendingCategory = spendingCategory,
		)
	}
}