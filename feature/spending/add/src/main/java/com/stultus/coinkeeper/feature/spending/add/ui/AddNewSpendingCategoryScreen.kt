package com.stultus.coinkeeper.feature.spending.add.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.stultus.coinkeeper.component.design.resources.theme.Blue100
import com.stultus.coinkeeper.component.design.resources.theme.Blue20
import com.stultus.coinkeeper.component.design.resources.theme.Blue40
import com.stultus.coinkeeper.component.design.resources.theme.Dark100
import com.stultus.coinkeeper.component.design.resources.theme.Dark25
import com.stultus.coinkeeper.component.design.resources.theme.Dark50
import com.stultus.coinkeeper.component.design.resources.theme.Dark50Opacity
import com.stultus.coinkeeper.component.design.resources.theme.Green100
import com.stultus.coinkeeper.component.design.resources.theme.Green20
import com.stultus.coinkeeper.component.design.resources.theme.Green40
import com.stultus.coinkeeper.component.design.resources.theme.Inter
import com.stultus.coinkeeper.component.design.resources.theme.Light100
import com.stultus.coinkeeper.component.design.resources.theme.Light80
import com.stultus.coinkeeper.component.design.resources.theme.Light80Opacity64
import com.stultus.coinkeeper.component.design.resources.theme.Red100
import com.stultus.coinkeeper.component.design.resources.theme.Red20
import com.stultus.coinkeeper.component.design.resources.theme.Red40
import com.stultus.coinkeeper.component.design.resources.theme.Violet100
import com.stultus.coinkeeper.component.design.resources.theme.Violet20
import com.stultus.coinkeeper.component.design.resources.theme.Yellow100
import com.stultus.coinkeeper.component.design.resources.theme.Yellow20
import com.stultus.coinkeeper.feature.spending.add.R
import com.stultus.coinkeeper.feature.spending.add.presentation.AddNewSpendingCategoryState
import com.stultus.coinkeeper.feature.spending.add.presentation.AddNewSpendingCategoryViewModel
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AddNewSpendingCategoryScreen(
	viewModel: AddNewSpendingCategoryViewModel = getViewModel { parametersOf(spendingCategory) },
	spendingCategory: SpendingCategory? = null,
) {
	val systemUiController = rememberSystemUiController()
	systemUiController.setNavigationBarColor(Light100)
	systemUiController.navigationBarDarkContentEnabled = true

	when (viewModel.state) {
		AddNewSpendingCategoryState.Error,
		AddNewSpendingCategoryState.SuccessDelete,
		AddNewSpendingCategoryState.SuccessEdit,
		AddNewSpendingCategoryState.Initial,
		AddNewSpendingCategoryState.Loading,
		AddNewSpendingCategoryState.SuccessAdd -> systemUiController.setStatusBarColor(Light100)

		is AddNewSpendingCategoryState.Content -> systemUiController.setStatusBarColor(Violet100)
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewSpendingCategoryState.Initial || viewModel.state == AddNewSpendingCategoryState.Loading,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		LoadingScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state is AddNewSpendingCategoryState.Content,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ContentScreen(
			contentState = viewModel.state as? AddNewSpendingCategoryState.Content ?: return@AnimatedVisibility,
			navigateBack = viewModel::navigateBack,
			updateName = viewModel::updateName,
			updateIconName = viewModel::updateIconName,
			updateColor = viewModel::updateColor,
			addNewSpendingCategory = viewModel::addNewSpendingCategory,
			deleteCategory = viewModel::deleteCategory,
		)
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewSpendingCategoryState.SuccessAdd,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		SuccessAddScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewSpendingCategoryState.SuccessEdit,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		SuccessEditScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewSpendingCategoryState.SuccessDelete,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		SuccessDeleteScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewSpendingCategoryState.Error,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ErrorScreen()
	}

	BackHandler { viewModel.navigateBack() }
}

