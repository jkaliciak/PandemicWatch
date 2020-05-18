package dev.jakal.pandemicwatch.presentation.linechart

import android.os.Parcelable
import androidx.annotation.ColorRes
import com.github.mikephil.charting.data.Entry
import dev.jakal.pandemicwatch.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LineDataConfig(
    val entries: List<Entry>,
    val label: String,
    @ColorRes
    val foregroundColor: Int,
    @ColorRes
    val onForegroundColor: Int
) : Parcelable {

    companion object {
        fun caseConfig(entries: List<Entry>, label: String = "") = LineDataConfig(
            entries = entries,
            label = label,
            foregroundColor = R.color.colorChartCaseForeground,
            onForegroundColor = R.color.colorChartCaseOnForeground
        )

        fun deathConfig(entries: List<Entry>, label: String = "") = LineDataConfig(
            entries = entries,
            label = label,
            foregroundColor = R.color.colorChartDeathForeground,
            onForegroundColor = R.color.colorChartDeathOnForeground
        )

        fun recoveredConfig(entries: List<Entry>, label: String = "") = LineDataConfig(
            entries = entries,
            label = label,
            foregroundColor = R.color.colorChartRecoveredForeground,
            onForegroundColor = R.color.colorChartRecoveredOnForeground
        )
    }
}