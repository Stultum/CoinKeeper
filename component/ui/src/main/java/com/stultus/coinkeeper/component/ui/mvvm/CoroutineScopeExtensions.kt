package com.stultus.coinkeeper.component.ui.mvvm

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

inline fun CoroutineScope.launch(crossinline onError: (Throwable) -> Unit = {}, noinline block: suspend CoroutineScope.() -> Unit): Job =
	launch(CoroutineExceptionHandler { _, throwable -> onError(throwable) }, block = block)

inline fun <T> CoroutineScope.async(crossinline onError: (Throwable) -> Unit = {}, noinline block: suspend CoroutineScope.() -> T): Deferred<T> =
	async(CoroutineExceptionHandler { _, throwable -> onError(throwable) }, block = block)

fun CoroutineScope.launchBuilderFrom(block: suspend CoroutineScope.() -> Unit): LaunchBuilder =
	LaunchBuilder(this, block)

fun <T> CoroutineScope.suspendGetterBuilderFrom(block: suspend CoroutineScope.() -> T): SuspendGetterBuilder<T> =
	SuspendGetterBuilder(block)