package com.stultus.coinkeeper.feature.spending.details.presentation

import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending

interface SpendingDetailsRouter {

	fun back()

	fun openEditSpending(spending: Spending)
}