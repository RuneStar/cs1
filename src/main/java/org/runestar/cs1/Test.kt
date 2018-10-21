package org.runestar.cs1

import java.lang.StringBuilder
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    val input = Paths.get("input.txt")
    val output = Paths.get("scripts.txt")
    val sb = StringBuilder()
    val scripts = LinkedHashMap<String, MutableList<Script>>()
    Files.lines(input).forEach { line ->
        val split = line.split('\t')
        val widget = "${split[0]}:${split[1]}"
        val cmp = split[3].toInt()
        val cmpValue = split[4].toInt()
        val insns = split.subList(5, split.size).map { it.toInt() }.toIntArray()
        val script = Script(cmp, cmpValue, insns)
        if (widget in scripts) {
            scripts.getValue(widget).add(script)
        } else {
            scripts[widget] = arrayListOf(script)
        }
    }
    for ((w, ss) in scripts) {
        sb.append(w).append(" = ")
        decompile(ss, sb)
        sb.append('\n')
    }
    Files.write(output, sb.toString().toByteArray())
}