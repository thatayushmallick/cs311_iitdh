package processor.memorysystem;

public class CacheLine{
    int[] tag = new int[2]; 
    int[] data = new int[2];
    int LRUctr;
    int cycle_count;
    int instruction_count;

    public int getCycle() {
        return cycle_count;
    }

    public void setData(int tag, int d) {
        if(tag == this.tag[0]){
            this.data[0] = d;
            this.LRUctr = 1;
        } else if(tag == this.tag[1]){
            this.data[0] = d;
            this.LRUctr = 0;
        } else{
            int l = this.LRUctr;
            this.tag[l] = tag;
            this.data[l] = d;
            this.LRUctr = 1 - this.LRUctr;
        }
	}

    public int getInsCount(){
        return instruction_count;
    }

    public CacheLine(int val) {
        this.tag[0] = -1;
        this.tag[1] = -1;
        this.LRUctr = val;
    }

    public void setInsCount(int c){
        instruction_count = c;
    }

    public int getData(int index) {
        return this.data[index];
    }

    public void setCycle(int cy){
        cycle_count = cy;
    }

    public int getTag(int index) {
        return this.tag[index];
    }

    public int getLRUctr() {
        return this.LRUctr;
    }

    public String toString() {
        return Integer.toString(this.LRUctr);
    }

    public int setLRUctr(int lRUctr) {
        this.LRUctr = lRUctr;
        return this.LRUctr;
    }

    public CacheLine() {
        this.tag[0] = -1;
        this.tag[1] = -1;
        this.LRUctr = 0;
    }
}