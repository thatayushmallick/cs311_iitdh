package processor.pipeline;

public class IF_EnableLatchType {

    boolean IF_enable;
    boolean IF_busy;
    int presentCount;
    int numberOfInstruction;

    public int getPCount(){
        return presentCount;
    }

    public void setPCount(int c){
        presentCount = c;
    }

    public int getIns(){
        return presentCount;
    }

    public void setIns(int I){
        numberOfInstruction = I;
    }

    public IF_EnableLatchType() {
        IF_enable = true;
        IF_busy = false;
    }

    public boolean isIF_busy() {
        return IF_busy;
    }

    public void setIF_busy(boolean iF_busy) {
        IF_busy = iF_busy;
    }

    public boolean isIF_enable() {
        return IF_enable;
    }

    public void setIF_enable(boolean iF_enable) {
        IF_enable = iF_enable;
    }

}
