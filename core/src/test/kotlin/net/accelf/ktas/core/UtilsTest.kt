package net.accelf.ktas.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class UtilsTest {
    @Test
    fun toBinary() {
        assertEquals("0101", 5.toBinary(4))
        assertEquals("0001", 1.toBinary(4))
        assertEquals("0000", 0.toBinary(4))
        assertEquals("1111", (-1).toBinary(4))
        assertEquals("1100", (-4).toBinary(4))

        assertFails { println(16.toBinary(4)) }
        assertFails { println((-17).toBinary(4)) }
    }
}
