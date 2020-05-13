package dev.jakal.pandemicwatch.domain.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber

abstract class FlowUseCase<in PARAM, RESULT>(private val coroutineDispatcher: CoroutineDispatcher) {

    @ExperimentalCoroutinesApi
    open operator fun invoke(parameters: PARAM): Flow<Result<RESULT>> {
        return execute(parameters).map<RESULT, Result<RESULT>> { Result.Success(it) }
            .catch { e ->
                run {
                    Timber.d(e)
                    emit(Result.Error(Exception(e)))
                }
            }
            .flowOn(coroutineDispatcher)
    }

    abstract fun execute(parameters: PARAM): Flow<RESULT>
}