@Composable
private fun ContentScreen(
	contentState: AddNewSpendingCategoryState.Content,
	navigateBack: () -> Unit,
	updateName: (String) -> Unit,
	updateIconName: (String) -> Unit,
	updateColor: (Int) -> Unit,
	addNewSpendingCategory: () -> Unit,
	deleteCategory: () -> Unit,
) {
	val localFocusManager = LocalFocusManager.current
	Scaffold(
		modifier = Modifier.pointerInput(Unit) {
			detectTapGestures(
				onTap = {
					localFocusManager.clearFocus()
				},
			)
		},
		topBar = {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.background(Violet100)
					.padding(16.dp),
				verticalAlignment = Alignment.CenterVertically,
			) {
				IconButton(
					modifier = Modifier.size(32.dp),
					onClick = { navigateBack() }
				) {
					Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = null, tint = Light100)
				}

				Text(
					modifier = Modifier.weight(1f),
					text = if (contentState.newCategory) {
						"Добавление новой категории расходов"
					} else {
						"Редактирование категории расходов"
					},
					fontFamily = Inter,
					fontWeight = FontWeight.SemiBold,
					fontSize = 18.sp,
					color = Light100,
					textAlign = TextAlign.Center,
				)
				if (contentState.newCategory) {
					Spacer(modifier = Modifier.size(32.dp))
				} else {
					IconButton(
						modifier = Modifier.size(32.dp),
						onClick = { deleteCategory() }
					) {
						Icon(painter = painterResource(R.drawable.ic_trash), contentDescription = null, tint = Light100)
					}
				}
			}
		},
		backgroundColor = Violet100,
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
		) {
			Spacer(modifier = Modifier.weight(1f))

			Text(
				modifier = Modifier.padding(start = 16.dp),
				text = "Название",
				fontFamily = Inter,
				fontWeight = FontWeight.SemiBold,
				fontSize = 18.sp,
				color = Light80Opacity64,
			)

			val fontScale by animateFloatAsState(
				targetValue = Integer.max((64 - (contentState.name.length.takeIf { it > 5 } ?: 0) / 2 * 4), 24).toFloat(),
				animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
			)
			BasicTextField(
				modifier = Modifier.padding(start = 16.dp),
				value = contentState.name,
				onValueChange = { updateName(it) },
				textStyle = TextStyle(
					fontFamily = Inter,
					fontWeight = FontWeight.SemiBold,
					fontSize = fontScale.sp,
					color = Light80,
				),
				singleLine = true,
				cursorBrush = SolidColor(Light80),
			)

			Card(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 8.dp),
				shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
			) {
				Column {
					Row {
						val salaryScale by animateFloatAsState(
							targetValue = if (contentState.selectedIconName == "ic_salary") {
								1.05f
							} else {
								1f
							},
							animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
						)
						Card(
							modifier = Modifier
								.weight(0.33f)
								.aspectRatio(1f)
								.padding(top = 24.dp, start = 16.dp, end = 16.dp)
								.scale(salaryScale)
								.clickable {
									updateIconName("ic_salary")
									updateColor(Green100.toArgb())
								},
							shape = RoundedCornerShape(16.dp),
							elevation = 4.dp,
							backgroundColor = if (contentState.selectedIconName == "ic_salary") {
								Green20
							} else {
								Light100
							},
						) {
							Box(Modifier.fillMaxSize()) {
								Icon(
									modifier = Modifier
										.fillMaxSize()
										.padding(16.dp),
									painter = painterResource(id = R.drawable.ic_salary),
									contentDescription = null,
									tint = if (contentState.selectedIconName == "ic_salary") {
										Green100
									} else {
										Dark50Opacity
									}
								)
							}
						}

						val walletScale by animateFloatAsState(
							targetValue = if (contentState.selectedIconName == "ic_wallet") {
								1.05f
							} else {
								1f
							},
							animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
						)
						Card(
							modifier = Modifier
								.weight(0.33f)
								.aspectRatio(1f)
								.padding(top = 24.dp, start = 16.dp, end = 16.dp)
								.scale(walletScale)
								.clickable {
									updateIconName("ic_wallet")
									updateColor(Blue100.toArgb())
								},
							shape = RoundedCornerShape(16.dp),
							elevation = 4.dp,
							backgroundColor = if (contentState.selectedIconName == "ic_wallet") {
								Blue20
							} else {
								Light100
							},
						) {
							Box(Modifier.fillMaxSize()) {
								Icon(
									modifier = Modifier
										.fillMaxSize()
										.padding(16.dp),
									painter = painterResource(id = R.drawable.ic_wallet),
									contentDescription = null,
									tint = if (contentState.selectedIconName == "ic_wallet") {
										Blue100
									} else {
										Dark50Opacity
									}
								)
							}
						}

						val foodScale by animateFloatAsState(
							targetValue = if (contentState.selectedIconName == "ic_food") {
								1.05f
							} else {
								1f
							},
							animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
						)
						Card(
							modifier = Modifier
								.weight(0.33f)
								.aspectRatio(1f)
								.padding(top = 24.dp, start = 16.dp, end = 16.dp)
								.scale(foodScale)
								.clickable {
									updateIconName("ic_food")
									updateColor(Red100.toArgb())
								},
							shape = RoundedCornerShape(16.dp),
							elevation = 4.dp,
							backgroundColor = if (contentState.selectedIconName == "ic_food") {
								Red20
							} else {
								Light100
							},
						) {
							Box(Modifier.fillMaxSize()) {
								Icon(
									modifier = Modifier
										.fillMaxSize()
										.padding(16.dp),
									painter = painterResource(id = R.drawable.ic_food),
									contentDescription = null,
									tint = if (contentState.selectedIconName == "ic_food") {
										Red100
									} else {
										Dark50Opacity
									}
								)
							}
						}
					}

					Row {
						val billScale by animateFloatAsState(
							targetValue = if (contentState.selectedIconName == "ic_bill") {
								1.05f
							} else {
								1f
							},
							animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
						)
						Card(
							modifier = Modifier
								.weight(0.33f)
								.aspectRatio(1f)
								.padding(top = 24.dp, start = 16.dp, end = 16.dp)
								.scale(billScale)
								.clickable {
									updateIconName("ic_bill")
									updateColor(Violet100.toArgb())
								},
							shape = RoundedCornerShape(16.dp),
							elevation = 4.dp,
							backgroundColor = if (contentState.selectedIconName == "ic_bill") {
								Violet20
							} else {
								Light100
							},
						) {
							Box(Modifier.fillMaxSize()) {
								Icon(
									modifier = Modifier
										.fillMaxSize()
										.padding(16.dp),
									painter = painterResource(id = R.drawable.ic_bill),
									contentDescription = null,
									tint = if (contentState.selectedIconName == "ic_bill") {
										Violet100
									} else {
										Dark50Opacity
									}
								)
							}
						}

						val bagScale by animateFloatAsState(
							targetValue = if (contentState.selectedIconName == "ic_shopping_bag") {
								1.05f
							} else {
								1f
							},
							animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
						)
						Card(
							modifier = Modifier
								.weight(0.33f)
								.aspectRatio(1f)
								.padding(top = 24.dp, start = 16.dp, end = 16.dp)
								.scale(bagScale)
								.clickable {
									updateIconName("ic_shopping_bag")
									updateColor(Yellow100.toArgb())
								},
							shape = RoundedCornerShape(16.dp),
							elevation = 4.dp,
							backgroundColor = if (contentState.selectedIconName == "ic_shopping_bag") {
								Yellow20
							} else {
								Light100
							},
						) {
							Box(Modifier.fillMaxSize()) {
								Icon(
									modifier = Modifier
										.fillMaxSize()
										.padding(16.dp),
									painter = painterResource(id = R.drawable.ic_shopping_bag),
									contentDescription = null,
									tint = if (contentState.selectedIconName == "ic_shopping_bag") {
										Yellow100
									} else {
										Dark50Opacity
									}
								)
							}
						}

						val carScale by animateFloatAsState(
							targetValue = if (contentState.selectedIconName == "ic_car") {
								1.05f
							} else {
								1f
							},
							animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
						)
						Card(
							modifier = Modifier
								.weight(0.33f)
								.aspectRatio(1f)
								.padding(top = 24.dp, start = 16.dp, end = 16.dp)
								.scale(carScale)
								.clickable {
									updateIconName("ic_car")
									updateColor(Blue40.toArgb())
								},
							shape = RoundedCornerShape(16.dp),
							elevation = 4.dp,
							backgroundColor = if (contentState.selectedIconName == "ic_car") {
								Blue20
							} else {
								Light100
							},
						) {
							Box(Modifier.fillMaxSize()) {
								Icon(
									modifier = Modifier
										.fillMaxSize()
										.padding(16.dp),
									painter = painterResource(id = R.drawable.ic_car),
									contentDescription = null,
									tint = if (contentState.selectedIconName == "ic_car") {
										Blue40
									} else {
										Dark50Opacity
									}
								)
							}
						}
					}

					Row {
						val homeScale by animateFloatAsState(
							targetValue = if (contentState.selectedIconName == "ic_home") {
								1.05f
							} else {
								1f
							},
							animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
						)
						Card(
							modifier = Modifier
								.weight(0.33f)
								.aspectRatio(1f)
								.padding(top = 24.dp, start = 16.dp, end = 16.dp)
								.scale(homeScale)
								.clickable {
									updateIconName("ic_home")
									updateColor(Red40.toArgb())
								},
							shape = RoundedCornerShape(16.dp),
							elevation = 4.dp,
							backgroundColor = if (contentState.selectedIconName == "ic_home") {
								Red20
							} else {
								Light100
							},
						) {
							Box(Modifier.fillMaxSize()) {
								Icon(
									modifier = Modifier
										.fillMaxSize()
										.padding(16.dp),
									painter = painterResource(id = R.drawable.ic_home),
									contentDescription = null,
									tint = if (contentState.selectedIconName == "ic_home") {
										Red40
									} else {
										Dark50Opacity
									}
								)
							}
						}

						val userScale by animateFloatAsState(
							targetValue = if (contentState.selectedIconName == "ic_user") {
								1.05f
							} else {
								1f
							},
							animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
						)
						Card(
							modifier = Modifier
								.weight(0.33f)
								.aspectRatio(1f)
								.padding(top = 24.dp, start = 16.dp, end = 16.dp)
								.scale(userScale)
								.clickable {
									updateIconName("ic_user")
									updateColor(Green40.toArgb())
								},
							shape = RoundedCornerShape(16.dp),
							elevation = 4.dp,
							backgroundColor = if (contentState.selectedIconName == "ic_user") {
								Green20
							} else {
								Light100
							},
						) {
							Box(Modifier.fillMaxSize()) {
								Icon(
									modifier = Modifier
										.fillMaxSize()
										.padding(16.dp),
									painter = painterResource(id = R.drawable.ic_user),
									contentDescription = null,
									tint = if (contentState.selectedIconName == "ic_user") {
										Green40
									} else {
										Dark50Opacity
									}
								)
							}
						}

						val trashScale by animateFloatAsState(
							targetValue = if (contentState.selectedIconName == "ic_trash") {
								1.05f
							} else {
								1f
							},
							animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
						)
						Card(
							modifier = Modifier
								.weight(0.33f)
								.aspectRatio(1f)
								.padding(top = 24.dp, start = 16.dp, end = 16.dp)
								.scale(trashScale)
								.clickable {
									updateIconName("ic_trash")
									updateColor(Dark100.toArgb())
								},
							shape = RoundedCornerShape(16.dp),
							elevation = 4.dp,
							backgroundColor = if (contentState.selectedIconName == "ic_trash") {
								Dark25
							} else {
								Light100
							},
						) {
							Box(Modifier.fillMaxSize()) {
								Icon(
									modifier = Modifier
										.fillMaxSize()
										.padding(16.dp),
									painter = painterResource(id = R.drawable.ic_trash),
									contentDescription = null,
									tint = if (contentState.selectedIconName == "ic_trash") {
										Dark100
									} else {
										Dark50Opacity
									}
								)
							}
						}
					}

					Button(
						modifier = Modifier
							.padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
							.fillMaxWidth(),
						onClick = { addNewSpendingCategory() },
						contentPadding = PaddingValues(16.dp),
						shape = RoundedCornerShape(16.dp),
					) {
						Text(
							text = if (contentState.newCategory) {
								"Добавить"
							} else {
								"Изменить"
							},
							fontFamily = Inter,
							fontWeight = FontWeight.SemiBold,
							fontSize = 18.sp,
							color = Light80,
						)
					}
				}
			}
		}
	}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun LoadingScreen() {
	Box(modifier = Modifier.fillMaxSize()) {
		CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
	}

	val keyboardController = LocalSoftwareKeyboardController.current
	keyboardController?.hide()
}

