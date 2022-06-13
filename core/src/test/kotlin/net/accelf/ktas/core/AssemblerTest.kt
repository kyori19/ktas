package net.accelf.ktas.core

import kotlin.test.Test
import kotlin.test.assertContentEquals

internal class AssemblerTest {
    @Test
    fun assemble() {
        listOf(
            /**
             * addi r1, r0, 1
             * addi r2, r0, 2
             * add r3, r1, r2
             */
            assemble {
                val r1 = register()
                val r2 = register()
                addi(zero, 1, r1)
                addi(zero, 2, r2)
                val r3 = register()
                add(r1, r2, r3)
            } to listOf(
                "00000100000000010000000000000001",
                "00000100000000100000000000000010",
                "00000000001000100001100000000000",
            ),
            /**
             * addi r1, r0, 7
             * addi r2, r0, 0
             * loop : add r2, r2, r1
             * addi r1, r1, -1
             * bne r1, r0, loop
             */
            assemble {
                val i = register()
                addi(zero, 7, i)
                val ans = register()
                addi(zero, 0, ans)
                val loop = add(ans, i, ans)
                addi(i, -1, i)
                bne(zero, i, loop)
            } to listOf(
                "00000100000000010000000000000111",
                "00000100000000100000000000000000",
                "00000000010000010001000000000000",
                "00000100001000011111111111111111",
                "10000100001000001111111111111101",
            )
        ).forEach { (ops, expect) ->
            assertContentEquals(expect, ops.map(AssembleScope.Instruction::toString))
        }
    }
}
