package processor.pipeline;

public class EX_IF_LatchType {
    boolean isBranchTaken;
    int offset;
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

    public EX_IF_LatchType() {
        isBranchTaken = false;
        offset = 70000;
    }

}

