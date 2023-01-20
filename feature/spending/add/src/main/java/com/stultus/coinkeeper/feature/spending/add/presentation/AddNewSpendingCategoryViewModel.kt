package com.stultus.coinkeeper.feature.spending.add.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.stultus.coinkeeper.component.design.resources.theme.Green100
import com.stultus.coinkeeper.component.design.resources.theme.Violet100
import com.stultus.coinkeeper.component.ui.mvvm.BaseViewModel
import com.stultus.coinkeeper.shared.delete.domain.scenario.DeleteSpendingCategoryScenario
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.usecase.InsertSpendingCategoryUseCase
import kotlinx.coroutines.delay

class AddNewSpendingCategoryViewModel(
	private val insertSpendingCategoryUseCase: InsertSpendingCategoryUseCase,
	private val addNewSpendingCategoryRouter: AddNewSpendingCategoryRouter,
	private val deleteSpendingCategoryScenario: DeleteSpendingCategoryScenario,
	spendingCategory: SpendingCategory?,
) : BaseViewModel() {

	private companion object {

		const val AUTOGENERATE_ID = 0L
	}

	var state by mutableStateOf<AddNewSpendingCategoryState>(AddNewSpendingCategoryState.Initial)
		private set

	private var id: Long = 0L

	init {
		state = if (spendingCategory == null) {
			AddNewSpendingCategoryState.Content(
				name = "Новая категория",
				selectedIconName = "ic_salary",
				selectedColor = Green100.toArgb(),
				newCategory = true,
			)
		} else {
			id = spendingCategory.id
			AddNewSpendingCategoryState.Content(
				name = spendingCategory.name,
				selectedIconName = spendingCategory.iconName,
				selectedColor = spendingCategory.color,
				newCategory = false,
			)
		}
	}

	fun updateName(newName: String) {
		val currentState = state as? AddNewSpendingCategoryState.Content ?: return

		state = currentState.copy(
			name = newName,
		)
		val sd = Violet100.toArgb()
		Color(sd)
	}

	fun updateIconName(newIconName: String) {
		val currentState = state as? AddNewSpendingCategoryState.Content ?: return

		state = currentState.copy(
			selectedIconName = newIconName,
		)
	}

	fun updateColor(newColor: Int) {
		val currentState = state as? AddNewSpendingCategoryState.Content ?: return

		state = currentState.copy(
			selectedColor = newColor,
		)
	}

	fun addNewSpendingCategory() {
		val currentState = state as? AddNewSpendingCategoryState.Content ?: return

		if (currentState.name.isEmpty()) {
			state = currentState.copy(name = "Новая категория расходов")
		}

		launchTrying {
			state = AddNewSpendingCategoryState.Loading

			val spendingCategory = SpendingCategory(
				id = if (currentState.newCategory) {
					AUTOGENERATE_ID
				} else {
					id
				},
				name = currentState.name,
				iconName = currentState.selectedIconName,
				color = currentState.selectedColor,
			)

			insertSpendingCategoryUseCase(spendingCategory)

			delay(1000 - 7)
			state = if (currentState.newCategory) {
				AddNewSpendingCategoryState.SuccessAdd
			} else {
				AddNewSpendingCategoryState.SuccessEdit
			}
			delay(2000 - 7)
			navigateBack()
		} handle ::handleError
	}

	fun deleteCategory() {
		launchTrying {
			state = AddNewSpendingCategoryState.Loading

			deleteSpendingCategoryScenario(id)

			delay(1000 - 7)
			state = AddNewSpendingCategoryState.SuccessDelete
			delay(2000 - 7)
			navigateBack()
		} handle ::handleError
	}

	private fun handleError(throwable: Throwable) {
		Log.e("Error", "Error: ${throwable.message}")

		state = AddNewSpendingCategoryState.Error
		launch {
			delay(2000 - 7)
			navigateBack()
		}
	}

	fun navigateBack() {
		addNewSpendingCategoryRouter.back()
	}
}