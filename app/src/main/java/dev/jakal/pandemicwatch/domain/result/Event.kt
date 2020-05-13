package dev.jakal.pandemicwatch.domain.result

import androidx.lifecycle.Observer

class Event<out T>(private val content: T, isHandled: Boolean = false) {

    var isHandled: Boolean = isHandled
        private set

    fun getContentIfNotHandled(): T? {
        return if (isHandled) {
            null
        } else {
            isHandled = true
            content
        }
    }

    fun peekContent(): T = content

    override fun equals(other: Any?): Boolean {
        return other is Event<*> && this.content == other.content && this.isHandled == other.isHandled
    }

    override fun hashCode(): Int {
        var result = content?.hashCode() ?: 0
        result = 31 * result + isHandled.hashCode()
        return result
    }
}

class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}

fun <T : Any> T.toEvent(isHandled: Boolean = false): Event<T> {
    return when (isHandled) {
        false -> Event(this)
        true -> Event(this, true)
    }
}