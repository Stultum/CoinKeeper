package com.stultus.coinkeeper.feature.income.add.ui

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
import com.stultus.coinkeeper.component.design.resources.theme.Dark50
import com.stultus.coinkeeper.component.design.resources.theme.Green100
import com.stultus.coinkeeper.component.design.resources.theme.Inter
import com.stultus.coinkeeper.component.design.resources.theme.Light100
import com.stultus.coinkeeper.component.design.resources.theme.Light20
import com.stultus.coinkeeper.component.design.resources.theme.Light80
import com.stultus.coinkeeper.component.design.resources.theme.Light80Opacity64
import com.stultus.coinkeeper.component.design.resources.theme.Red100
import com.stultus.coinkeeper.component.design.resources.theme.Violet100
import com.stultus.coinkeeper.component.design.resources.theme.Violet80
import com.stultus.coinkeeper.feature.income.add.R
import com.stultus.coinkeeper.feature.income.add.presentation.AddNewIncomeState
import com.stultus.coinkeeper.feature.income.add.presentation.AddNewIncomeViewModel
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AddNewIncomeScreen(
	viewModel: AddNewIncomeViewModel = getViewModel { parametersOf(income) },
	income: Income? = null,
) {
	val systemUiController = rememberSystemUiController()
	systemUiController.setNavigationBarColor(Light100)
	systemUiController.navigationBarDarkContentEnabled = true

	when (viewModel.state) {
		AddNewIncomeState.SuccessDelete,
		AddNewIncomeState.SuccessEdit,
		AddNewIncomeState.Error,
		AddNewIncomeState.InitialD,
		AddNewIncomeState.Loading,
		AddNewIncomeState.NoAccounts,
		AddNewIncomeState.SuccessAdd -> systemUiController.setStatusBarColor(Light100)

		is AddNewIncomeState.Content -> systemUiController.setStatusBarColor(Green100)
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewIncomeState.InitialD || viewModel.state == AddNewIncomeState.Loading,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		LoadingScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state is AddNewIncomeState.Content,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ContentScreen(
			contentState = viewModel.state as? AddNewIncomeState.Content ?: return@AnimatedVisibility,
			navigateBack = viewModel::navigateBack,
			updateAmount = viewModel::updateAmount,
			updateSelectedAccount = viewModel::updateSelectedAccount,
			updateComment = viewModel::updateComment,
			addIncome = viewModel::addNewIncome,
			deleteIncome = viewModel::deleteIncome,
		)
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewIncomeState.SuccessAdd,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		SuccessAddScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewIncomeState.SuccessEdit,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		SuccessEditScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewIncomeState.SuccessDelete,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		SuccessDeleteScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewIncomeState.NoAccounts,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		NoAccountsScreen(
			navigateToAddAccount = viewModel::navigateToAddNewAccount,
		)
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewIncomeState.Error,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ErrorScreen()
	}

	BackHandler { viewModel.navigateBack() }
}

@Composable
private fun ContentScreen(
	contentState: AddNewIncomeState.Content,
	navigateBack: () -> Unit,
	updateAmount: (String) -> Unit,
	updateSelectedAccount: (Account) -> Unit,
	updateComment: (String) -> Unit,
	addIncome: () -> Unit,
	deleteIncome: () -> Unit,
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
					.background(Green100)
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
					text = if (contentState.newIncome) {
						"Добавление дохода"
					} else {
						"Редактирование дохода"
					},
					fontFamily = Inter,
					fontWeight = FontWeight.SemiBold,
					fontSize = 18.sp,
					color = Light100,
					textAlign = TextAlign.Center,
				)

				if (contentState.newIncome) {
					Spacer(modifier = Modifier.size(32.dp))
				} else {
					IconButton(
						modifier = Modifier.size(32.dp),
						onClick = { deleteIncome() }
					) {
						Icon(painter = painterResource(R.drawable.ic_trash), contentDescription = null, tint = Light100)
					}
				}
			}
		},
		backgroundColor = Green100,
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

					var showMenu by remember { mutableStateOf(false) }
					val icon = if (showMenu) {
						Icons.Filled.KeyboardArrowUp
					} else {
						Icons.Filled.KeyboardArrowDown
					}
					var textFieldSize by remember { mutableStateOf(Size.Zero) }
					Column(Modifier.padding(horizontal = 16.dp)) {
						OutlinedTextField(
							modifier = Modifier
								.focusable(false)
								.fillMaxWidth()
								.padding(top = 24.dp)
								.onGloballyPositioned { coordinates ->
									textFieldSize = coordinates.size.toSize()
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
									modifier = Modifier.clickable { showMenu = !showMenu },
									imageVector = icon,
									contentDescription = null
								)
							}
						)

						DropdownMenu(
							modifier = Modifier
								.width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
							expanded = showMenu,
							onDismissRequest = { showMenu = false }) {
							contentState.accountList.forEach { account ->
								DropdownMenuItem(
									modifier = Modifier.padding(4.dp),
									onClick = {
										updateSelectedAccount(account)
										showMenu = false
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
						onClick = { addIncome() },
						contentPadding = PaddingValues(16.dp),
						shape = RoundedCornerShape(16.dp),
					) {
						Text(
							text = if (contentState.newIncome) {
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
				text = "Доход добавлен!",
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
				text = "Доход изменен!",
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
				text = "Доход удален!",
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