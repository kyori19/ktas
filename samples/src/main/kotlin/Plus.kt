import net.accelf.ktas.core.assemble
import net.accelf.ktas.core.print

fun main() {
    assemble {
        val r1 = register()
        val r2 = register()
        addi(zero, 1, r1)
        addi(zero, 2, r2)
        val r3 = register()
        add(r1, r2, r3)
    }
            .print()
}
