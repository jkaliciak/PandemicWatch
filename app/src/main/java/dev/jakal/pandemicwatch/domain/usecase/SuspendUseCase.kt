package dev.jakal.pandemicwatch.domain.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class SuspendUseCase<in PARAM, RESULT>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(parameters: PARAM): Result<RESULT> {
        return try {
            withContext(coroutineDispatcher) {
                execute(parameters).let {
                    Result.Success(it)
                }
            }
        } catch (e: Exception) {
            Timber.d(e)
            Result.Error(e)
        }
    }

    protected abstract suspend fun execute(parameters: PARAM): RESULT
}