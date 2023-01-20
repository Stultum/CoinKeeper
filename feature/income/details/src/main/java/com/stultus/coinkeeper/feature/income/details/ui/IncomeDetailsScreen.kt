package com.stultus.coinkeeper.feature.income.details.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.stultus.coinkeeper.component.design.resources.theme.Dark100
import com.stultus.coinkeeper.component.design.resources.theme.Dark50
import com.stultus.coinkeeper.component.design.resources.theme.Green100
import com.stultus.coinkeeper.component.design.resources.theme.Inter
import com.stultus.coinkeeper.component.design.resources.theme.Light100
import com.stultus.coinkeeper.component.design.resources.theme.Light80
import com.stultus.coinkeeper.component.design.resources.theme.LightBorder
import com.stultus.coinkeeper.component.design.resources.theme.LightForText
import com.stultus.coinkeeper.component.design.resources.theme.Red100
import com.stultus.coinkeeper.feature.income.details.R
import com.stultus.coinkeeper.feature.income.details.presentation.IncomeDetailsState
import com.stultus.coinkeeper.feature.income.details.presentation.IncomeDetailsViewModel
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IncomeDetailsScreen(viewModel: IncomeDetailsViewModel = getViewModel { parametersOf(income) }, income: Income) {
	val systemUiController = rememberSystemUiController()
	systemUiController.setNavigationBarColor(Light100)
	systemUiController.navigationBarDarkContentEnabled = true

	when (viewModel.state) {
		IncomeDetailsState.Error,
		IncomeDetailsState.Initial,
		IncomeDetailsState.Loading,
		IncomeDetailsState.Success -> systemUiController.setStatusBarColor(Light100)

		is IncomeDetailsState.Content -> systemUiController.setStatusBarColor(Green100)
	}

	AnimatedVisibility(
		visible = viewModel.state == IncomeDetailsState.Initial || viewModel.state == IncomeDetailsState.Loading,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		LoadingScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state is IncomeDetailsState.Content,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ContentScreen(
			contentState = viewModel.state as? IncomeDetailsState.Content ?: return@AnimatedVisibility,
			navigateBack = viewModel::navigateBack,
			deleteIncome = viewModel::deleteIncome,
			openEditIncome = viewModel::navigateToEditIncome,
		)
	}

	AnimatedVisibility(
		visible = viewModel.state == IncomeDetailsState.Success,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		SuccessScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state == IncomeDetailsState.Error,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ErrorScreen()
	}

	BackHandler { viewModel.navigateBack() }
}

@Composable
private fun ContentScreen(
	contentState: IncomeDetailsState.Content,
	navigateBack: () -> Unit,
	deleteIncome: () -> Unit,
	openEditIncome: () -> Unit,
) {
	Scaffold(
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
					text = "Детали транзакции",
					fontFamily = Inter,
					fontWeight = FontWeight.SemiBold,
					fontSize = 18.sp,
					color = Light100,
					textAlign = TextAlign.Center,
				)

				IconButton(
					modifier = Modifier.size(32.dp),
					onClick = { deleteIncome() }
				) {
					Icon(painter = painterResource(R.drawable.ic_trash), contentDescription = null, tint = Light100)
				}
			}
		},
		backgroundColor = Light100,
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
		) {
			Box {
				Card(
					modifier = Modifier
						.fillMaxWidth(),
					shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
					backgroundColor = Green100,
					elevation = 0.dp,
				) {
					Column {
						Text(
							modifier = Modifier.fillMaxWidth(),
							text = "${contentState.income.amount}₽",
							fontFamily = Inter,
							fontWeight = FontWeight.Bold,
							fontSize = 48.sp,
							color = Light80,
							textAlign = TextAlign.Center,
						)

						Text(
							modifier = Modifier
								.fillMaxWidth()
								.padding(top = 8.dp, bottom = 50.dp),
							text = getFormattedDate(contentState.income.date),
							fontFamily = Inter,
							fontWeight = FontWeight.Medium,
							fontSize = 13.sp,
							color = Light80,
							textAlign = TextAlign.Center,
						)
					}
				}
			}

			Card(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.offset(y = (-30).dp),
				shape = RoundedCornerShape(12.dp),
				backgroundColor = Light100,
				border = BorderStroke(width = 1.dp, brush = SolidColor(LightBorder)),
				elevation = 2.dp,
			) {

				Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 26.dp)) {
					Row {
						Text(
							modifier = Modifier
								.weight(0.3f),
							fontFamily = Inter,
							fontWeight = FontWeight.Medium,
							fontSize = 14.sp,
							text = "Тип",
							textAlign = TextAlign.Start,
							color = LightForText,
						)

						Text(
							modifier = Modifier
								.weight(0.7f),
							fontFamily = Inter,
							fontWeight = FontWeight.SemiBold,
							fontSize = 16.sp,
							text = "Доход",
							textAlign = TextAlign.End,
							color = Dark100,
						)
					}

					Row(Modifier.padding(top = 9.dp)) {
						Text(
							modifier = Modifier
								.weight(0.3f),
							fontFamily = Inter,
							fontWeight = FontWeight.Medium,
							fontSize = 14.sp,
							text = "Счет",
							textAlign = TextAlign.Start,
							color = LightForText,
						)

						Text(
							modifier = Modifier
								.weight(0.7f),
							fontFamily = Inter,
							fontWeight = FontWeight.SemiBold,
							fontSize = 16.sp,
							text = contentState.income.account.name,
							textAlign = TextAlign.End,
							color = Dark100,
						)
					}
				}
			}

			Image(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp),
				painter = painterResource(id = R.drawable.line_divider),
				contentDescription = null,
				contentScale = ContentScale.Crop,
			)

			Text(
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 16.dp, end = 16.dp, top = 14.dp),
				fontFamily = Inter,
				fontWeight = FontWeight.SemiBold,
				fontSize = 16.sp,
				text = "Описание",
				textAlign = TextAlign.Start,
				color = LightForText,
			)

			Text(
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 16.dp, end = 16.dp, top = 15.dp),
				fontFamily = Inter,
				fontWeight = FontWeight.Medium,
				fontSize = 16.sp,
				text = contentState.income.comment ?: "Здесь могло быть ваше описание",
				textAlign = TextAlign.Start,
				color = Dark100,
			)

			Spacer(modifier = Modifier.weight(1f))

			Button(
				modifier = Modifier
					.padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
					.fillMaxWidth(),
				onClick = { openEditIncome() },
				contentPadding = PaddingValues(16.dp),
				shape = RoundedCornerShape(16.dp),
				colors = ButtonDefaults.buttonColors(backgroundColor = Green100),
			) {
				Text(
					text = "Редактировать",
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
private fun LoadingScreen() {
	Box(modifier = Modifier.fillMaxSize()) {
		CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
	}
}

@Composable
private fun SuccessScreen() {
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

private fun getFormattedDate(dateInMillis: Long): String {
	val date = Date(dateInMillis)

	val dateFormat = SimpleDateFormat("EE d MMM y hh:mm", Locale("ru"))

	return dateFormat.format(date)
}