@Composable
private fun SuccessAddScreen() {

	Box(modifier = Modifier.fillMaxSize()) {
		Column(
			modifier = Modifier.align(Alignment.Center),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Icon(
				painter = painterResource(R.drawable.ic_success),
				contentDescription = null,
				tint = Green100,
			)
			Text(
				modifier = Modifier.padding(16.dp),
				text = "Категория расходов добавлена!",
				fontFamily = Inter,
				fontWeight = FontWeight.Medium,
				fontSize = 24.sp,
				color = Dark50,
				textAlign = TextAlign.Center,
			)
		}
	}
}

@Composable
private fun SuccessEditScreen() {

	Box(modifier = Modifier.fillMaxSize()) {
		Column(
			modifier = Modifier.align(Alignment.Center),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Icon(
				painter = painterResource(R.drawable.ic_success),
				contentDescription = null,
				tint = Green100,
			)
			Text(
				modifier = Modifier.padding(16.dp),
				text = "Категория расходов изменена!",
				fontFamily = Inter,
				fontWeight = FontWeight.Medium,
				fontSize = 24.sp,
				color = Dark50,
				textAlign = TextAlign.Center,
			)
		}
	}
}

@Composable
private fun SuccessDeleteScreen() {

	Box(modifier = Modifier.fillMaxSize()) {
		Column(
			modifier = Modifier.align(Alignment.Center),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Icon(
				painter = painterResource(R.drawable.ic_success),
				contentDescription = null,
				tint = Green100,
			)
			Text(
				modifier = Modifier.padding(16.dp),
				text = "Категория расходов удалена!",
				fontFamily = Inter,
				fontWeight = FontWeight.Medium,
				fontSize = 24.sp,
				color = Dark50,
				textAlign = TextAlign.Center,
			)
		}
	}
}

@Composable
private fun ErrorScreen() {

	Box(modifier = Modifier.fillMaxSize()) {
		Column(
			modifier = Modifier.align(Alignment.Center),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Icon(
				modifier = Modifier.size(96.dp),
				painter = painterResource(R.drawable.ic_error),
				contentDescription = null,
				tint = Red100,
			)
			Text(
				modifier = Modifier.padding(16.dp),
				text = "Произошла ошибка!",
				fontFamily = Inter,
				fontWeight = FontWeight.Medium,
				fontSize = 24.sp,
				color = Dark50,
				textAlign = TextAlign.Center,
			)
		}
	}
}