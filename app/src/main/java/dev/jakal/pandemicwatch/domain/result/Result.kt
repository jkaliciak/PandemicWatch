package dev.jakal.pandemicwatch.domain.result

sealed class Result<out RESULT> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=]$data"
            is Error -> "Error[exception=]$exception"
            Loading -> "Loading"
        }
    }
}

val <T> Result<T>.data: T?
    get() = (this as? Result.Success)?.data

fun <T : Any> Result<T>.onError(block: (Result.Error) -> Unit) {
    if (this is Result.Error) block(this)
}

//fun <T> Result<T>.successOr(fallback: T): T {
//    return (this as? Result.Success<T>)?.data ?: fallback
//}
//
//inline fun <reified T> Result<T>.updateOnSuccess(liveData: MutableLiveData<T>) {
//    if (this is Result.Success) {
//        liveData.value = data
//    }
//}