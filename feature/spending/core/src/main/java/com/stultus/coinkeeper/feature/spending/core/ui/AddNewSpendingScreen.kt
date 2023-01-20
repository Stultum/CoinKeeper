package com.stultus.coinkeeper.feature.spending.core.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.stultus.coinkeeper.component.design.resources.theme.Blue100
import com.stultus.coinkeeper.component.design.resources.theme.Blue20
import com.stultus.coinkeeper.component.design.resources.theme.Blue40
import com.stultus.coinkeeper.component.design.resources.theme.Dark25
import com.stultus.coinkeeper.component.design.resources.theme.Dark50
import com.stultus.coinkeeper.component.design.resources.theme.Green100
import com.stultus.coinkeeper.component.design.resources.theme.Green20
import com.stultus.coinkeeper.component.design.resources.theme.Green40
import com.stultus.coinkeeper.component.design.resources.theme.Inter
import com.stultus.coinkeeper.component.design.resources.theme.Light100
import com.stultus.coinkeeper.component.design.resources.theme.Light20
import com.stultus.coinkeeper.component.design.resources.theme.Light80
import com.stultus.coinkeeper.component.design.resources.theme.Light80Opacity64
import com.stultus.coinkeeper.component.design.resources.theme.Red100
import com.stultus.coinkeeper.component.design.resources.theme.Red20
import com.stultus.coinkeeper.component.design.resources.theme.Red40
import com.stultus.coinkeeper.component.design.resources.theme.Violet100
import com.stultus.coinkeeper.component.design.resources.theme.Violet20
import com.stultus.coinkeeper.component.design.resources.theme.Violet80
import com.stultus.coinkeeper.component.design.resources.theme.Yellow100
import com.stultus.coinkeeper.component.design.resources.theme.Yellow20
import com.stultus.coinkeeper.feature.spending.core.R
import com.stultus.coinkeeper.feature.spending.core.presentation.AddNewSpendingState
import com.stultus.coinkeeper.feature.spending.core.presentation.AddNewSpendingViewModel
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AddNewSpendingScreen(
	viewModel: AddNewSpendingViewModel = getViewModel { parametersOf(spending) },
	spending: Spending? = null,
) {
	val systemUiController = rememberSystemUiController()
	systemUiController.setNavigationBarColor(Light100)
	systemUiController.navigationBarDarkContentEnabled = true

	when (viewModel.state) {
		AddNewSpendingState.SuccessDelete,
		AddNewSpendingState.SuccessEdit,
		AddNewSpendingState.Error,
		AddNewSpendingState.Initial,
		AddNewSpendingState.Loading,
		AddNewSpendingState.NoAccounts,
		AddNewSpendingState.NoSpendingCategories,
		AddNewSpendingState.SuccessAdd -> systemUiController.setStatusBarColor(Light100)

		is AddNewSpendingState.Content -> systemUiController.setStatusBarColor(Red100)
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewSpendingState.Initial || viewModel.state == AddNewSpendingState.Loading,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		LoadingScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state is AddNewSpendingState.Content,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ContentScreen(
			contentState = viewModel.state as? AddNewSpendingState.Content ?: return@AnimatedVisibility,
			navigateBack = viewModel::navigateBack,
			updateAmount = viewModel::updateAmount,
			updateSelectedAccount = viewModel::updateSelectedAccount,
			updateSelectedCategory = viewModel::updateSelectedCategory,
			updateComment = viewModel::updateComment,
			addSpending = viewModel::addNewSpending,
			deleteSpending = viewModel::deleteSpending,
		)
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewSpendingState.SuccessAdd,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		SuccessAddScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewSpendingState.SuccessEdit,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		SuccessEditScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewSpendingState.SuccessDelete,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		SuccessDeleteScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewSpendingState.NoAccounts,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		NoAccountsScreen(
			navigateToAddAccount = viewModel::navigateToAddNewAccount,
		)
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewSpendingState.NoSpendingCategories,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		NoSpendingCategoriesScreen(
			navigateToAddCategory = viewModel::navigateToAddNewSpendingCategory,
		)
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewSpendingState.Error,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ErrorScreen()
	}

	BackHandler { viewModel.navigateBack() }
}

