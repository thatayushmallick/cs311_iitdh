package processor.pipeline;

import configuration.Configuration;
import generic.*;
import generic.Instruction.OperationType;
import processor.Clock;
import processor.Processor;

public class MemoryAccess implements Element {
    Processor containingProcessor;
    EX_MA_LatchType EX_MA_Latch;
    MA_RW_LatchType MA_RW_Latch;
    IF_OF_LatchType IF_OF_Latch;
    OF_EX_LatchType OF_EX_Latch;

    public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch,
            IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch) {
        this.containingProcessor = containingProcessor;
        this.EX_MA_Latch = eX_MA_Latch;
        this.MA_RW_Latch = mA_RW_Latch;
        this.IF_OF_Latch = iF_OF_Latch;
        this.OF_EX_Latch = oF_EX_Latch;
    }

    public void performMA() {
        // System.out.println("YessMA");
        if (EX_MA_Latch.isMA_enable()) {
            if (EX_MA_Latch.isMA_busy()) {
                // System.out.println(" is in IF Busy Stage");
                MA_RW_Latch.setRW_enable(false);
                return;
            }
            Instruction instruction = EX_MA_Latch.getInstruction();
            int ALU_output = EX_MA_Latch.getALU_result();
            MA_RW_Latch.setALU_Output(ALU_output);
            if (instruction == null) {
                // NOP
            } else {
                OperationType Operation_Type = instruction.getOperationType();
                switch (Operation_Type) {
                    case store:
                        int res_st = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
                        Simulator.getEventQueue().addEvent(new MemoryWriteEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency,this, containingProcessor.getMainMemory(), ALU_output, res_st));
                        EX_MA_Latch.setMA_busy(true);
                        MA_RW_Latch.setRW_enable(false);
                        break;
                    case load:
                        Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency,this, containingProcessor.getMainMemory(), ALU_output));
                        EX_MA_Latch.setMA_busy(true);
                        MA_RW_Latch.setRW_enable(false);
                        break;
                    default:
                        break;
                }
                MA_RW_Latch.setInstruction(instruction);
            }
        }
    }

    @Override
    public void handleEvent(Event e) {
        if (MA_RW_Latch.isRW_busy()) {
            e.setEventTime(Clock.getCurrentTime() + 1);
            Simulator.getEventQueue().addEvent(e);
        } else {
            boolean IF_OF_NOW = false;
            if(!IF_OF_NOW){
                MemoryResponseEvent event = (MemoryResponseEvent) e;

                int aluResult = EX_MA_Latch.getALU_result();

                Instruction inst = EX_MA_Latch.getInstruction();

                int ldResult = 0;

                ldResult = event.getValue();
                MA_RW_Latch.setInstruction(inst);
                MA_RW_Latch.setLoad_Output(ldResult);
                MA_RW_Latch.setALU_Output(aluResult);
                MA_RW_Latch.setRW_enable(true);

                IF_OF_Latch.setOF_enable(true);

                EX_MA_Latch.setMA_busy(false);
                
                Instruction nop = new Instruction();
                nop.setOperationType(OperationType.nop);
                EX_MA_Latch.setInstruction(nop);
            }
        }
    }

}
