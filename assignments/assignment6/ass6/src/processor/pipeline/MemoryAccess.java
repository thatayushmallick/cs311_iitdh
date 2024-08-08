package processor.pipeline;

import generic.*;
import generic.Instruction.OperationType;
import processor.Clock;
import processor.Processor;
import processor.memorysystem.Cache;
import generic.Event.EventType;

public class MemoryAccess implements Element{
    Processor containingProcessor;
    EX_MA_LatchType EX_MA_Latch;
    MA_RW_LatchType MA_RW_Latch;
    IF_OF_LatchType IF_OF_Latch;
    OF_EX_LatchType OF_EX_Latch;

    Cache cache;

    public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, Cache cache) {
        this.containingProcessor = containingProcessor;
        this.EX_MA_Latch = eX_MA_Latch;
        this.MA_RW_Latch = mA_RW_Latch;
        this.IF_OF_Latch = iF_OF_Latch;
        this.OF_EX_Latch = oF_EX_Latch;
        this.cache = cache;
    }

    public void performMA() {
        if(EX_MA_Latch.isMA_enable() && !EX_MA_Latch.isMA_busy()) {
            if(EX_MA_Latch.isNop) {
                MA_RW_Latch.isNop = true;
            }
            else {
                Instruction instruction = EX_MA_Latch.getInstruction();
                int ALU_output = EX_MA_Latch.getALU_result();
                MA_RW_Latch.setALU_Output(ALU_output);
                MA_RW_Latch.setInstruction(instruction);
                MA_RW_Latch.insPC = EX_MA_Latch.insPC;
                MA_RW_Latch.isNop = false;
                if (instruction == null) {
                }
                else {
                    OperationType Operation_Type = instruction.getOperationType();
                    if (Operation_Type.toString().equals("store")) {
                        int ins_cout = EX_MA_Latch.getcou();
                        int SOP = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
                        EX_MA_Latch.setcou(ins_cout+1);
                        EX_MA_Latch.setMA_busy(true);
                        int cy = EX_MA_Latch.getin();
                        Simulator.storeresp = Clock.getCurrentTime();
                        EX_MA_Latch.setins(cy+1);
                        Simulator.getEventQueue().addEvent(new MemoryWriteEvent(Clock.getCurrentTime() + this.cache.latency,this,this.cache,ALU_output,SOP));
                        EX_MA_Latch.setMA_enable(false);
                    } else if (Operation_Type.toString().equals("load")) {
                        EX_MA_Latch.setMA_busy(true);
                        OF_EX_Latch.setcou(OF_EX_Latch.getcou()-1);
                        Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime(),this,this.cache, ALU_output));
                        OF_EX_Latch.setins(OF_EX_Latch.getin()-1);
                        EX_MA_Latch.setMA_enable(false);
                    }
                    MA_RW_Latch.setInstruction(instruction);
                }
            }
            EX_MA_Latch.setMA_enable(false);
            if(EX_MA_Latch.getInstruction().getOperationType().toString().equals("end")) {
                OF_EX_Latch.setcou(OF_EX_Latch.getcou()+1);
                EX_MA_Latch.setMA_enable(false);
            }
            MA_RW_Latch.setRW_enable(true);
        }
    }

    @Override
    public void handleEvent(Event e) {
        if(e.getEventType() == EventType.MemoryResponse) {
            MemoryResponseEvent event = (MemoryResponseEvent) e ;
            MA_RW_Latch.setALU_Output(event.getValue());
            MA_RW_Latch.insPC = EX_MA_Latch.insPC;
            MA_RW_Latch.setRW_enable(true);
            EX_MA_Latch.setMA_busy(false);
        }
        else {
            EX_MA_Latch.setMA_busy(false);
        }
    }
}
