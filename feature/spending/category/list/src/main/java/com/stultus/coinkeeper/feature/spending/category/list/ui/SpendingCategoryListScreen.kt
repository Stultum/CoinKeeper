package com.stultus.coinkeeper.feature.spending.category.list.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.stultus.coinkeeper.component.design.resources.theme.Green100
import com.stultus.coinkeeper.component.design.resources.theme.Green20
import com.stultus.coinkeeper.component.design.resources.theme.Green40
import com.stultus.coinkeeper.component.design.resources.theme.Inter
import com.stultus.coinkeeper.component.design.resources.theme.Light100
import com.stultus.coinkeeper.component.design.resources.theme.Light20
import com.stultus.coinkeeper.component.design.resources.theme.Light80
import com.stultus.coinkeeper.component.design.resources.theme.LightForText
import com.stultus.coinkeeper.component.design.resources.theme.Red100
import com.stultus.coinkeeper.component.design.resources.theme.Red20
import com.stultus.coinkeeper.component.design.resources.theme.Red40
import com.stultus.coinkeeper.component.design.resources.theme.Violet100
import com.stultus.coinkeeper.component.design.resources.theme.Violet20
import com.stultus.coinkeeper.component.design.resources.theme.Yellow100
import com.stultus.coinkeeper.component.design.resources.theme.Yellow20
import com.stultus.coinkeeper.feature.spending.category.list.R
import com.stultus.coinkeeper.feature.spending.category.list.presentation.SpendingCategoryListState
import com.stultus.coinkeeper.feature.spending.category.list.presentation.SpendingCategoryListViewModel
import com.stultus.coinkeeper.shared.spending.core.src.main.domain.entity.SpendingCategory
import org.koin.androidx.compose.getViewModel

@Composable
fun SpendingCategoryListScreen(viewModel: SpendingCategoryListViewModel = getViewModel()) {
	LaunchedEffect(Unit) {
		viewModel.loadSpendingCatsList()
	}
	val systemUiController = rememberSystemUiController()
	systemUiController.setNavigationBarColor(Light100)
	systemUiController.setStatusBarColor(Light100)
	systemUiController.navigationBarDarkContentEnabled = true

	AnimatedVisibility(
		visible = viewModel.state == SpendingCategoryListState.Initial || viewModel.state == SpendingCategoryListState.Loading,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		LoadingScreen()
	}

	AnimatedVisibility(
		visible = viewModel.state is SpendingCategoryListState.Content,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ContentScreen(
			contentState = viewModel.state as? SpendingCategoryListState.Content ?: return@AnimatedVisibility,
			navigateBack = viewModel::navigateBack,
			openAddSpendingCategory = viewModel::navigateToAddNewSpendingCategory,
			openEditCategory = viewModel::openEditCategory,
		)
	}

	AnimatedVisibility(
		visible = viewModel.state == SpendingCategoryListState.Error,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		ErrorScreen()
	}

	BackHandler { viewModel.navigateBack() }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ContentScreen(
	contentState: SpendingCategoryListState.Content,
	navigateBack: () -> Unit,
	openAddSpendingCategory: () -> Unit,
	openEditCategory: (SpendingCategory) -> Unit,
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
					text = "Категории расходов",
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
	) {
		LazyColumn(
			modifier = Modifier.fillMaxSize(),
			contentPadding = PaddingValues(bottom = 80.dp)
		) {
			stickyHeader {
				Card(
					modifier = Modifier.fillMaxWidth(),
					elevation = 0.dp,
				) {
					Box(modifier = Modifier.fillMaxSize()) {
						Image(
							modifier = Modifier.fillMaxWidth(),
							painter = painterResource(id = R.drawable.ellipse_background),
							contentDescription = null,
							contentScale = ContentScale.Crop,
						)

						Column(
							modifier = Modifier
								.fillMaxSize()
								.align(Alignment.Center)
						) {
							Text(
								modifier = Modifier.fillMaxWidth(),
								text = "Траты по всем категориям",
								fontFamily = Inter,
								fontWeight = FontWeight.Medium,
								fontSize = 14.sp,
								color = LightForText,
								textAlign = TextAlign.Center,
							)

							Text(
								modifier = Modifier.fillMaxWidth(),
								text = "${contentState.totalAmount}₽",
								fontFamily = Inter,
								fontWeight = FontWeight.SemiBold,
								fontSize = 40.sp,
								color = Dark100,
								textAlign = TextAlign.Center,
							)
						}
					}
				}
			}
			items(contentState.spendingCatsList) { spendingCategory ->
				Row(
					modifier = Modifier
						.padding(16.dp)
						.clickable {
							openEditCategory(spendingCategory)
						}
				) {
					Card(
						modifier = Modifier
							.weight(0.15f)
							.aspectRatio(1f),
						shape = RoundedCornerShape(16.dp),
						backgroundColor = getBackgroundColor(spendingCategory.color),
					) {
						Icon(
							modifier = Modifier
								.fillMaxSize()
								.padding(12.dp),
							painter = painterResource(id = getIconRes(name = spendingCategory.iconName)),
							contentDescription = null,
							tint = Color(spendingCategory.color),
						)
					}
					Text(
						modifier = Modifier
							.weight(0.85f)
							.align(Alignment.CenterVertically)
							.padding(start = 8.dp),
						fontFamily = Inter,
						fontWeight = FontWeight.SemiBold,
						fontSize = 18.sp,
						text = spendingCategory.name,
						textAlign = TextAlign.Center,
						color = Dark100,
					)
				}
			}
		}

		Column(
			modifier = Modifier
				.fillMaxSize()
		) {
			Spacer(modifier = Modifier.weight(1f))

			Button(
				modifier = Modifier
					.padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
					.fillMaxWidth(),
				onClick = { openAddSpendingCategory() },
				contentPadding = PaddingValues(16.dp),
				shape = RoundedCornerShape(16.dp),
			) {
				Text(
					text = "+ Добавить новую категорию",
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