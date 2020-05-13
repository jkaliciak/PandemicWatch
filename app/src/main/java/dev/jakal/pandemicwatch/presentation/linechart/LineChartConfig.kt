package dev.jakal.pandemicwatch.presentation.linechart

import android.os.Parcelable
import androidx.annotation.ColorRes
import com.github.mikephil.charting.data.Entry
import dev.jakal.pandemicwatch.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LineChartConfig(
    val entries: List<Entry> = emptyList(),
    @ColorRes
    val foregroundColor: Int,
    @ColorRes
    val onForegroundColor: Int,
    @ColorRes
    val backgroundColor: Int,
    @ColorRes
    val onBackgroundColor: Int,
    val touchEnabled: Boolean = false,
    val animationDuration: Int = 0
) : Parcelable {

    companion object {
        fun caseConfig() = LineChartConfig(
            foregroundColor = R.color.colorChartCaseForeground,
            onForegroundColor = R.color.colorChartCaseOnForeground,
            backgroundColor = R.color.colorChartCaseBackground,
            onBackgroundColor = R.color.colorChartCaseOnBackground
        )

        fun deathConfig() = LineChartConfig(
            foregroundColor = R.color.colorChartDeathForeground,
            onForegroundColor = R.color.colorChartDeathOnForeground,
            backgroundColor = R.color.colorChartDeathBackground,
            onBackgroundColor = R.color.colorChartDeathOnBackground
        )

        fun recoveredConfig() = LineChartConfig(
            foregroundColor = R.color.colorChartRecoveredForeground,
            onForegroundColor = R.color.colorChartRecoveredOnForeground,
            backgroundColor = R.color.colorChartRecoveredBackground,
            onBackgroundColor = R.color.colorChartRecoveredOnBackground
        )
    }
}
