package dev.jakal.pandemicwatch.common.utils

fun Double.formatDecimalPoints(decimalPoints: Int) = "%.${decimalPoints}f".format(this)