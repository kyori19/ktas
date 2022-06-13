import net.accelf.ktas.core.assemble
import net.accelf.ktas.core.print

fun main() {
    assemble {
        val i = register()
        addi(zero, 7, i) // var i = 7

        val ans = register()
        addi(zero, 0, ans) // var ans = 0

        val loop = add(ans, i, ans) // ans = ans + i (labeled as loop)
        addi(i, -1, i) // i = i - 1

        bne(zero, i, loop) // jump to loop if i != 0
    }
            .print()
}
