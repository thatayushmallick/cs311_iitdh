package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {

    public int insPC;
    boolean MA_enable;
    int alu_result;
    Instruction instruction;
    int flag;
    boolean MA_busy;
    boolean isNop;
    int count;
    int nins;
    public void setcou(int c){
        count = c;
    }

    public int getcou(){
        return count;
    }

    public void setins(int s){
        nins = s;
    }

    public int getin(){
        return nins;
    }

    public EX_MA_LatchType() {
        Instruction nop = new Instruction();
        nop.setOperationType(Instruction.OperationType.nop);
        MA_enable = false;
        instruction = nop;
        flag = 0;
        MA_busy = false;
        isNop = false;
        insPC = -1;
    }

    public boolean isMA_busy() {
        return MA_busy;
    }

    public int getALU_result() {
        return alu_result;
    }

    public void setMA_busy(boolean MA_busy) {
        this.MA_busy = MA_busy;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setALU_result(int result) {
        alu_result = result;
    }

    public boolean isMA_enable() {
        return MA_enable;
    }

    public void setMA_enable(boolean mA_enable) {
        MA_enable = mA_enable;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

}
