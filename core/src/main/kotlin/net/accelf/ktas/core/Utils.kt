package net.accelf.ktas.core

import kotlin.math.pow

fun Int.toBinary(length: Int): String =
        (if (this < 0) {
            this + 2.toFloat().pow(length).toInt()
        } else this)
                .toString(2).padStart(length, '0')
                .takeIf { it.length == length && !it.contains('-') }
                ?: error("invalid number $this passed for length $length")
