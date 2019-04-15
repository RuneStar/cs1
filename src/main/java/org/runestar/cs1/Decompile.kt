package org.runestar.cs1

data class Condition(
        val instructions: IntArray,
        val comparison: Int,
        val comparisonValue: Int
)

fun decompile(conditions: List<Condition>, dst: StringBuilder) {
    require(conditions.isNotEmpty())
    val itr = conditions.iterator()
    decompile(itr.next(), dst)
    for (c in itr) {
        dst.append(" && ")
        decompile(c, dst)
    }
}

private fun decompile(condition: Condition, dst: StringBuilder) {
    val insns = condition.instructions
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
            4 -> s = "invcount(${insns[pc++]}:${insns[pc++]}, ${OBJ_NAMES.getValue(insns[pc++])})"
            5 -> s = "varp(${insns[pc++]})"
            6 -> s = "xp_for_lvl(${insns[pc++]})"
            7 -> s = "scale_varp(${insns[pc++]})"
            8 -> s = "combat_lvl"
            9 -> s = "total_lvl"
            10 -> s = "invcontains(${insns[pc++]}:${insns[pc++]}, ${OBJ_NAMES.getValue(insns[pc++])})"
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
                dst.append(' ').append(OPS[op]).append(' ')
            }
            dst.append(s)
            op = 0
        } else {
            op = nextOp
        }
    }
    dst.append(' ').append(COMPARISONS[condition.comparison - 1]).append(' ')
    dst.append(condition.comparisonValue.toString())
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

private val OBJ_NAMES = HashMap<Int, String>().apply {
    Condition::class.java.getResource("obj-names.txt").openStream().bufferedReader().forEachLine { line ->
        val split = line.split('\t')
        this[split[0].toInt()] = split[1]
    }
}