package org.runestar.cs1

import java.lang.invoke.MethodHandles

fun decompile(scripts: List<Script>, appendable: Appendable) {
    require(scripts.isNotEmpty())
    decompile(scripts[0], appendable)
    for (s in scripts.subList(1, scripts.size)) {
        appendable.append(" && ")
        decompile(s, appendable)
    }
}

private fun decompile(script: Script, appendable: Appendable) {
    val insns = script.instructions
    var first = true
    var pc = 0
    var op = 0
    out@
    while (true) {
        var nextOp = 0
        lateinit var s: String
        when (insns[pc++]) {
            0 -> break@out
            1 -> s = "stat(${STAT_NAMES[insns[pc++]]})"
            2 -> s = "stat_base(${STAT_NAMES[insns[pc++]]})"
            3 -> s = "stat_xp(${STAT_NAMES[insns[pc++]]})"
            4 -> s = "invcount(${insns[pc++]}:${insns[pc++]}, ${OBJ_NAMES.getValue(insns[pc++]++)})"
            5 -> s = "varp(${insns[pc++]})"
            6 -> s = "xp_for_lvl(${insns[pc++]})"
            7 -> s = "scale_varp(${insns[pc++]})"
            8 -> s = "combat_lvl"
            9 -> s = "total_lvl"
            10 -> s = "invcontains(${insns[pc++]}:${insns[pc++]}, ${OBJ_NAMES.getValue(insns[pc++]++)})"
            11 -> s = "runenergy_visible"
            12 -> s = "runweight_visible"
            13 -> s = "testbit_varp(${insns[pc++]}, ${insns[pc++]})"
            14 -> s = "varbit(${insns[pc++]})"
            15 -> nextOp = 1
            16 -> nextOp = 2
            17 -> nextOp = 3
            18 -> s = "coordx"
            19 -> s = "coordy"
            20 -> s = insns[pc++].toString()
        }
        if (nextOp == 0) {
            if (first) {
                first = false
            } else {
                appendable.append(' ').append(OPS[op]).append(' ')
            }
            appendable.append(s)
            op = 0
        } else {
            op = nextOp
        }
    }
    appendable.append(' ').append(COMPARISONS[script.comparison - 1]).append(' ')
    appendable.append(script.comparisonValue.toString())
}

private val COMPARISONS = arrayOf("==", "<", ">", "!=")

private val OPS = charArrayOf('+', '-', '/', '*')

private val STAT_NAMES = arrayOf(
        "attack",
        "defence",
        "strength",
        "hitpoints",
        "ranged",
        "prayer",
        "magic",
        "cooking",
        "woodcutting",
        "fletching",
        "fishing",
        "firemaking",
        "crafting",
        "smithing",
        "mining",
        "herblore",
        "agility",
        "thieving",
        "slayer",
        "farming",
        "runecraft",
        "hunter",
        "construction"
)

private val OBJ_NAMES = run {
    val map = HashMap<Int, String>()
    MethodHandles.lookup().lookupClass().getResource("obj-names.txt").openStream().bufferedReader().use { input ->
        input.lines().forEach { line ->
            val split = line.split('\t')
            map[split[0].toInt()] = split[1]
        }
    }
    map
}