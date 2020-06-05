package dev.jakal.pandemicwatch.presentation.common

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyboardHelper {

    companion object {
        fun hideKeyboardFrom(context: Context?, view: View) {
            context?.let {
                val imm =
                    context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}