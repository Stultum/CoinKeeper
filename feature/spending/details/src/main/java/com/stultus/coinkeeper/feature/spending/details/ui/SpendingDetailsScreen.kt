package com.stultus.coinkeeper.feature.spending.details.ui

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
import com.stultus.coinkeeper.feature.spending.details.R
import com.stultus.coinkeeper.feature.spending.details.presentation.SpendingDetailsState
import com.stultus.coinkeeper.feature.spending.details.presentation.SpendingDetailsViewModel
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SpendingDetailsScreen(viewModel: SpendingDetailsViewModel = getViewModel { parametersOf(spending) }, spending: Spending) {
	val systemUiController = rememberSystemUiController()
	systemUiController.setNavigationBarColor(Light100)
	systemUiController.navigationBarDarkContentEnabled = true

	when (viewModel.state) {
		SpendingDetailsState.Error,
		SpendingDetailsState.Initial,
		SpendingDetailsState.Loading,
		SpendingDetailsState.Success -> systemUiController.setStatusBarColor(Light100)

		is SpendingDetailsState.Content -> systemUiController.setStatusBarColor(Red100)
	}

	AnimatedVisibility(
		visible = viewModel.state == SpendingDetailsState.Initial || viewModel.state == SpendingDetailsState.Loading,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		LoadingScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state is SpendingDetailsState.Content,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ContentScreen(
			contentState = viewModel.state as? SpendingDetailsState.Content ?: return@AnimatedVisibility,
			navigateBack = viewModel::navigateBack,
			deleteSpending = viewModel::deleteSpending,
			openEditSpending = viewModel::navigateToEditSpending,
		)
	}

	AnimatedVisibility(
		visible = viewModel.state == SpendingDetailsState.Success,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		SuccessScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state == SpendingDetailsState.Error,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ErrorScreen()
	}

	BackHandler { viewModel.navigateBack() }
}

@Composable
private fun ContentScreen(
	contentState: SpendingDetailsState.Content,
	navigateBack: () -> Unit,
	deleteSpending: () -> Unit,
	openEditSpending: () -> Unit,
) {
	Scaffold(
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
					text = "Детали транзакции",
					fontFamily = Inter,
					fontWeight = FontWeight.SemiBold,
					fontSize = 18.sp,
					color = Light100,
					textAlign = TextAlign.Center,
				)

				IconButton(
					modifier = Modifier.size(32.dp),
					onClick = { deleteSpending() }
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
					backgroundColor = Red100,
					elevation = 0.dp,
				) {
					Column {
						Text(
							modifier = Modifier.fillMaxWidth(),
							text = "${contentState.spending.amount}₽",
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
							text = getFormattedDate(contentState.spending.date),
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
							text = "Расход",
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
							text = "Категория",
							textAlign = TextAlign.Start,
							color = LightForText,
						)

						Text(
							modifier = Modifier
								.weight(0.7f),
							fontFamily = Inter,
							fontWeight = FontWeight.SemiBold,
							fontSize = 16.sp,
							text = contentState.spending.category.name,
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
							text = contentState.spending.account.name,
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
				text = contentState.spending.comment ?: "Здесь могло быть ваше описание",
				textAlign = TextAlign.Start,
				color = Dark100,
			)

			Spacer(modifier = Modifier.weight(1f))

			Button(
				modifier = Modifier
					.padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
					.fillMaxWidth(),
				onClick = { openEditSpending() },
				contentPadding = PaddingValues(16.dp),
				shape = RoundedCornerShape(16.dp),
				colors = ButtonDefaults.buttonColors(backgroundColor = Red100),
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