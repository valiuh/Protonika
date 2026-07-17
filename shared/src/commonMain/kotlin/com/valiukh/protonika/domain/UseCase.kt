package com.valiukh.protonika.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UseCaseResult<T> private constructor(
    val value: T?,
    val error: Throwable?,
) {
    companion object {
        fun <T> success(value: T): UseCaseResult<T> = UseCaseResult(
            value = value,
            error = null,
        )
        fun <T> failure(error: Throwable): UseCaseResult<T> = UseCaseResult(
            value = null,
            error = error,
        )
        fun <T> unauthorized(): UseCaseResult<T> = UseCaseResult(
            value = null,
            error = null,
        )
    }

    fun isSuccess(): Boolean = value != null && error == null
    fun isFailure(): Boolean = error != null
    fun isUnauthorized(): Boolean = value == null && error == null

    inline fun getOrElse(onFailure: (exception: Throwable) -> T): T {
        return if (isSuccess()) value as T
        else onFailure(error ?: Throwable("Unknown error"))
    }

    inline fun onSuccess(action: (T) -> Unit): UseCaseResult<T> {
        if (isSuccess()) value?.let { action(it) }
        return this
    }

    inline fun onFailure(action: (Throwable) -> Unit): UseCaseResult<T> {
        if (isFailure()) error?.let { action(it) }
        return this
    }

    fun getOrNull(): T? = if (isSuccess()) value else null

    fun getOrDefault(default: T): T {
        return if (isSuccess()) value as T else default
    }

    inline fun <R> fold(
        onSuccess: (T) -> R,
        onFailure: (Throwable) -> R,
        onUnauthorized: () -> R
    ): R {
        return when {
            isSuccess() -> onSuccess(value as T)
            isFailure() -> onFailure(error as Throwable)
            isUnauthorized() -> onUnauthorized()
            else -> throw IllegalStateException("Invalid UseCaseResult state")
        }
    }

    fun getOrThrow(): T {
        if (isSuccess()) return value as T
        throw error ?: Throwable("Unknown error")
    }
}

inline fun <T> UseCaseResult<T>.getOrElse(onFailure: (exception: Throwable) -> T): T {
    return if (isSuccess()) value as T
    else onFailure(error ?: Throwable("Unknown error"))
}


abstract class BaseUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(parameters: P): UseCaseResult<R> {
        return withContext(coroutineDispatcher) {
            try {
                UseCaseResult.success(execute(parameters))
            } catch (e: Throwable) {
                UseCaseResult.failure(e)
            }
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R
}

abstract class BaseNonSuspendUseCase<in P, R> {
    operator fun invoke(parameters: P): UseCaseResult<R> {
        return try {
            UseCaseResult.success(execute(parameters))
        } catch (e: Throwable) {
            UseCaseResult.failure(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract fun execute(parameters: P): R
}

abstract class BaseParamsLessUseCase<R>(private val coroutineDispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(): UseCaseResult<R> {
        return withContext(coroutineDispatcher) {
            try {
                UseCaseResult.success(execute())
            } catch (e: Throwable) {
                UseCaseResult.failure(e)
            }
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(): R
}

abstract class BaseNonSuspendParamsLessUseCase<R> {
    operator fun invoke(): UseCaseResult<R> {
        return try {
            UseCaseResult.success(execute())
        } catch (e: Throwable) {
            UseCaseResult.failure(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract fun execute(): R
}