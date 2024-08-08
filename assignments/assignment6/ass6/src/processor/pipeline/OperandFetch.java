package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;
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


    public OperandFetch(Processor containingProcessor,IF_OF_LatchType iF_OF_Latch,OF_EX_LatchType oF_EX_Latch,EX_MA_LatchType eX_MA_Latch,MA_RW_LatchType mA_RW_Latch,IF_EnableLatchType if_enableLatch) {
        this.containingProcessor = containingProcessor;
        this.IF_OF_Latch = iF_OF_Latch;
        this.OF_EX_Latch = oF_EX_Latch;
        this.EX_MA_Latch = eX_MA_Latch;
        this.MA_RW_Latch = mA_RW_Latch;
        this.IF_EnableLatch = if_enableLatch;
    }

    public static char opposite(char c) {
        return (c == '0') ? '1' : '0';
    }

    public static String twosComplement(String binary) {
        String twos = "", ones = "";
        for (int i = 0; i < binary.length(); i++) {
            ones += opposite(binary.charAt(i));
        }

        StringBuilder builder = new StringBuilder(ones);
        boolean flag = false;
        for (int i = ones.length() - 1; i > 0; i--) {
            if (ones.charAt(i) == '1') {
                builder.setCharAt(i, '0');
            } else {
                builder.setCharAt(i, '1');
                flag = true;
                break;
            }
        }
        if (!flag) {
            builder.append("1", 0, 7);
        }
        twos = builder.toString();
        return twos;
    }

    public void performOF() {
        if(OF_EX_Latch.isEX_busy()) IF_OF_Latch.setOF_busy(true);
        else IF_OF_Latch.setOF_busy(false);
        if(IF_OF_Latch.isOF_enable() && !OF_EX_Latch.isEX_busy())
        {
            int inst = IF_OF_Latch.getInstruction();
            OperationType[] operationType = OperationType.values();
            String instb = Integer.toBinaryString(inst);
            instb = String.format("%32s", instb).replace(' ', '0');
            String opcode = instb.substring(0, 5);
            int opcodei = Integer.parseInt(opcode, 2);
            OperationType operation = operationType[opcodei];
            Instruction instn = new Instruction();
            Operand rs1;
            int reg_no;
            Operand rs2;
            Operand rd;
            String cons;
            int cons_val;
            if(operation.toString().equals("add")||operation.toString().equals("sub")||operation.toString().equals("mul")||operation.toString().equals("div")||operation.toString().equals("and")||operation.toString().equals("or")||operation.toString().equals("xor")||operation.toString().equals("slt")||operation.toString().equals("sll")||operation.toString().equals("srl")||operation.toString().equals("sra")){
                System.out.println("THIS IS : " + operation.toString());
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

                instn.setOperationType(operationType[opcodei]);
                instn.setSourceOperand1(rs1);
                instn.setSourceOperand2(rs2);
                instn.setDestinationOperand(rd);
            }else if(operation.toString().equals("end")){
                instn.setOperationType(operationType[opcodei]);
            }else if(operation.toString().equals("jmp")){
                Operand op = new Operand();
                cons = instb.substring(10, 32);
                cons_val = Integer.parseInt(cons, 2);
                if (cons.charAt(0) == '1') {
                    cons = twosComplement(cons);
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

                instn.setOperationType(operationType[opcodei]);
                instn.setDestinationOperand(op);
            }else if(operation.toString().equals("beq")||operation.toString().equals("bgt")||operation.toString().equals("bne")||operation.toString().equals("blt")||operation.toString().equals("store")){
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
                    cons = twosComplement(cons);
                    cons_val = Integer.parseInt(cons, 2) * -1;
                }
                rd.setValue(cons_val);

                instn.setOperationType(operationType[opcodei]);
                instn.setSourceOperand1(rs1);
                instn.setSourceOperand2(rs2);
                instn.setDestinationOperand(rd);
            }else if(operation.toString().equals("addi")||operation.toString().equals("subi")||operation.toString().equals("muli")||operation.toString().equals("divi")||operation.toString().equals("andi")||operation.toString().equals("ori")||operation.toString().equals("xori")||operation.toString().equals("slti")||operation.toString().equals("slli")||operation.toString().equals("srli")||operation.toString().equals("srai")||operation.toString().equals("load")){
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
                    cons = twosComplement(cons);
                    cons_val = Integer.parseInt(cons, 2) * -1;
                }
                rs2.setValue(cons_val);

                instn.setOperationType(operationType[opcodei]);
                instn.setSourceOperand1(rs1);
                instn.setSourceOperand2(rs2);
                instn.setDestinationOperand(rd);
            }else{
                instn.setOperationType(OperationType.nop);
            }

            OF_EX_Latch.isNop = false;
            int n = IF_OF_Latch.getin();
            OF_EX_Latch.setInstruction(instn);
            IF_OF_Latch.setcou(n+1);
            OF_EX_Latch.insPC = IF_OF_Latch.insPC;
            int c = IF_OF_Latch.getcou();
            OF_EX_Latch.setEX_enable(true);
            IF_OF_Latch.setins(c+1);
            IF_EnableLatch.setIF_enable(true);

            if(operation.toString().equals("end")) {
                IF_OF_Latch.setOF_enable(false);
                IF_EnableLatch.setIF_enable(false);
            }
            IF_OF_Latch.setOF_enable(false);
            OF_EX_Latch.setEX_enable(true);
        }
    }

}
