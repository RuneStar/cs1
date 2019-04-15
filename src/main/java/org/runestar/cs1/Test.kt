package org.runestar.cs1

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Paths.get("input.txt")
    val output = Paths.get("scripts.txt")
    val sb = StringBuilder()
    val scripts = LinkedHashMap<String, MutableList<Condition>>()
    Files.lines(input).forEach { line ->
        val split = line.split('\t')
        val widget = "${split[0]}:${split[1]}"
        val cmp = split[3].toInt()
        val cmpValue = split[4].toInt()
        val insns = split.subList(5, split.size).map { it.toInt() }.toIntArray()
        val cond = Condition(insns, cmp, cmpValue)
        if (widget in scripts) {
            scripts.getValue(widget).add(cond)
        } else {
            scripts[widget] = arrayListOf(cond)
        }
    }
    for ((w, cs) in scripts) {
        sb.append(w).append(" = ")
        decompile(cs, sb)
        sb.append('\n')
    }
    Files.write(output, sb.toString().toByteArray())
}