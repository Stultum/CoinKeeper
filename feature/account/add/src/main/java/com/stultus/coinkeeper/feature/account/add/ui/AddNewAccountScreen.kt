package com.stultus.coinkeeper.feature.account.add.ui

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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.stultus.coinkeeper.component.design.resources.theme.Dark50
import com.stultus.coinkeeper.component.design.resources.theme.Dark50Opacity
import com.stultus.coinkeeper.component.design.resources.theme.Green100
import com.stultus.coinkeeper.component.design.resources.theme.Inter
import com.stultus.coinkeeper.component.design.resources.theme.Light100
import com.stultus.coinkeeper.component.design.resources.theme.Light20
import com.stultus.coinkeeper.component.design.resources.theme.Light80
import com.stultus.coinkeeper.component.design.resources.theme.Light80Opacity64
import com.stultus.coinkeeper.component.design.resources.theme.Red100
import com.stultus.coinkeeper.component.design.resources.theme.Violet100
import com.stultus.coinkeeper.component.design.resources.theme.Violet40Opa
import com.stultus.coinkeeper.feature.account.add.R
import com.stultus.coinkeeper.feature.account.add.presentation.AddNewAccountState
import com.stultus.coinkeeper.feature.account.add.presentation.AddNewAccountViewModel
import com.stultus.coinkeeper.shared.account.src.main.domain.entity.Account
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.lang.Integer.max

@Composable
fun AddNewAccountScreen(viewModel: AddNewAccountViewModel = getViewModel { parametersOf(account) }, account: Account? = null) {
	val systemUiController = rememberSystemUiController()
	systemUiController.setNavigationBarColor(Light100)
	systemUiController.navigationBarDarkContentEnabled = true

	when (viewModel.state) {
		AddNewAccountState.Error,
		AddNewAccountState.Initial,
		AddNewAccountState.SuccessDelete,
		AddNewAccountState.SuccessEdit,
		AddNewAccountState.Loading,
		AddNewAccountState.SuccessAdd -> systemUiController.setStatusBarColor(Light100)

		is AddNewAccountState.Content -> systemUiController.setStatusBarColor(Violet100)
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewAccountState.Initial || viewModel.state == AddNewAccountState.Loading,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		LoadingScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state is AddNewAccountState.Content,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ContentScreen(
			contentState = viewModel.state as? AddNewAccountState.Content ?: return@AnimatedVisibility,
			navigateBack = viewModel::navigateBack,
			updateBalance = viewModel::updateBalance,
			updateName = viewModel::updateName,
			updateImageName = viewModel::updateImageName,
			addAccount = viewModel::addNewAccount,
			deleteAccount = viewModel::deleteAccount,
		)
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewAccountState.SuccessAdd,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		SuccessAddScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewAccountState.SuccessEdit,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		SuccessEditScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewAccountState.SuccessDelete,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		SuccessDeleteScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state == AddNewAccountState.Error,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ErrorScreen()
	}

	BackHandler { viewModel.navigateBack() }
}

@Composable
private fun ContentScreen(
	contentState: AddNewAccountState.Content,
	navigateBack: () -> Unit,
	updateBalance: (String) -> Unit,
	updateName: (String) -> Unit,
	updateImageName: (String) -> Unit,
	addAccount: () -> Unit,
	deleteAccount: () -> Unit,
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
					text = if (contentState.newAccount) {
						"Добавление нового счета"
					} else {
						"Редактирование счета"
					},
					fontFamily = Inter,
					fontWeight = FontWeight.SemiBold,
					fontSize = 18.sp,
					color = Light100,
					textAlign = TextAlign.Center,
				)
				if (contentState.newAccount) {
					Spacer(modifier = Modifier.size(32.dp))
				} else {
					IconButton(
						modifier = Modifier.size(32.dp),
						onClick = { deleteAccount() }
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
				text = "Баланс",
				fontFamily = Inter,
				fontWeight = FontWeight.SemiBold,
				fontSize = 18.sp,
				color = Light80Opacity64,
			)

			val fontScale by animateFloatAsState(
				targetValue = max((64 - (contentState.amount.length.takeIf { it > 5 } ?: 0) / 2 * 4), 24).toFloat(),
				animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
			)
			BasicTextField(
				modifier = Modifier.padding(start = 16.dp),
				value = "₽${contentState.amount}",
				onValueChange = {
					updateBalance(
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
						value = contentState.name,
						onValueChange = { updateName(it) },
						shape = RoundedCornerShape(16.dp),
						colors = TextFieldDefaults.outlinedTextFieldColors(
							unfocusedBorderColor = Light20,
							focusedBorderColor = Violet100,
						),
						singleLine = true,
						label = { Text(text = "Название") }
					)

					val cardScale by animateFloatAsState(
						targetValue = if (contentState.iconName == "amogus") {
							1.05f
						} else {
							1f
						},
						animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
					)
					Card(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 24.dp, start = 16.dp, end = 16.dp)
							.height(80.dp)
							.scale(cardScale)
							.clickable { updateImageName("amogus") },
						shape = RoundedCornerShape(16.dp),
					) {
						Box(Modifier.fillMaxSize()) {
							Image(
								modifier = Modifier.fillMaxSize(),
								painter = painterResource(id = R.drawable.amogus),
								contentDescription = null,
								contentScale = ContentScale.Crop,
								colorFilter = if (contentState.iconName == "amogus") {
									ColorFilter.tint(Violet40Opa, BlendMode.SrcAtop)
								} else {
									ColorFilter.tint(Dark50Opacity, BlendMode.SrcAtop)
								}
							)

							Text(
								modifier = Modifier.align(Alignment.Center),
								text = "Карта",
								fontFamily = Inter,
								fontWeight = FontWeight.SemiBold,
								fontSize = 20.sp,
								color = if (contentState.iconName == "amogus") {
									Violet100
								} else {
									Dark50
								},
							)
						}
					}

					val cashScale by animateFloatAsState(
						targetValue = if (contentState.iconName == "amogus") {
							1f
						} else {
							1.05f
						},
						animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
					)
					Card(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 16.dp, start = 16.dp, end = 16.dp)
							.height(80.dp)
							.scale(cashScale)
							.clickable { updateImageName("adoptus") },
						shape = RoundedCornerShape(16.dp),
					) {
						Box(Modifier.fillMaxSize()) {
							Image(
								modifier = Modifier.fillMaxSize(),
								painter = painterResource(id = R.drawable.adoptus),
								contentDescription = null,
								contentScale = ContentScale.Crop,
								colorFilter = if (contentState.iconName == "amogus") {
									ColorFilter.tint(Dark50Opacity, BlendMode.SrcAtop)
								} else {
									ColorFilter.tint(Violet40Opa, BlendMode.SrcAtop)
								},
							)

							Text(
								modifier = Modifier.align(Alignment.Center),
								text = "Наличные",
								fontFamily = Inter,
								fontWeight = FontWeight.SemiBold,
								fontSize = 20.sp,
								color = if (contentState.iconName == "amogus") {
									Dark50
								} else {
									Violet100
								},
							)
						}
					}

					Button(
						modifier = Modifier
							.padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
							.fillMaxWidth(),
						onClick = { addAccount() },
						contentPadding = PaddingValues(16.dp),
						shape = RoundedCornerShape(16.dp),
					) {
						Text(
							text = if (contentState.newAccount) {
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
				text = "Счет добавлен!",
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
				text = "Счет изменен!",
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
				text = "Счет удален!",
				fontFamily = Inter,
				fontWeight = FontWeight.Medium,
				fontSize = 24.sp,
				color = Dark50,
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