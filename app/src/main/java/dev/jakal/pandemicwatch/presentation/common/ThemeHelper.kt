package dev.jakal.pandemicwatch.presentation.common

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.jakal.pandemicwatch.R
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository

class ThemeHelper(val repository: CovidRepository) {

    fun initDefaultNightMode() {
        AppCompatDelegate.setDefaultNightMode(repository.getNightMode())
    }

    fun showSelectNightModeDialog(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.select_theme_dialog_title))
            .setSingleChoiceItems(
                getDialogItems(context),
                getCurrentlySelectedItemPosition()
            ) { dialog, selectedItemPosition ->
                val selectedNightMode =
                    selectedItemPosition.mapSelectedItemPositionToNightMode()
                AppCompatDelegate.setDefaultNightMode(selectedNightMode)
                repository.setNightMode(selectedNightMode)
                dialog.dismiss()
            }.show()
    }

    private fun getDialogItems(context: Context) = with(context) {
        arrayOf(
            getString(R.string.select_theme_dialog_item_light),
            getString(R.string.select_theme_dialog_item_dark),
            getString(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    R.string.select_theme_dialog_item_follow_system
                } else {
                    R.string.select_theme_dialog_item_auto_battery
                }
            )
        )
    }

    private fun getCurrentlySelectedItemPosition() =
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> 0
            AppCompatDelegate.MODE_NIGHT_YES -> 1
            else -> 2
        }

    private fun Int.mapSelectedItemPositionToNightMode() =
        when (this) {
            0 -> AppCompatDelegate.MODE_NIGHT_NO
            1 -> AppCompatDelegate.MODE_NIGHT_YES
            2 -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            }
        }
}