package dev.jakal.pandemicwatch.infrastructure.logging

import com.chuckerteam.chucker.api.ChuckerCollector
import timber.log.Timber

class AppDebugTree(
    private val chuckerCollector: ChuckerCollector
) : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String? {
        with(element) {
            return "($fileName:$lineNumber)$methodName()"
        }
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)

        t?.let { chuckerCollector.onError(tag ?: "UnknownTag", t) }
    }
}