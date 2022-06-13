package net.accelf.ktas.core

@Suppress("Unused", "MemberVisibilityCanBePrivate")
class AssembleScope {
    private val usedRegisters = mutableSetOf<Int>()
    val instructions: List<Instruction>
        get() = _instructions
    private val _instructions = mutableListOf<Instruction>()

    fun register() = register((usedRegisters.maxOrNull() ?: 0) + 1)
    fun register(address: Int): Register {
        if (!usedRegisters.add(address)) {
            error("register address $address already used")
        }
        return Register(address)
    }
    val zero = register(0)

    val add = aluOp(0)
    val sub = aluOp(2)
    val and = aluOp(8)
    val or = aluOp(9)
    val xor = aluOp(10)
    val nor = aluOp(11)

    val sll = shiftOp(16)
    val srl = shiftOp(17)
    val sra = shiftOp(18)

    val addi = immOp(1)
    val andi = immOp(4)
    val ori = immOp(5)
    val xori = immOp(6)

    fun lui(imm: Int, dst: Register) = immOp(3)(zero, imm, dst)

    val lw = dplOp(16)
    val lh = dplOp(18)
    val lb = dplOp(20)
    val sw = dplOp(24)
    val sh = dplOp(26)
    val sb = dplOp(28)

    val beq = branchOp(32)
    val bne = branchOp(33)
    val blt = branchOp(34)
    val ble = branchOp(35)

    val j = jmpOp(40)
    val jal = jmpOp(41)

    fun jr(src: Register) = instruction {
        op(42)
        r(0)
        src.append()
        r(0)
        aux(0)
    }

    private fun aluOp(aux: Int) = { src1: Register, src2: Register, dst: Register ->
        instruction {
            op(0)
            src1.append()
            src2.append()
            dst.append()
            aux(aux)
        }
    }

    private fun shiftOp(aux: Int) = { src: Register, dst: Register ->
        aluOp(aux)(src, zero, dst)
    }

    private fun immOp(opc: Int) = { src: Register, imm: Int, dst: Register ->
        instruction {
            op(opc)
            src.append()
            dst.append()
            imm(imm)
        }
    }

    private fun dplOp(opc: Int) = { dpl: Int, base: Int, dst: Register ->
        instruction {
            op(opc)
            dst.append()
            r(base)
            appendBinary(dpl, 16)
        }
    }

    private fun branchOp(opc: Int) = { src1: Register, src2: Register, label: Label ->
        instruction {
            op(opc)
            src2.append()
            src1.append()
            appendBinary(label.index - instructions.size - 1, 16)
        }
    }

    private fun jmpOp(aux: Int) = { label: Label ->
        instruction {
            op(aux)
            label.append()
        }
    }

    private fun instruction(builder: Instruction.() -> Unit): Label = Label()
            .also { _instructions.add(Instruction().also(builder)) }

    class Instruction {
        private val builder = StringBuilder()

        internal fun op(opc: Int) = appendBinary(opc, 6)
        internal fun r(address: Int) = appendBinary(address, 5)
        internal fun aux(aux: Int) = appendBinary(aux, 11)
        internal fun imm(imm: Int) = appendBinary(imm, 16)
        internal fun appendBinary(num: Int, digits: Int) = builder.append(num.toBinary(digits))

        internal fun Register.append() = r(address)
        internal fun Label.append() = appendBinary(index, 26)

        override fun toString() = builder.toString()
    }

    inner class Register internal constructor(internal val address: Int)
    inner class Label internal constructor(internal val index: Int = instructions.size)
}

fun assemble(op: AssembleScope.() -> Unit) = AssembleScope().also(op).instructions
fun Iterable<AssembleScope.Instruction>.print() = println(joinToString("\n"))
