package com.stultus.coinkeeper.component.ui.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {

	val scope: CoroutineScope
		get() = viewModelScope

	protected fun launchTrying(block: suspend CoroutineScope.() -> Unit): LaunchBuilder =
		scope.launchBuilderFrom(block)

	protected fun <T> CoroutineScope.launchTrying(block: suspend CoroutineScope.() -> T): SuspendGetterBuilder<T> =
		scope.suspendGetterBuilderFrom(block)

	protected fun launch(block: suspend CoroutineScope.() -> Unit): Job =
		scope.launch(block = block)

	protected fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> =
		scope.async(block = block)
}