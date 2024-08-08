package processor.pipeline;

public class IF_OF_LatchType {

    boolean OF_enable;
    int instruction;
    boolean OF_busy;
    int counter;
    boolean check;

    public boolean isOF_busy() {
        return OF_busy;
    }

    public boolean isOF_enable() {
        return OF_enable;
    }

    public int getInstruction() {
        return instruction;
    }

    public void setOF_enable(boolean oF_enable) {
        OF_enable = oF_enable;
    }

    public void setInstruction(int instruction) {
        this.instruction = instruction;
    }

    public void setOF_busy(boolean oF_busy) {
        OF_busy = oF_busy;
    }

    public IF_OF_LatchType() {
        OF_enable = true;
        instruction = -1;
        OF_busy = false;
        counter = 0;
    }

    public int returnCounter(){
        return counter;
    }

    public void setCounter(int cons){
        counter = cons;
    }

    public void setCheck(boolean chk){
        check = chk;
    }    

    public boolean getCheck(){
        return check;
    }

}
