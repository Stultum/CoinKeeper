package com.stultus.coinkeeper.shared.spending.core.src.main.data.mapper

import com.stultus.coinkeeper.shared.spending.core.src.main.data.dto.SpendingCategoryDto
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory

fun SpendingCategory.toDto(): SpendingCategoryDto = SpendingCategoryDto(
	id = id,
	name = name,
	iconName = iconName,
	color = color,
)

fun SpendingCategoryDto.toEntity(): SpendingCategory = SpendingCategory(
	id = id,
	name = name,
	iconName = iconName,
	color = color,
)