package com.stultus.coinkeeper.shared.account.src.main.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountDto(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val id: Long,
	@ColumnInfo(name = "name")
	val name: String,
	@ColumnInfo(name = "amount")
	val amount: Double,
	@ColumnInfo(name = "iconName")
	val iconName: String,
)