package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
    Processor containingProcessor;
    MA_RW_LatchType MA_RW_Latch;
    IF_EnableLatchType IF_EnableLatch;
    IF_OF_LatchType IF_OF_Latch;
    OF_EX_LatchType OF_EX_Latch;
    EX_MA_LatchType EX_MA_Latch;

    public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType ifOfLatchType, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch) {
        this.containingProcessor = containingProcessor;
        this.MA_RW_Latch = mA_RW_Latch;
        this.IF_EnableLatch = iF_EnableLatch;
        this.IF_OF_Latch = ifOfLatchType;
        this.OF_EX_Latch = oF_EX_Latch;
        this.EX_MA_Latch = eX_MA_Latch;
    }

    public void performRW() {
        if (MA_RW_Latch.isRW_enable()) {
            if (!MA_RW_Latch.isNop) {
                Instruction cmd = MA_RW_Latch.getInstruction();

                int alu_output = MA_RW_Latch.getALU_Output();
                OperationType operationType = cmd.getOperationType();
                if(operationType.toString().equals("store")||operationType.toString().equals("jmp")||operationType.toString().equals("beq")||operationType.toString().equals("bne")||operationType.toString().equals("blt")||operationType.toString().equals("bgt")||operationType.toString().equals("nop"))
                {
                    ;
                }else if(operationType.toString().equals("load"))
                {
                    int DOP = cmd.getDestinationOperand().getValue();
                    containingProcessor.getRegisterFile().setValue(DOP, alu_output);
                }else if(operationType.toString().equals("end"))
                {
                    Simulator.setSimulationComplete(true);
                        IF_EnableLatch.setIF_enable(false);
                }else{
                    int DOP = cmd.getDestinationOperand().getValue();
                    containingProcessor.getRegisterFile().setValue(DOP, alu_output);
                }
                MA_RW_Latch.setRW_enable(false);
            }
        }
    }

}
