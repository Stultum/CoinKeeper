package com.stultus.coinkeeper.feature.splash.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.stultus.coinkeeper.component.design.resources.theme.Inter
import com.stultus.coinkeeper.component.design.resources.theme.Light100
import com.stultus.coinkeeper.component.design.resources.theme.PinkSplash
import com.stultus.coinkeeper.component.design.resources.theme.Violet100
import com.stultus.coinkeeper.feature.splash.R
import com.stultus.coinkeeper.feature.splash.presentation.SplashViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SplashScreen(viewModel: SplashViewModel = getViewModel()) {
	val systemUiController = rememberSystemUiController()
	systemUiController.setNavigationBarColor(Light100)
	systemUiController.navigationBarDarkContentEnabled = true
	systemUiController.setStatusBarColor(Violet100)

	AnimatedVisibility(
		visible = true,
		enter = fadeIn(),
		exit = fadeOut(),
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Violet100),
			contentAlignment = Alignment.Center,
		) {
			Icon(
				modifier = Modifier
					.size(96.dp)
					.offset(x = -(35).dp),
				painter = painterResource(R.drawable.splash),
				contentDescription = null,
				tint = PinkSplash,
			)
			Text(
				modifier = Modifier.padding(16.dp),
				text = "coin keeper",
				fontFamily = Inter,
				fontWeight = FontWeight.Bold,
				fontSize = 56.sp,
				color = Light100,
				textAlign = TextAlign.Center,
			)
		}
	}
}