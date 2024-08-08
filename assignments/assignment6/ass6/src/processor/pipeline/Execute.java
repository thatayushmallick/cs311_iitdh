package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import processor.Processor;

import java.util.Arrays;

public class Execute
 {
    Processor containingProcessor;
    OF_EX_LatchType OF_EX_Latch;
    EX_MA_LatchType EX_MA_Latch;
    EX_IF_LatchType EX_IF_Latch;
    IF_OF_LatchType IF_OF_Latch;
    MA_RW_LatchType MA_RW_Latch;
    IF_EnableLatchType IF_EnableLatch;

    public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch,EX_IF_LatchType eX_IF_Latch, IF_OF_LatchType iF_OF_Latch, MA_RW_LatchType mA_RW_Latch,IF_EnableLatchType iF_EnableLatch) {
        this.containingProcessor = containingProcessor;
        this.OF_EX_Latch = oF_EX_Latch;
        this.EX_MA_Latch = eX_MA_Latch;
        this.EX_IF_Latch = eX_IF_Latch;
        this.IF_OF_Latch = iF_OF_Latch;
        this.MA_RW_Latch = mA_RW_Latch;
        this.IF_EnableLatch = iF_EnableLatch;
    }

    public void performEX() {
        if (EX_MA_Latch.isMA_busy())
            OF_EX_Latch.setEX_busy(true);
        else
            OF_EX_Latch.setEX_busy(false);
        if (OF_EX_Latch.isEX_enable() && !EX_MA_Latch.isMA_busy()) {
            boolean branchDetect = false;
            if (OF_EX_Latch.isNop) {
                EX_MA_Latch.isNop = true;
            } else {
                EX_MA_Latch.isNop = false;
                Instruction cmd = OF_EX_Latch.getInstruction();
                EX_MA_Latch.setInstruction(cmd);
                OperationType cmd_op = cmd.getOperationType();
                int cmd_op_opcode = Arrays.asList(OperationType.values()).indexOf(cmd_op);
                int currentPC = containingProcessor.getRegisterFile().getProgramCounter() - 1;
                String opType = cmd_op.toString();
                int ALU_output = 0;
                // boolean b = opType.equals("addi") || opType.equals("subi") || opType.equals("muli") || opType.equals("divi")
                // || opType.equals("andi") || opType.equals("ori") || opType.equals("xori") || opType.equals("slti")
                // || opType.equals("slli") || opType.equals("srli") || opType.equals("srai") || opType.equals("load");
                // int op1 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand1().getValue());
				// int op2 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand2().getValue());
                switch (cmd_op) {
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
                        int op1 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand1().getValue());
                        int op2 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand2().getValue());
                        if(opType.equals("add")){ALU_output = op1 + op2;}
                        else if(opType.equals("sub")){ALU_output = op1 - op2;}
                        else if(opType.equals("mul")){ALU_output = op1 * op2;}
                        else if(opType.equals("div"))
                        {   
                            ALU_output = op1 / op2; 
                            containingProcessor.getRegisterFile().setValue(31, op1%op2);
                        }
                        else if(opType.equals("and")){ALU_output = op1 & op2;}
                        else if(opType.equals("or")){ALU_output = op1 | op2;}
                        else if(opType.equals("xor")){ALU_output = op1 ^ op2;}
                        else if(opType.equals("slt"))
                        {
                            if (op1 < op2)
							    ALU_output = 1;
						    else
							    ALU_output = 0;
                        }
                        else if(opType.equals("sll")){ALU_output = op1 << op2;}
                        else if(opType.equals("srl")){ALU_output = op1 >>> op2;}
                        else if(opType.equals("sra")){ALU_output = op1 >> op2;}
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
                        int i = cmd.getSourceOperand1().getValue();
                        op1 = containingProcessor.getRegisterFile().getValue(i);
                        op2 = cmd.getSourceOperand2().getValue();
                        if(opType.equals("addi")){ALU_output = op1 + op2;}
                        else if(opType.equals("subi")){ALU_output = op1 - op2;}
                        else if(opType.equals("muli")){ALU_output = op1 * op2;}
                        else if(opType.equals("divi"))
                        {
                            ALU_output = op1 / op2; 
                            containingProcessor.getRegisterFile().setValue(31, op1%op2);
                        }
                        else if(opType.equals("andi")){ALU_output = op1 & op2;}
                        else if(opType.equals("ori")){ALU_output = op1 | op2;}
                        else if(opType.equals("xori")){ALU_output = op1 ^ op2;}
                        else if(opType.equals("slti"))
                        {
                            if (op1 < op2)
							    ALU_output = 1;
						    else
							    ALU_output = 0;
                        }
                        else if(opType.equals("slli")){ALU_output = op1 << op2;}
                        else if(opType.equals("srli")){ALU_output = op1 >>> op2;}
                        else if(opType.equals("srai")){ALU_output = op1 >> op2;}
                        else if(opType.equals("load")){ALU_output = op1 + op2;}
                        break;
                    case store:
                        int SOP1 = cmd.getDestinationOperand().getValue();
                        int SOP2 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand2().getValue());
                        ALU_output = SOP1 + SOP2;
                    case beq:
                    case bne:
                    case bgt:
                    case blt:
                        SOP1 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand1().getValue());
                        SOP2 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand2().getValue());
                        int immediate = cmd.getDestinationOperand().getValue();
                        if(opType.equals("beq"))
                        {
                            if (SOP1 == SOP2) {
                                ALU_output = immediate;
                                branchDetect = true;
                            }
                        }
                        else if(opType.equals("bne"))
                        {
                            if (SOP1 != SOP2) {
                                ALU_output = immediate;
                                branchDetect = true;
                            }
                        }
                        else if(opType.equals("bgt"))
                        {
                            if (SOP1 > SOP2) {
                                ALU_output = immediate;
                                branchDetect = true;
                            }
                        }
                        else if(opType.equals("blt"))
                        {
                            if (SOP1 < SOP2) {
                                ALU_output = immediate;
                                branchDetect = true;
                            }
                        }
                        break;
                    case jmp:
                        Operand.OperandType OPERNDTYPE = cmd.getDestinationOperand().getOperandType();
                        immediate = 0;
                        if (OPERNDTYPE == Operand.OperandType.Register) {
                            immediate = containingProcessor.getRegisterFile().getValue(cmd.getDestinationOperand().getValue());
                        } else {
                            immediate = cmd.getDestinationOperand().getValue();
                        }
                        ALU_output = immediate;
                        branchDetect = true;
                        break;
                    default:
                        break;
                }
                if (branchDetect) {
                    EX_IF_Latch.isBranchTaken = true;
                    int a = OF_EX_Latch.getcou();
                    EX_IF_Latch.offset = ALU_output - 1;
                    OF_EX_Latch.setcou(a+1);
                    IF_EnableLatch.setIF_enable(true);
                    int c = OF_EX_Latch.getin();
                    OF_EX_Latch.setEX_enable(false);
                    OF_EX_Latch.setins(c+1);
                    IF_OF_Latch.setOF_enable(false);
                    //nop time
                    Instruction nop = new Instruction();
                    nop.setOperationType(OperationType.nop);
                    OF_EX_Latch.setInstruction(nop);
                }
                EX_MA_Latch.setALU_result(ALU_output);
                EX_MA_Latch.insPC = OF_EX_Latch.insPC;
                if (OF_EX_Latch.getInstruction().getOperationType().toString().equals("end")) {
                    OF_EX_Latch.setEX_enable(false);
                }
            }
            OF_EX_Latch.setEX_enable(false);
            EX_MA_Latch.setMA_enable(true);
        }
    }
}
