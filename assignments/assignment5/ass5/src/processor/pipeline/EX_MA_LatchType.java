package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {

    boolean MA_enable;
    int alu_result;
    Instruction instruction;
    int flag;
    boolean MA_busy;
    int count;
    int cycle;

    public void setCount(int c){
        count = c;
    }

    public int getCount(){
        return count;
    }

    public void setCycle(int cy){
        cycle = cy;
    }

    public int getCycle(){
        return cycle;
    }

    public EX_MA_LatchType() {
        Instruction nop = new Instruction();
        nop.setOperationType(Instruction.OperationType.nop);
        MA_enable = true;
        instruction = nop;
        flag = 0;
        MA_busy = false;
    }

    public int getALU_result() {
        return alu_result;
    }

    public void setMA_busy(boolean MA_busy) {
        this.MA_busy = MA_busy;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public int getFlag() {
        return flag;
    }

    public boolean isMA_enable() {
        return MA_enable;
    }

    public void setALU_result(int result) {
        alu_result = result;
    }

    public void setMA_enable(boolean mA_enable) {
        MA_enable = mA_enable;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public boolean isMA_busy() {
        return MA_busy;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}
