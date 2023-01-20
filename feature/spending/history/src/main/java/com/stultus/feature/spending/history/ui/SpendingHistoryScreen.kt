package com.stultus.feature.spending.history.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.Pair
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.datepicker.MaterialDatePicker
import com.stultus.coinkeeper.component.design.resources.theme.Blue100
import com.stultus.coinkeeper.component.design.resources.theme.Blue20
import com.stultus.coinkeeper.component.design.resources.theme.Blue40
import com.stultus.coinkeeper.component.design.resources.theme.Dark100
import com.stultus.coinkeeper.component.design.resources.theme.Dark25
import com.stultus.coinkeeper.component.design.resources.theme.Dark50
import com.stultus.coinkeeper.component.design.resources.theme.Green100
import com.stultus.coinkeeper.component.design.resources.theme.Green20
import com.stultus.coinkeeper.component.design.resources.theme.Green40
import com.stultus.coinkeeper.component.design.resources.theme.Inter
import com.stultus.coinkeeper.component.design.resources.theme.Light100
import com.stultus.coinkeeper.component.design.resources.theme.Light20
import com.stultus.coinkeeper.component.design.resources.theme.Light60
import com.stultus.coinkeeper.component.design.resources.theme.Light80
import com.stultus.coinkeeper.component.design.resources.theme.LightBorder
import com.stultus.coinkeeper.component.design.resources.theme.LightForText
import com.stultus.coinkeeper.component.design.resources.theme.Red100
import com.stultus.coinkeeper.component.design.resources.theme.Red20
import com.stultus.coinkeeper.component.design.resources.theme.Red40
import com.stultus.coinkeeper.component.design.resources.theme.Violet100
import com.stultus.coinkeeper.component.design.resources.theme.Violet20
import com.stultus.coinkeeper.component.design.resources.theme.Yellow100
import com.stultus.coinkeeper.component.design.resources.theme.Yellow20
import com.stultus.coinkeeper.feature.spending.history.R
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import com.stultus.feature.spending.history.presentation.HistoryContentType
import com.stultus.feature.spending.history.presentation.SpendingHistoryState
import com.stultus.feature.spending.history.presentation.SpendingHistoryViewModel
import org.koin.androidx.compose.getViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SpendingHistoryScreen(viewModel: SpendingHistoryViewModel = getViewModel()) {
	LaunchedEffect(Unit) {
		viewModel.loadSpendingList()
	}
	val systemUiController = rememberSystemUiController()
	systemUiController.setNavigationBarColor(Light100)
	systemUiController.setStatusBarColor(Light100)
	systemUiController.navigationBarDarkContentEnabled = true

	AnimatedVisibility(
		visible = viewModel.state == SpendingHistoryState.Initial || viewModel.state == SpendingHistoryState.Loading,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		LoadingScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state is SpendingHistoryState.Content,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ContentScreen(
			contentState = viewModel.state as? SpendingHistoryState.Content ?: return@AnimatedVisibility,
			navigateBack = viewModel::navigateBack,
			openAddSpending = viewModel::navigateToAddNewSpending,
			openAddIncome = viewModel::navigateToAddNewIncome,
			updateContentType = viewModel::updateContentType,
			openSpendingDetails = viewModel::navigateToSpendingDetails,
			openIncomeDetails = viewModel::navigateToIncomeDetails,
			updateDates = viewModel::updateDates,
		)
	}

	AnimatedVisibility(
		visible = viewModel.state == SpendingHistoryState.Error,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ErrorScreen()
	}

	BackHandler { viewModel.navigateBack() }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun ContentScreen(
	contentState: SpendingHistoryState.Content,
	navigateBack: () -> Unit,
	openAddSpending: () -> Unit,
	openAddIncome: () -> Unit,
	updateContentType: (HistoryContentType) -> Unit,
	openSpendingDetails: (Spending) -> Unit,
	openIncomeDetails: (Income) -> Unit,
	updateDates: (Long, Long) -> Unit,
) {
	Scaffold(
		topBar = {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.background(Light100)
					.padding(16.dp),
				verticalAlignment = Alignment.CenterVertically,
			) {
				IconButton(
					modifier = Modifier.size(32.dp),
					onClick = { navigateBack() }
				) {
					Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = null, tint = Dark100)
				}

				Text(
					modifier = Modifier.weight(1f),
					text = "История операций",
					fontFamily = Inter,
					fontWeight = FontWeight.SemiBold,
					fontSize = 18.sp,
					color = Dark100,
					textAlign = TextAlign.Center,
				)

				Spacer(modifier = Modifier.size(32.dp))
			}
		},
		backgroundColor = Light100,
		floatingActionButtonPosition = FabPosition.End,
		floatingActionButton = {
			val fabColor by animateColorAsState(
				targetValue = if (contentState.contentType == HistoryContentType.Spending) {
					Red100
				} else {
					Green100
				}
			)
			var buttonClicked by remember { mutableStateOf(false) }
			FloatingActionButton(
				onClick = {
					if (!buttonClicked) {
						if (contentState.contentType == HistoryContentType.Spending) {
							openAddSpending()
						} else {
							openAddIncome()
						}
						buttonClicked = true
					}
				},
				backgroundColor = fabColor,
				contentColor = Light100,
			) {
				Icon(painter = painterResource(id = R.drawable.ic_close), contentDescription = null)
			}
		}
	) {
		val fragmentManager = rememberFragmentManager()
		LazyColumn(
			modifier = Modifier.fillMaxSize(),
			contentPadding = PaddingValues(bottom = 80.dp)
		) {
			var cardPainterResource: Painter? = null
			var cashPainterResource: Painter? = null
			val dateRangePicker =
				MaterialDatePicker.Builder.dateRangePicker()
					.setTitleText("Выберите даты")
					.setSelection(
						Pair(
							contentState.periodFirstDate,
							contentState.periodSecondDate,
						)
					)
					.setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
					.build()
			dateRangePicker.addOnPositiveButtonClickListener {
				val firstDate = dateRangePicker.selection?.first
				val secondDate = dateRangePicker.selection?.second

				firstDate?.let {
					secondDate?.let {
						updateDates(firstDate, secondDate)
					}
				}
			}

			stickyHeader {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.Center
				) {
					Button(
						onClick = {
							if (!dateRangePicker.isAdded) {
								dateRangePicker.show(fragmentManager, "")
							}
						},
						contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
						shape = RoundedCornerShape(40.dp),
						colors = ButtonDefaults.buttonColors(backgroundColor = Light60),
						border = BorderStroke(width = 1.dp, brush = SolidColor(LightBorder)),
					) {
						Text(
							text = "${getFormattedDateForTitle(contentState.periodFirstDate)} - ${getFormattedDateForTitle(contentState.periodSecondDate)}",
							fontFamily = Inter,
							fontWeight = FontWeight.SemiBold,
							fontSize = 14.sp,
							color = Dark50,
							textAlign = TextAlign.Center,
						)
					}
				}

				Card(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 8.dp, start = 16.dp, end = 16.dp),
					elevation = 0.dp,
					shape = RoundedCornerShape(32.dp),
					backgroundColor = Light60,
				) {
					cardPainterResource = painterResource(R.drawable.amogus)
					cashPainterResource = painterResource(R.drawable.adoptus)
					val spendingBGColor by animateColorAsState(
						targetValue = if (contentState.contentType == HistoryContentType.Spending) {
							Red100
						} else {
							Light60
						}
					)
					val spendingTextColor by animateColorAsState(
						targetValue = if (contentState.contentType == HistoryContentType.Spending) {
							Light80
						} else {
							LightForText
						}
					)
					val incomeBGColor by animateColorAsState(
						targetValue = if (contentState.contentType == HistoryContentType.Income) {
							Green100
						} else {
							Light60
						}
					)
					val incomeTextColor by animateColorAsState(
						targetValue = if (contentState.contentType == HistoryContentType.Income) {
							Light80
						} else {
							LightForText
						}
					)
					Row {
						Button(
							modifier = Modifier
								.padding(4.dp)
								.fillMaxWidth()
								.weight(0.5f),
							onClick = { updateContentType(HistoryContentType.Spending) },
							contentPadding = PaddingValues(15.dp),
							shape = RoundedCornerShape(32.dp),
							colors = ButtonDefaults.buttonColors(backgroundColor = spendingBGColor),
							elevation = ButtonDefaults.elevation(0.dp),
						) {
							Text(
								text = "Расходы",
								fontFamily = Inter,
								fontWeight = FontWeight.Medium,
								fontSize = 16.sp,
								color = spendingTextColor,
							)
						}

						Button(
							modifier = Modifier
								.padding(4.dp)
								.fillMaxWidth()
								.weight(0.5f),
							onClick = { updateContentType(HistoryContentType.Income) },
							contentPadding = PaddingValues(15.dp),
							shape = RoundedCornerShape(32.dp),
							colors = ButtonDefaults.buttonColors(backgroundColor = incomeBGColor),
							elevation = ButtonDefaults.elevation(0.dp),
						) {
							Text(
								text = "Доходы",
								fontFamily = Inter,
								fontWeight = FontWeight.Medium,
								fontSize = 16.sp,
								color = incomeTextColor,
							)
						}
					}
				}
			}
			if (contentState.contentType == HistoryContentType.Spending) {
				items(contentState.spendingList) { spending ->
					Card(
						modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
						shape = RoundedCornerShape(24.dp),
						backgroundColor = Light80,
						elevation = 0.dp,
						onClick = { openSpendingDetails(spending) },
					) {
						Row(modifier = Modifier.padding(16.dp)) {
							Card(
								modifier = Modifier
									.weight(0.2f)
									.aspectRatio(1f),
								shape = RoundedCornerShape(16.dp),
								backgroundColor = getBackgroundColor(spending.category.color),
							) {
								Icon(
									modifier = Modifier
										.fillMaxSize()
										.padding(16.dp),
									painter = painterResource(id = getIconRes(name = spending.category.iconName)),
									contentDescription = null,
									tint = Color(spending.category.color),
								)
							}
							Column(
								modifier = Modifier
									.weight(0.45f)
									.padding(start = 8.dp),
							) {
								Text(
									modifier = Modifier.padding(top = 6.dp),
									fontFamily = Inter,
									fontWeight = FontWeight.Medium,
									fontSize = 16.sp,
									text = getShorterString(spending.category.name),
									textAlign = TextAlign.Start,
									color = Dark100,
								)

								Text(
									modifier = Modifier.padding(top = 13.dp, bottom = 6.dp),
									fontFamily = Inter,
									fontWeight = FontWeight.Medium,
									fontSize = 13.sp,
									text = getShorterString(spending.comment ?: ""),
									textAlign = TextAlign.Start,
									color = LightForText,
								)
							}
							Column(
								modifier = Modifier
									.weight(0.35f)
									.padding(end = 8.dp),
								horizontalAlignment = Alignment.End,
							) {
								Text(
									modifier = Modifier.padding(top = 6.dp),
									fontFamily = Inter,
									fontWeight = FontWeight.SemiBold,
									fontSize = 16.sp,
									text = "- ${spending.amount}",
									textAlign = TextAlign.End,
									color = Red100,
								)

								Text(
									modifier = Modifier.padding(top = 13.dp, bottom = 6.dp),
									fontFamily = Inter,
									fontWeight = FontWeight.Medium,
									fontSize = 13.sp,
									text = getFormattedDate(spending.date),
									textAlign = TextAlign.End,
									color = LightForText,
								)
							}
						}
					}
				}
			} else {
				items(contentState.incomeList) { income ->
					Card(
						modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
						shape = RoundedCornerShape(24.dp),
						backgroundColor = Light80,
						elevation = 0.dp,
						onClick = { openIncomeDetails(income) },
					) {
						Row(modifier = Modifier.padding(16.dp)) {
							Card(
								modifier = Modifier
									.weight(0.2f)
									.aspectRatio(1f),
								shape = RoundedCornerShape(16.dp),
							) {
								Image(
									modifier = Modifier
										.fillMaxSize(),
									contentScale = ContentScale.FillHeight,
									painter = if (income.account.iconName == "amogus") {
										requireNotNull(cardPainterResource)
									} else {
										requireNotNull(cashPainterResource)
									},
									contentDescription = null,
								)
							}
							Column(
								modifier = Modifier
									.weight(0.45f)
									.padding(start = 8.dp),
							) {
								Text(
									modifier = Modifier.padding(top = 6.dp),
									fontFamily = Inter,
									fontWeight = FontWeight.Medium,
									fontSize = 16.sp,
									text = getShorterString(income.account.name),
									textAlign = TextAlign.Start,
									color = Dark100,
								)

								Text(
									modifier = Modifier.padding(top = 13.dp, bottom = 6.dp),
									fontFamily = Inter,
									fontWeight = FontWeight.Medium,
									fontSize = 13.sp,
									text = getShorterString(income.comment ?: ""),
									textAlign = TextAlign.Start,
									color = LightForText,
								)
							}
							Column(
								modifier = Modifier
									.weight(0.35f)
									.padding(end = 8.dp),
								horizontalAlignment = Alignment.End,
							) {
								Text(
									modifier = Modifier.padding(top = 6.dp),
									fontFamily = Inter,
									fontWeight = FontWeight.SemiBold,
									fontSize = 16.sp,
									text = "+ ${income.amount}",
									textAlign = TextAlign.End,
									color = Green100,
								)

								Text(
									modifier = Modifier.padding(top = 13.dp, bottom = 6.dp),
									fontFamily = Inter,
									fontWeight = FontWeight.Medium,
									fontSize = 13.sp,
									text = getFormattedDate(income.date),
									textAlign = TextAlign.End,
									color = LightForText,
								)
							}
						}
					}
				}
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

@Composable
fun rememberFragmentManager(): FragmentManager {
	val context = LocalContext.current
	return remember(context) {
		(context as FragmentActivity).supportFragmentManager
	}
}

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

private fun getShorterString(string: String): String {
	var formattedString: String

	if (string.length > 16) {
		formattedString = string.take(15)
		formattedString = formattedString.plus("..")
	} else {
		formattedString = string
	}

	return formattedString
}

private fun getFormattedDate(dateInMillis: Long): String {
	val date = Date(dateInMillis)

	val dateFormat = SimpleDateFormat("d MMM hh:mm", Locale("ru"))

	return dateFormat.format(date)
}

private fun getFormattedDateForTitle(dateInMillis: Long): String {
	val date = Date(dateInMillis)

	val dateFormat = SimpleDateFormat("d MMM YY", Locale("ru"))

	return dateFormat.format(date)
}