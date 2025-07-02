package dev.wjteo

import kotlin.math.abs

fun main(args: Array<String>) {
    var marauderOnly = false
    var targetCrit = 0.0
    var infusions = 0

    args.reversed().forEachIndexed { i, it ->
        if (i == 0) {
            targetCrit = it.toDoubleOrNull() ?: 0.0
            return@forEachIndexed
        }

        if (it.equals("-marauder", true)) {
            marauderOnly = true
            return@forEachIndexed
        }

        if (it.startsWith("-i=", true)) {
            infusions = it.substringAfter("-i=").toIntOrNull() ?: 0
            return@forEachIndexed
        }
    }

    if (targetCrit == 0.0) {
        println("Invalid crit. target. Syntax: <command> [-marauder] [-i=?] <crit>")
        return
    }

    val targetPrecision = (targetCrit * 21) + 895 - (infusions * 5)
    calculate(targetPrecision.toInt(), marauderOnly)
}

fun calculate(targetPrecision: Int, marauderOnly: Boolean) {
    val berserkerDiff: ArrayList<Int> = arrayListOf(18, 13, 40, 13, 27, 13, 23, 36, 36, 49, 41, 41, 71)
    val dragonDiff1: ArrayList<Int> = arrayListOf(15, 12, 34, 12, 23, 12, 13, 25, 25, 37, 29, 29, 61)
    val dragonDiff2: ArrayList<Int> = arrayListOf(24, 18, 54, 18, 37, 18, 25, 43, 43, 62, 50, 50, 97)
    val dragonDiff: ArrayList<Int> = if (marauderOnly) dragonDiff2 else dragonDiff1
    val basePrecision = if (marauderOnly) 2172 else 1960

    val diff: Int = targetPrecision - basePrecision
    val n = berserkerDiff.size
    val swapIndices: ArrayList<Int> = ArrayList()

    for (i in 0..45 step 5) {
        calculate(if (diff < 0) dragonDiff else berserkerDiff, n, abs(diff - i), swapIndices)

        if (swapIndices.isNotEmpty()) {
            println("Infusions required: ${i / 5}")
            swapIndices.remove(-1)
            break
        }
    }

    if (swapIndices.isEmpty()) {
        println("No result.")
        return
    }

    val gearTypes = Gear.entries
    val secondaryStat = if (diff < 0) "Dragon" else "Assassin"

    for (i in 0 until n) {
        if (i !in swapIndices) continue
        println("${gearTypes[i].toString().padEnd(8, ' ')}: $secondaryStat")
    }
}

fun calculate(values: ArrayList<Int>, n: Int, sum: Int, result: ArrayList<Int>) {
    if (sum == 0) {
        result.add(-1)
        return
    }

    if (n == 0) {
        return
    }

    val value: Int = values[n - 1]

    if (value > sum) {
        calculate(values, n - 1, sum, result)
        return
    }

    calculate(values, n - 1, sum - value, result)

    if (result.isNotEmpty()) {
        result.add(n - 1)
        return
    }

    calculate(values, n - 1, sum, result)
}