@Composable
private fun ContentScreen(
	contentState: AddNewSpendingState.Content,
	navigateBack: () -> Unit,
	updateAmount: (String) -> Unit,
	updateSelectedAccount: (Account) -> Unit,
	updateSelectedCategory: (SpendingCategory) -> Unit,
	updateComment: (String) -> Unit,
	addSpending: () -> Unit,
	deleteSpending: () -> Unit,
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
					.background(Red100)
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
					text = if (contentState.newSpending) {
						"Добавление расхода"
					} else {
						"Редактирование расхода"
					},
					fontFamily = Inter,
					fontWeight = FontWeight.SemiBold,
					fontSize = 18.sp,
					color = Light100,
					textAlign = TextAlign.Center,
				)

				if (contentState.newSpending) {
					Spacer(modifier = Modifier.size(32.dp))
				} else {
					IconButton(
						modifier = Modifier.size(32.dp),
						onClick = { deleteSpending() }
					) {
						Icon(painter = painterResource(R.drawable.ic_trash), contentDescription = null, tint = Light100)
					}
				}
			}
		},
		backgroundColor = Red100,
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
		) {
			Spacer(modifier = Modifier.weight(1f))

			Text(
				modifier = Modifier.padding(start = 16.dp),
				text = "Сумма",
				fontFamily = Inter,
				fontWeight = FontWeight.SemiBold,
				fontSize = 18.sp,
				color = Light80Opacity64,
			)

			val fontScale by animateFloatAsState(
				targetValue = Integer.max((64 - (contentState.amount.length.takeIf { it > 5 } ?: 0) / 2 * 4), 24).toFloat(),
				animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
			)
			BasicTextField(
				modifier = Modifier.padding(start = 16.dp),
				value = "₽${contentState.amount}",
				onValueChange = {
					updateAmount(
						it.filter { it.isDigit() || it == '.' }.toMutableList().apply {
							while (count { it == '.' } > 1) {
								remove('.')
							}
						}.joinToString(separator = "")
					)
				},
				textStyle = TextStyle(
					fontFamily = Inter,
					fontWeight = FontWeight.SemiBold,
					fontSize = fontScale.sp,
					color = Light80,
				),
				singleLine = true,
				cursorBrush = SolidColor(Light80),
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
			)

			Card(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 8.dp),
				shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
			) {
				Column {
					var showCategoryMenu by remember { mutableStateOf(false) }
					val categoryIcon = if (showCategoryMenu) {
						Icons.Filled.KeyboardArrowUp
					} else {
						Icons.Filled.KeyboardArrowDown
					}
					var categoryTextFieldSize by remember { mutableStateOf(Size.Zero) }
					Column(Modifier.padding(horizontal = 16.dp)) {
						OutlinedTextField(
							modifier = Modifier
								.focusable(false)
								.fillMaxWidth()
								.padding(top = 24.dp)
								.onGloballyPositioned { coordinates ->
									categoryTextFieldSize = coordinates.size.toSize()
								},
							value = contentState.selectedCategory.name,
							onValueChange = { },
							shape = RoundedCornerShape(16.dp),
							colors = TextFieldDefaults.outlinedTextFieldColors(
								unfocusedBorderColor = Light20,
								focusedBorderColor = Violet100,
							),
							singleLine = true,
							label = { Text(text = "Категория расхода") },
							readOnly = true,
							trailingIcon = {
								Icon(
									modifier = Modifier.clickable { showCategoryMenu = !showCategoryMenu },
									imageVector = categoryIcon,
									contentDescription = null
								)
							}
						)

						DropdownMenu(
							modifier = Modifier
								.width(with(LocalDensity.current) { categoryTextFieldSize.width.toDp() }),
							expanded = showCategoryMenu,
							onDismissRequest = { showCategoryMenu = false }) {
							contentState.categoryList.forEach { category ->
								DropdownMenuItem(
									modifier = Modifier.padding(4.dp),
									onClick = {
										updateSelectedCategory(category)
										showCategoryMenu = false
									}) {
									Row {
										Card(
											modifier = Modifier
												.weight(0.15f)
												.aspectRatio(1f),
											shape = RoundedCornerShape(16.dp),
											backgroundColor = getBackgroundColor(category.color),
										) {
											Icon(
												modifier = Modifier
													.fillMaxSize()
													.padding(8.dp),
												painter = painterResource(id = getIconRes(name = category.iconName)),
												contentDescription = null,
												tint = Color(category.color),
											)
										}
										Text(
											modifier = Modifier
												.align(Alignment.CenterVertically)
												.weight(0.85f),
											fontFamily = Inter,
											fontWeight = FontWeight.SemiBold,
											fontSize = 20.sp,
											text = category.name,
											textAlign = TextAlign.Center,
											color = Color(category.color),
										)
									}
								}
							}
						}
					}

					OutlinedTextField(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 24.dp, start = 16.dp, end = 16.dp),
						value = contentState.comment ?: "",
						onValueChange = { updateComment(it) },
						shape = RoundedCornerShape(16.dp),
						colors = TextFieldDefaults.outlinedTextFieldColors(
							unfocusedBorderColor = Light20,
							focusedBorderColor = Violet100,
						),
						singleLine = true,
						label = { Text(text = "Описание") },
					)

					var showAccountMenu by remember { mutableStateOf(false) }
					val accountIcon = if (showAccountMenu) {
						Icons.Filled.KeyboardArrowUp
					} else {
						Icons.Filled.KeyboardArrowDown
					}
					var accountTextFieldSize by remember { mutableStateOf(Size.Zero) }
					Column(Modifier.padding(horizontal = 16.dp)) {
						OutlinedTextField(
							modifier = Modifier
								.focusable(false)
								.fillMaxWidth()
								.padding(top = 24.dp)
								.onGloballyPositioned { coordinates ->
									accountTextFieldSize = coordinates.size.toSize()
								},
							value = contentState.selectedAccount.name,
							onValueChange = { },
							shape = RoundedCornerShape(16.dp),
							colors = TextFieldDefaults.outlinedTextFieldColors(
								unfocusedBorderColor = Light20,
								focusedBorderColor = Violet100,
							),
							singleLine = true,
							label = { Text(text = "Счет") },
							readOnly = true,
							trailingIcon = {
								Icon(
									modifier = Modifier.clickable { showAccountMenu = !showAccountMenu },
									imageVector = accountIcon,
									contentDescription = null
								)
							}
						)

						DropdownMenu(
							modifier = Modifier
								.width(with(LocalDensity.current) { accountTextFieldSize.width.toDp() }),
							expanded = showAccountMenu,
							onDismissRequest = { showAccountMenu = false }) {
							contentState.accountList.forEach { account ->
								DropdownMenuItem(
									modifier = Modifier.padding(4.dp),
									onClick = {
										updateSelectedAccount(account)
										showAccountMenu = false
									}) {
									Row {
										Card(
											modifier = Modifier
												.weight(0.15f)
												.aspectRatio(1f),
											shape = RoundedCornerShape(16.dp),
										) {
											Image(
												modifier = Modifier
													.fillMaxSize(),
												contentScale = ContentScale.FillHeight,
												painter = painterResource(id = getIconRes(name = account.iconName)),
												contentDescription = null,
											)
										}
										Text(
											modifier = Modifier
												.align(Alignment.CenterVertically)
												.weight(0.85f),
											fontFamily = Inter,
											fontWeight = FontWeight.SemiBold,
											fontSize = 20.sp,
											text = account.name,
											textAlign = TextAlign.Center,
											color = Violet80,
										)
									}
								}
							}
						}
					}

					Button(
						modifier = Modifier
							.padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
							.fillMaxWidth(),
						onClick = { addSpending() },
						contentPadding = PaddingValues(16.dp),
						shape = RoundedCornerShape(16.dp),
					) {
						Text(
							text = if (contentState.newSpending) {
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
				text = "Расход добавлен!",
				fontFamily = Inter,
				fontWeight = FontWeight.Medium,
				fontSize = 24.sp,
				color = Dark50,
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
				text = "Расход изменен!",
				fontFamily = Inter,
				fontWeight = FontWeight.Medium,
				fontSize = 24.sp,
				color = Dark50,
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
				text = "Расход удален!",
				fontFamily = Inter,
				fontWeight = FontWeight.Medium,
				fontSize = 24.sp,
				color = Dark50,
			)
		}
	}
}

