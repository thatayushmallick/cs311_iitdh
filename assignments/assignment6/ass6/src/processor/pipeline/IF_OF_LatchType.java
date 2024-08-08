package processor.pipeline;

public class IF_OF_LatchType {

    int insPC;
    boolean OF_enable;
    int instruction;
    boolean OF_busy;
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

    public int getInstruction() {
        return instruction;
    }

    public IF_OF_LatchType() {
        OF_enable = false;
        instruction = -1999;
        OF_busy = false;
        insPC = -1;
    }

    public void setInsPC(int insPC) {
        this.insPC = insPC;
    }

    public void setOF_busy(boolean oF_busy) {
        OF_busy = oF_busy;
    }

    public void setInstruction(int instruction) {
        this.instruction = instruction;
    }

    public boolean isOF_enable() {
        return OF_enable;
    }

    public int getInsPC() {
        return insPC;
    }

    public void setOF_enable(boolean oF_enable) {
        OF_enable = oF_enable;
    }

    public boolean isOF_busy() {
        return OF_busy;
    }

}
