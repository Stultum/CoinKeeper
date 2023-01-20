package com.stultus.coinkeeper.shared.spending.core.src.main.data.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account

@Entity(tableName = "spending")
data class SpendingDto(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val id: Long,
	@Embedded(prefix = "spending_cat_")
	val category: SpendingCategoryDto,
	@Embedded(prefix = "account_")
	val account: Account,
	@ColumnInfo(name = "amount")
	val amount: Double,
	@ColumnInfo(name = "date")
	val date: Long,
	@ColumnInfo(name = "comment")
	val comment: String?,
)