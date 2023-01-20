package com.stultus.coinkeeper.feature.feed.ui

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
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
import com.stultus.coinkeeper.component.design.resources.theme.Dark75
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
import com.stultus.coinkeeper.feature.feed.R
import com.stultus.coinkeeper.feature.feed.presentation.FeedState
import com.stultus.coinkeeper.feature.feed.presentation.FeedViewModel
import com.stultus.coinkeeper.feature.feed.presentation.TransactionType
import com.stultus.coinkeeper.shared.income.core.src.main.domain.entity.Income
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.Spending
import org.koin.androidx.compose.getViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FeedScreen(viewModel: FeedViewModel = getViewModel()) {
	LaunchedEffect(Unit) {
		viewModel.loadInitialData()
	}

	val activity = (LocalContext.current as? Activity)

	BackHandler {
		viewModel.closeApp()
	}

	val systemUiController = rememberSystemUiController()
	systemUiController.setNavigationBarColor(Light100)
	systemUiController.navigationBarDarkContentEnabled = true

	when (viewModel.state) {
		FeedState.Error,
		FeedState.Initial,
		FeedState.Loading,
							 -> systemUiController.setStatusBarColor(Light100)

		is FeedState.Content -> systemUiController.setStatusBarColor(Yellow20)

		FeedState.CloseApp   -> {
			activity?.finish()
		}
	}

	AnimatedVisibility(
		visible = viewModel.state == FeedState.Initial || viewModel.state == FeedState.Loading,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		LoadingScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state is FeedState.Content,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ContentScreen(
			contentState = viewModel.state as? FeedState.Content ?: return@AnimatedVisibility,
			openAccountList = viewModel::openAccountList,
			openSpendingCategoryList = viewModel::openSpendingCatsList,
			openAddIncome = viewModel::openAddIncome,
			openAddSpending = viewModel::openAddSpending,
			openSpendingDetails = viewModel::openSpendingDetails,
			openIncomeDetails = viewModel::openIncomeDetails,
			openFullHistory = viewModel::openFullHistory,
			updateDates = viewModel::updateDates,
		)
	}

	AnimatedVisibility(
		visible = viewModel.state == FeedState.Error,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ErrorScreen()
	}
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun ContentScreen(
	contentState: FeedState.Content,
	openAccountList: () -> Unit,
	openSpendingCategoryList: () -> Unit,
	openAddIncome: () -> Unit,
	openAddSpending: () -> Unit,
	openSpendingDetails: (Spending) -> Unit,
	openIncomeDetails: (Income) -> Unit,
	openFullHistory: () -> Unit,
	updateDates: (Long, Long) -> Unit,
) {
	Scaffold(
		backgroundColor = Light100,
		floatingActionButtonPosition = FabPosition.End,
		floatingActionButton = {
			var buttonClicked by remember { mutableStateOf(false) }
			var showAddButtons by remember { mutableStateOf(false) }
			AnimatedVisibility(
				visible = showAddButtons,
				enter = fadeIn(),
				exit = fadeOut(),
			) {
				Button(
					modifier = Modifier.size(56.dp),
					onClick = {
						showAddButtons = !showAddButtons
					},
					shape = CircleShape,
					contentPadding = PaddingValues(0.dp),
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_cross),
						contentDescription = null,
					)
				}
			}

			AnimatedVisibility(
				visible = !showAddButtons,
				enter = fadeIn(),
				exit = fadeOut(),
			) {
				Button(
					modifier = Modifier.size(56.dp),
					onClick = {
						showAddButtons = !showAddButtons
					},
					shape = CircleShape,
					contentPadding = PaddingValues(0.dp),
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_close),
						contentDescription = null,
					)
				}
			}

			AnimatedVisibility(
				visible = showAddButtons,
				enter = slideIn(
					initialOffset = { fullSize ->
						IntOffset(
							fullSize.width - 150,
							fullSize.height
						)
					},
					animationSpec = tween(200, easing = LinearOutSlowInEasing),
				),
				exit = slideOut(
					targetOffset = { fullSize ->
						IntOffset(
							fullSize.width - 150,
							fullSize.height,
						)
					},
					animationSpec = tween(200, easing = FastOutLinearInEasing),
				),
			) {
				Button(
					modifier = Modifier
						.size(56.dp)
						.offset(y = (-67).dp),
					onClick = {
						if (!buttonClicked) {
							openAddIncome()
							buttonClicked = true
						}
					},
					shape = CircleShape,
					contentPadding = PaddingValues(0.dp),
					colors = ButtonDefaults.buttonColors(backgroundColor = Green100)
				) {
					Icon(painter = painterResource(id = R.drawable.ic_income), contentDescription = null, tint = Light100)
				}
			}

			AnimatedVisibility(
				visible = showAddButtons,
				enter = slideIn(
					initialOffset = { fullSize ->
						IntOffset(
							fullSize.width,
							fullSize.height - 150,
						)
					},
					animationSpec = tween(200, easing = LinearOutSlowInEasing),
				),
				exit = slideOut(
					targetOffset = { fullSize ->
						IntOffset(
							fullSize.width,
							fullSize.height - 150,
						)
					},
					animationSpec = tween(200, easing = FastOutLinearInEasing),
				),
			) {
				Button(
					modifier = Modifier
						.size(56.dp)
						.offset(x = (-67).dp),
					onClick = {
						if (!buttonClicked) {
							openAddSpending()
							buttonClicked = true
						}
					},
					shape = CircleShape,
					contentPadding = PaddingValues(0.dp),
					colors = ButtonDefaults.buttonColors(backgroundColor = Red100)
				) {
					Icon(painter = painterResource(id = R.drawable.ic_spending), contentDescription = null, tint = Light100)
				}
			}
		}
	) {
		var cardPainterResource: Painter? = null
		var cashPainterResource: Painter? = null
		val fragmentManager = rememberFragmentManager()
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
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState()),
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.background(Yellow20)
					.padding(16.dp),
				verticalAlignment = CenterVertically,
				horizontalArrangement = Arrangement.End,
			) {
				Spacer(modifier = Modifier.width(32.dp))
				Spacer(modifier = Modifier.weight(1f))

				Button(
					onClick = {
						if (!dateRangePicker.isAdded) {
							dateRangePicker.show(fragmentManager, "")
						}
					},
					contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
					shape = RoundedCornerShape(40.dp),
					colors = ButtonDefaults.buttonColors(backgroundColor = Yellow20),
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

				Spacer(modifier = Modifier.weight(1f))

				var showSettings by remember { mutableStateOf(false) }
				Box {
					IconButton(
						modifier = Modifier.size(32.dp),
						onClick = { showSettings = !showSettings },
					) {
						Icon(painter = painterResource(R.drawable.ic_settings), contentDescription = null, tint = Violet100)
					}

					DropdownMenu(
						expanded = showSettings,
						onDismissRequest = { showSettings = false },
					) {
						listOf("Категории расходов", "Счета").forEach { name ->
							DropdownMenuItem(
								modifier = Modifier.padding(4.dp),
								onClick = {
									if (name == "Категории расходов") {
										openSpendingCategoryList()
									} else {
										openAccountList()
									}
									showSettings = false
								}) {
								Row {
									Text(
										modifier = Modifier
											.align(CenterVertically)
											.weight(0.85f),
										fontFamily = Inter,
										fontWeight = FontWeight.Medium,
										fontSize = 20.sp,
										text = name,
										textAlign = TextAlign.Center,
										color = Dark75,
									)
								}
							}
						}
					}
				}
			}
			Box {
				Card(
					modifier = Modifier
						.fillMaxWidth(),
					shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
					elevation = 0.dp,
					backgroundColor = Yellow20,
				) {
					cardPainterResource = painterResource(R.drawable.amogus)
					cashPainterResource = painterResource(R.drawable.adoptus)
					Column {
						Text(
							modifier = Modifier.fillMaxWidth(),
							text = "Общий счет",
							fontFamily = Inter,
							fontWeight = FontWeight.Medium,
							fontSize = 15.sp,
							color = LightForText,
							textAlign = TextAlign.Center,
						)

						Text(
							modifier = Modifier.fillMaxWidth(),
							text = "${contentState.totalAmount}₽",
							fontFamily = Inter,
							fontWeight = FontWeight.SemiBold,
							fontSize = 40.sp,
							color = Dark75,
							textAlign = TextAlign.Center,
						)

						Column(modifier = Modifier.padding(top = 27.dp, start = 16.dp, end = 16.dp)) {
							Card(
								shape = RoundedCornerShape(28.dp),
								backgroundColor = Green100,
								elevation = 0.dp,
							) {
								Row(
									modifier = Modifier
										.padding(16.dp)
										.fillMaxWidth(),
									verticalAlignment = Alignment.CenterVertically,
									horizontalArrangement = Arrangement.Center,
								) {
									Card(
										modifier = Modifier
											.width(56.dp)
											.aspectRatio(1f),
										shape = RoundedCornerShape(16.dp),
										backgroundColor = Light80,
									) {
										Icon(
											modifier = Modifier
												.fillMaxSize()
												.padding(8.dp),
											painter = painterResource(id = R.drawable.ic_income),
											contentDescription = null,
											tint = Green100,
										)
									}
									Column(
										modifier = Modifier
											.padding(start = 8.dp)
											.weight(1f),
									) {
										Text(
											modifier = Modifier
												.fillMaxWidth(),
											fontFamily = Inter,
											fontWeight = FontWeight.Medium,
											fontSize = 14.sp,
											text = "Доходы",
											textAlign = TextAlign.Center,
											color = Light80,
										)

										Text(
											modifier = Modifier
												.padding(top = 4.dp)
												.fillMaxWidth(),
											fontFamily = Inter,
											fontWeight = FontWeight.SemiBold,
											fontSize = 22.sp,
											text = "${contentState.totalIncome}₽",
											textAlign = TextAlign.Center,
											color = Light100,
										)
									}

									Spacer(modifier = Modifier.width(56.dp))
								}
							}

							Card(
								modifier = Modifier
									.padding(top = 8.dp, bottom = 23.dp),
								shape = RoundedCornerShape(28.dp),
								backgroundColor = Red100,
								elevation = 0.dp,
							) {
								Row(
									modifier = Modifier
										.padding(16.dp)
										.fillMaxWidth(),
									verticalAlignment = Alignment.CenterVertically,
									horizontalArrangement = Arrangement.Center,
								) {
									Card(
										modifier = Modifier
											.width(56.dp)
											.aspectRatio(1f),
										shape = RoundedCornerShape(16.dp),
										backgroundColor = Light80,
									) {
										Icon(
											modifier = Modifier
												.fillMaxSize()
												.padding(8.dp),
											painter = painterResource(id = R.drawable.ic_spending),
											contentDescription = null,
											tint = Red100,
										)
									}
									Column(
										modifier = Modifier
											.padding(start = 8.dp)
											.weight(1f),
									) {
										Text(
											modifier = Modifier.fillMaxWidth(),
											fontFamily = Inter,
											fontWeight = FontWeight.Medium,
											fontSize = 14.sp,
											text = "Расходы",
											textAlign = TextAlign.Center,
											color = Light80,
										)

										Text(
											modifier = Modifier
												.padding(top = 4.dp)
												.fillMaxWidth(),
											fontFamily = Inter,
											fontWeight = FontWeight.SemiBold,
											fontSize = 22.sp,
											text = "${contentState.totalSpending}₽",
											textAlign = TextAlign.Center,
											color = Light100,
										)
									}

									Spacer(modifier = Modifier.width(56.dp))
								}
							}
						}
					}
				}
			}

			if (contentState.statsCircleData.isNotEmpty()) {
				AnimatedCircle(
					modifier = Modifier
						.padding(16.dp)
						.height(200.dp)
						.fillMaxWidth(),
					statsCircleData = contentState.statsCircleData,
				)

				contentState.statsCircleData.forEach { data ->
					Row(
						modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp),
						verticalAlignment = CenterVertically,
					) {
						Card(
							modifier = Modifier,
							shape = RoundedCornerShape(32.dp),
							backgroundColor = Light100,
							border = BorderStroke(width = 1.dp, brush = SolidColor(LightBorder)),
							elevation = 0.dp,
						) {

							Row(
								modifier = Modifier
									.padding(horizontal = 8.dp),
								verticalAlignment = CenterVertically,
							) {

								Canvas(
									modifier = Modifier
										.width(14.dp)
										.height(14.dp)
								) {
									val canvasWidth = size.width
									val canvasHeight = size.width
									drawCircle(
										color = data.color,
										center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
										radius = size.minDimension / 2,
									)
								}

								Text(
									modifier = Modifier
										.padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
									fontFamily = Inter,
									fontWeight = FontWeight.SemiBold,
									fontSize = 14.sp,
									text = data.name,
									textAlign = TextAlign.Start,
									color = Dark50,
								)

							}
						}

						Spacer(modifier = Modifier.weight(1f))

						Text(
							modifier = Modifier
								.padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
							fontFamily = Inter,
							fontWeight = FontWeight.SemiBold,
							fontSize = 24.sp,
							text = "- ${data.amount}₽",
							textAlign = TextAlign.Start,
							color = Red100,
						)
					}

					Card(
						modifier = Modifier
							.height(16.dp)
							.fillMaxWidth()
							.padding(start = 16.dp, end = 16.dp, top = 4.dp),
						shape = RoundedCornerShape(32.dp),

						) {
						LinearProgressIndicator(
							modifier = Modifier,
							progress = data.proportion,
							color = data.color,
							backgroundColor = Light60,
						)
					}
				}
			}

			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(8.dp),
				verticalAlignment = CenterVertically,
			) {
				Text(
					modifier = Modifier
						.padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
					fontFamily = Inter,
					fontWeight = FontWeight.SemiBold,
					fontSize = 18.sp,
					text = "Последние операции",
					textAlign = TextAlign.Start,
					color = Dark50,
				)

				Spacer(modifier = Modifier.weight(1f))

				Button(
					modifier = Modifier
						.padding(top = 4.dp, end = 8.dp, bottom = 4.dp),
					onClick = { openFullHistory() },
					contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
					shape = RoundedCornerShape(40.dp),
					colors = ButtonDefaults.buttonColors(backgroundColor = Violet20),
				) {
					Text(
						text = "История",
						fontFamily = Inter,
						fontWeight = FontWeight.Medium,
						fontSize = 14.sp,
						color = Violet100,
					)
				}
			}

			contentState.lastTransactions.forEach { transaction ->
				if (transaction.type is TransactionType.SpendingType) {
					val spending = transaction.type.spending
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
									text = getFormattedDateForHistory(spending.date),
									textAlign = TextAlign.End,
									color = LightForText,
								)
							}
						}
					}
				} else if (transaction.type is TransactionType.IncomeType) {
					val income = transaction.type.income
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
									text = getFormattedDateForHistory(income.date),
									textAlign = TextAlign.End,
									color = LightForText,
								)
							}
						}
					}
				}
			}

			Spacer(modifier = Modifier.height(80.dp))
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

@Composable
fun rememberFragmentManager(): FragmentManager {
	val context = LocalContext.current
	return remember(context) {
		(context as FragmentActivity).supportFragmentManager
	}
}

private fun getFormattedDateForHistory(dateInMillis: Long): String {
	val date = Date(dateInMillis)

	val dateFormat = SimpleDateFormat("d MMM hh:mm", Locale("ru"))

	return dateFormat.format(date)
}

private fun getFormattedDateForTitle(dateInMillis: Long): String {
	val date = Date(dateInMillis)

	val dateFormat = SimpleDateFormat("d MMM YY", Locale("ru"))

	return dateFormat.format(date)
}