@Composable
private fun NoAccountsScreen(
	navigateToAddAccount: () -> Unit,
) {

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
				text = "У вас пока нету ни одного счета, добавьте его!",
				fontFamily = Inter,
				fontWeight = FontWeight.Medium,
				fontSize = 24.sp,
				color = Dark50,
				textAlign = TextAlign.Center,
			)

			Button(
				modifier = Modifier
					.padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
					.fillMaxWidth(),
				onClick = { navigateToAddAccount() },
				contentPadding = PaddingValues(16.dp),
				shape = RoundedCornerShape(16.dp),
			) {
				Text(
					text = "Добавить",
					fontFamily = Inter,
					fontWeight = FontWeight.SemiBold,
					fontSize = 18.sp,
					color = Light80,
				)
			}
		}
	}
}

@Composable
private fun NoSpendingCategoriesScreen(
	navigateToAddCategory: () -> Unit,
) {

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
				text = "У вас пока нету ни одной категории расходов, добавьте ее!",
				fontFamily = Inter,
				fontWeight = FontWeight.Medium,
				fontSize = 24.sp,
				color = Dark50,
				textAlign = TextAlign.Center,
			)

			Button(
				modifier = Modifier
					.padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
					.fillMaxWidth(),
				onClick = { navigateToAddCategory() },
				contentPadding = PaddingValues(16.dp),
				shape = RoundedCornerShape(16.dp),
			) {
				Text(
					text = "Добавить",
					fontFamily = Inter,
					fontWeight = FontWeight.SemiBold,
					fontSize = 18.sp,
					color = Light80,
				)
			}
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

@Composable
fun getIconRes(name: String): Int =
	LocalContext.current.resources.getIdentifier(name, "drawable", LocalContext.current.packageName)

private fun getBackgroundColor(color: Int): Color =
	when (Color(color)) {
		Red100,
		Red40     -> Red20

		Green100,
		Green40   -> Green20

		Blue100,
		Blue40    -> Blue20

		Violet100 -> Violet20

		Yellow100 -> Yellow20

		Light100  -> Light20

		else      -> Dark25
	}