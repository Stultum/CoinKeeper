package com.stultus.coinkeeper.component.ui.mvvm

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LaunchBuilder internal constructor(
	val coroutineScope: CoroutineScope,
	val body: suspend CoroutineScope.() -> Unit
) {

	inline infix fun handle(crossinline handler: (Throwable) -> Unit): Job =
		coroutineScope.launch(
			context = CoroutineExceptionHandler { _, t -> handler(t) },
			block = body
		)
}