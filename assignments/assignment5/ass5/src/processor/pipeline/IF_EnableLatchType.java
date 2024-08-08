package processor.pipeline;

public class IF_EnableLatchType {

    boolean IF_enable = true;
    boolean IF_busy = false;
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
