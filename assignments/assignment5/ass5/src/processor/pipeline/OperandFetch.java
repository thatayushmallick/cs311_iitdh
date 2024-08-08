package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;
import generic.Statistics;
import processor.Processor;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OperandFetch {
    Processor containingProcessor;
    IF_EnableLatchType IF_EnableLatch;
    IF_OF_LatchType IF_OF_Latch;
    OF_EX_LatchType OF_EX_Latch;
    EX_MA_LatchType EX_MA_Latch;
    MA_RW_LatchType MA_RW_Latch;

    public OperandFetch(Processor containingProcessor,
            IF_OF_LatchType iF_OF_Latch,
            OF_EX_LatchType oF_EX_Latch,
            EX_MA_LatchType eX_MA_Latch,
            MA_RW_LatchType mA_RW_Latch,
            IF_EnableLatchType if_enableLatch) {
        this.containingProcessor = containingProcessor;
        this.IF_OF_Latch = iF_OF_Latch;
        this.OF_EX_Latch = oF_EX_Latch;
        this.EX_MA_Latch = eX_MA_Latch;
        this.MA_RW_Latch = mA_RW_Latch;
        this.IF_EnableLatch = if_enableLatch;
    }

    public static String twoComplement(String binaryString) {
        // Find the index of the first '1' from the right
        int index = binaryString.length() - 1;
        while (index >= 0 && binaryString.charAt(index) != '1') {
            index--;
        }
    
        // If no '1' is found, return the original string as it's already in two's complement form
        if (index == -1) {
            return binaryString;
        }
    
        StringBuilder result = new StringBuilder(binaryString.length());
    
        // Flip all bits after the rightmost '1'
        for (int i = 0; i < index; i++) {
            result.append(binaryString.charAt(i) == '0' ? '1' : '0');
        }
    
        // Append the bits before the rightmost '1'
        result.append(binaryString, index, binaryString.length());
    
        return result.toString();
      }

    public boolean CheckConflict(Instruction inst, Instruction toCheck) {
        if (toCheck != null &&
                !toCheck.getOperationType().toString().equals("nop")
                && !toCheck.getOperationType().toString().equals("end")
                && !inst.getOperationType().toString().equals("nop")
                && !inst.getOperationType().toString().equals("end")
                && !inst.getOperationType().toString().equals("jmp")
                && toCheck.getDestinationOperand() != null
                && (toCheck.getDestinationOperand().getValue() == inst.getSourceOperand1().getValue() ||
                        toCheck.getDestinationOperand().getValue() == inst.getSourceOperand2().getValue())) {
            /// System.out.println("!!!");
            return true;
        } else if (toCheck != null &&
                !toCheck.getOperationType().toString().equals("nop")
                && !toCheck.getOperationType().toString().equals("end")
                && !inst.getOperationType().toString().equals("nop")
                && !inst.getOperationType().toString().equals("end")
                && !inst.getOperationType().toString().equals("jmp")
                && toCheck.getDestinationOperand() != null
                && (toCheck.getDestinationOperand().getValue() == inst.getSourceOperand1().getValue() ||
                        toCheck.getDestinationOperand().getValue() == inst.getSourceOperand2().getValue())
                &&
                (inst.getOperationType().toString().equals("beq") ||
                        inst.getOperationType().toString().equals("bne") ||
                        inst.getOperationType().toString().equals("blt") ||
                        inst.getOperationType().toString().equals("bgt"))) {
            return true;
        } else {
            return false;
        }

    }

    public void performOF() {
        // System.out.println("YessO");
        // System.out.println(OF_EX_Latch.isEX_busy());
        if (EX_MA_Latch.isMA_busy()) {
            return;
        }
        if (OF_EX_Latch.isEX_busy()) {
            return;
        } else if (IF_OF_Latch.isOF_enable()) {
            int inst = IF_OF_Latch.getInstruction();
            if (inst == -1) {
                return;
            }
            String instb = Integer.toBinaryString(inst);
            if (instb.length() != 32) {
                int limm = instb.length();
                String lRepeated = "";
                if ((32 - limm) != 0) {
                    String s = "0";
                    int q = 32 - limm;
                    lRepeated = IntStream.range(0, q).mapToObj(i -> s).collect(Collectors.joining(""));
                }
                instb = lRepeated + instb;
            }
            String opcode = instb.substring(0, 5);
            int opcodei = Integer.parseInt(opcode, 2);
            OperationType[] operationType = OperationType.values();
            OperationType operation = operationType[opcodei];
            Instruction forwardInstruction = new Instruction();
            Operand rs1;
            int reg_no;
            Operand rs2;
            Operand rd;
            String cons;
            int cons_val;
            switch (operation) {
                case add:
                case sub:
                case mul:
                case div:
                case and:
                case or:
                case xor:
                case slt:
                case sll:
                case srl:
                case sra:
                    rs1 = new Operand();
                    rs1.setOperandType(OperandType.Register);
                    reg_no = Integer.parseInt(instb.substring(5, 10), 2);
                    rs1.setValue(reg_no);

                    rs2 = new Operand();
                    rs2.setOperandType(OperandType.Register);
                    reg_no = Integer.parseInt(instb.substring(10, 15), 2);
                    rs2.setValue(reg_no);

                    rd = new Operand();
                    rd.setOperandType(OperandType.Register);
                    reg_no = Integer.parseInt(instb.substring(15, 20), 2);
                    rd.setValue(reg_no);

                    forwardInstruction.setOperationType(operationType[opcodei]);
                    forwardInstruction.setSourceOperand1(rs1);
                    forwardInstruction.setSourceOperand2(rs2);
                    forwardInstruction.setDestinationOperand(rd);
                    break;
                
                case end:
                    forwardInstruction.setOperationType(operationType[opcodei]);
                    break;
                case jmp:
                    Operand op = new Operand();
                    cons = instb.substring(10, 32);
                    cons_val = Integer.parseInt(cons, 2);
                    if (cons.charAt(0) == '1') {
                        cons = twoComplement(cons);
                        cons_val = Integer.parseInt(cons, 2) * -1;
                    }
                    if (cons_val != 0) {
                        op.setOperandType(OperandType.Immediate);
                        op.setValue(cons_val);
                    } else {
                        reg_no = Integer.parseInt(instb.substring(5, 10), 2);
                        op.setOperandType(OperandType.Register);
                        op.setValue(reg_no);
                    }

                    forwardInstruction.setOperationType(operationType[opcodei]);
                    forwardInstruction.setDestinationOperand(op);
                    OperandType OPERNDTYPE = forwardInstruction.getDestinationOperand().getOperandType();
                    int immediate = 0;
                    if (OPERNDTYPE == OperandType.Register) {
                        immediate = containingProcessor.getRegisterFile().getValue(
                                forwardInstruction.getDestinationOperand().getValue());
                    } else {
                        immediate = forwardInstruction.getDestinationOperand().getValue();
                    }
                    int cPC = containingProcessor.getRegisterFile().programCounter - 1;
                    int output = immediate + cPC;
                    containingProcessor.getRegisterFile().setProgramCounter(output);
                    break;
                case beq:
                case bne:
                case blt:
                case bgt:
                    rs1 = new Operand();
                    rs1.setOperandType(OperandType.Register);
                    reg_no = Integer.parseInt(instb.substring(5, 10), 2);
                    rs1.setValue(reg_no);

                    // destination register
                    rs2 = new Operand();
                    rs2.setOperandType(OperandType.Register);
                    reg_no = Integer.parseInt(instb.substring(10, 15), 2);
                    rs2.setValue(reg_no);

                    // Immediate value
                    rd = new Operand();
                    rd.setOperandType(OperandType.Immediate);
                    cons = instb.substring(15, 32);
                    cons_val = Integer.parseInt(cons, 2);
                    if (cons.charAt(0) == '1') {
                        cons = twoComplement(cons);
                        cons_val = Integer.parseInt(cons, 2) * -1;
                    }
                    rd.setValue(cons_val);

                    forwardInstruction.setOperationType(operationType[opcodei]);
                    forwardInstruction.setSourceOperand1(rs1);
                    forwardInstruction.setSourceOperand2(rs2);
                    forwardInstruction.setDestinationOperand(rd);
                    break;
                case addi:
                case subi:
                case muli:
                case divi:
                case andi:
                case ori:
                case xori:
                case slti:
                case slli:
                case srli:
                case srai:
                case load:
                case store:
                    // Source register 1
                    rs1 = new Operand();
                    rs1.setOperandType(OperandType.Register);
                    reg_no = Integer.parseInt(instb.substring(5, 10), 2);
                    rs1.setValue(reg_no);

                    // Destination register
                    rd = new Operand();
                    rd.setOperandType(OperandType.Register);
                    reg_no = Integer.parseInt(instb.substring(10, 15), 2);
                    rd.setValue(reg_no);

                    // Immediate values
                    rs2 = new Operand();
                    rs2.setOperandType(OperandType.Immediate);
                    cons = instb.substring(15, 32);
                    cons_val = Integer.parseInt(cons, 2);
                    if (cons.charAt(0) == '1') {
                        cons = twoComplement(cons);
                        cons_val = Integer.parseInt(cons, 2) * -1;
                    }
                    rs2.setValue(cons_val);

                    forwardInstruction.setOperationType(operationType[opcodei]);
                    forwardInstruction.setSourceOperand1(rs1);
                    forwardInstruction.setSourceOperand2(rs2);
                    forwardInstruction.setDestinationOperand(rd);
                    break;
                    default:
                    break;
            }
            OF_EX_Latch.setEX_enable(true);
            EX_MA_Latch.setMA_enable(true);
            MA_RW_Latch.setRW_enable(true);

            Instruction nop = new Instruction();
            nop.setOperationType(OperationType.nop);
            boolean notConflict = false;
            // int tk=0;
            int freeze = 0;
            for(int i=0; i<1; i+=0) {
                if (EX_MA_Latch.getFlag() == 1) {
                    OF_EX_Latch.setInstruction(nop);
                    containingProcessor.getRegisterFile()
                            .setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter() - 1);
                    containingProcessor.setWrong_input(containingProcessor.getWrong_input() + 1);
                    EX_MA_Latch.setFlag(0);
                    Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
                    break;
                } else if (CheckConflict(forwardInstruction, MA_RW_Latch.instruction)
                        && MA_RW_Latch.instruction.getOperationType() != OperationType.nop) {
                    IF_EnableLatch.setIF_enable(false);
                    OF_EX_Latch.setInstruction(nop);
                    containingProcessor.setStalls(containingProcessor.getStalls() + 1);
                    Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
                    break;
                } else if (CheckConflict(forwardInstruction, OF_EX_Latch.instruction)
                        && OF_EX_Latch.instruction.getOperationType() != OperationType.nop) {
                    IF_EnableLatch.setIF_enable(false);
                    OF_EX_Latch.setInstruction(nop);
                    containingProcessor.setStalls(containingProcessor.getStalls() + 1);
                    Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
                    break;
                } else if (forwardInstruction.getOperationType() == OperationType.end) {
                    containingProcessor.getRegisterFile().setFreezed(true);
                    containingProcessor.getRegisterFile()
                            .setFreezedprogramCounter(containingProcessor.getRegisterFile().getProgramCounter());
                    containingProcessor.setFreezed_stalls(containingProcessor.getStalls());
                    containingProcessor.setFreezed_wrong_input(containingProcessor.getWrong_input());
                    Statistics.setFreezednumberOfInstructions(Statistics.getNumberOfInstructions());
                } else if (CheckConflict(forwardInstruction, EX_MA_Latch.instruction)
                        && EX_MA_Latch.instruction.getOperationType() != OperationType.nop) {
                    IF_EnableLatch.setIF_enable(false);
                    OF_EX_Latch.setInstruction(nop);
                    containingProcessor.setStalls(containingProcessor.getStalls() + 1);
                    Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
                    break;
                }
                notConflict = true;
                break;

            }
            if (notConflict && IF_OF_Latch.isOF_enable()) {
                IF_EnableLatch.setIF_enable(true);
                OF_EX_Latch.setInstruction(forwardInstruction);
            }
        }
    }

}
