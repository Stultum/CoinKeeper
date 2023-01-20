package com.stultus.coinkeeper.component.ui.mvvm

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

class SuspendGetterBuilder<T> internal constructor(
	val block: suspend CoroutineScope.() -> T
) {

	suspend inline infix fun handle(crossinline handler: (Throwable) -> Unit): T {
		var cancellableContinuation: CancellableContinuation<T>? = null

		val scope = CoroutineScope(
			Dispatchers.Unconfined +
				CoroutineExceptionHandler { _, t ->
					handler(t)
					cancellableContinuation?.cancel()
				}
		)

		return suspendCancellableCoroutine { continuation: CancellableContinuation<T> ->
			cancellableContinuation = continuation
			scope.launch {
				val result = Result.success(block())
				if (continuation.isActive) {
					continuation.resumeWith(result)
				}
			}
		}
	}
}