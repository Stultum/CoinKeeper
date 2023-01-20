package com.stultus.coinkeeper.shared.income.core.src.main.data.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stultus.coinkeeper.shared.account.src.main.data.dto.AccountDto

@Entity(tableName = "income")
data class IncomeDto(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val id: Long,
	@Embedded(prefix = "account_")
	val account: AccountDto,
	@ColumnInfo(name = "amount")
	val amount: Double,
	@ColumnInfo(name = "date")
	val date: Long,
	@ColumnInfo(name = "comment")
	val comment: String?,
)