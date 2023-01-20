package com.stultus.coinkeeper.shared.spending.core.src.main.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spending_category")
data class SpendingCategoryDto(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val id: Long,
	@ColumnInfo(name = "name")
	val name: String,
	@ColumnInfo(name = "iconName")
	val iconName: String,
	@ColumnInfo(name = "color")
	val color: Int,
)
