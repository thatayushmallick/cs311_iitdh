package processor.pipeline;

import generic.*;
import processor.Clock;
import processor.Processor;
import processor.memorysystem.Cache;

public class InstructionFetch implements Element {

    Processor containingProcessor;
    IF_EnableLatchType IF_EnableLatch;
    IF_OF_LatchType IF_OF_Latch;
    EX_IF_LatchType EX_IF_Latch;

    Cache cache;
    int currentPC;

    public InstructionFetch(Processor containingProcessor,IF_EnableLatchType iF_EnableLatch,IF_OF_LatchType iF_OF_Latch,EX_IF_LatchType eX_IF_Latch,Cache cache) {
        this.containingProcessor = containingProcessor;
        this.IF_EnableLatch = iF_EnableLatch;
        this.IF_OF_Latch = iF_OF_Latch;
        this.EX_IF_Latch = eX_IF_Latch;
        this.cache = cache;
    }

    @Override

    public void handleEvent(Event e) {
        if(IF_OF_Latch.isOF_busy()) {
            e.setEventTime(Clock.getCurrentTime() + 1);
            Simulator.getEventQueue().addEvent(e);
        }
        else {
            MemoryResponseEvent event = (MemoryResponseEvent) e ;
            if(!EX_IF_Latch.isBranchTaken)	{
                IF_OF_Latch.setInstruction(event.getValue());
            }
            else {
                IF_OF_Latch.setInstruction(0);
            }
            IF_OF_Latch.insPC = this.currentPC;
            containingProcessor.getRegisterFile().setProgramCounter(this.currentPC + 1);
            IF_OF_Latch.setOF_enable(true);
            IF_EnableLatch.setIF_busy(false);

        }
    }

    public void performIF() {
        if(IF_EnableLatch.isIF_enable()) {
            if(IF_EnableLatch.isIF_busy()) return;
            currentPC = containingProcessor.getRegisterFile().getProgramCounter();
            if(EX_IF_Latch.isBranchTaken) {
                currentPC = currentPC + EX_IF_Latch.offset - 1;
                EX_IF_Latch.isBranchTaken = false;
                IF_EnableLatch.setIns(currentPC);
                int count = IF_EnableLatch.getPCount();
            }
            IF_EnableLatch.setIns(0);
            int ins_N = IF_EnableLatch.getIns();
            IF_EnableLatch.setIns(ins_N+1);
            int CycleCount = IF_EnableLatch.getPCount();
            IF_EnableLatch.setPCount(CycleCount+1);
            Simulator.ins_count++;
            Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime()+this.cache.latency,this,this.cache,currentPC));
            IF_EnableLatch.setIF_busy(true);
        }

    }
}
