package org.eclipse.keyple.famoco.validator.util

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.Continuation

/**
 *
 *  created on 01/10/2020
 *
 *  @author youssefamrani
 */

suspend inline fun <T> suspendCoroutineWithTimeout(timeout: Long, crossinline block: (Continuation<T>) -> Unit ) : T? {
    var finalValue : T? = null
    withTimeoutOrNull(timeout) {
        finalValue = suspendCancellableCoroutine(block = block)
    }
    return finalValue
}