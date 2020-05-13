package dev.jakal.pandemicwatch.domain.model

import org.threeten.bp.LocalDate

data class Timeline(
    val cases: Map<LocalDate, Int>,
    val deaths: Map<LocalDate, Int>,
    val recovered: Map<LocalDate, Int>
)