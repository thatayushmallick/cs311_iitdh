package processor.pipeline;

public class EX_IF_LatchType {
	boolean If_enable;
	int Bpc;
	
	public EX_IF_LatchType()
	{
		If_enable = false;
	}
	public void set_If_enable(boolean if_eanble){
		If_enable = if_eanble;
	}
	public boolean get_If_enable(){
		return If_enable;
	}
	public void set_BPC(int PC){
		Bpc = PC;
	}
	public int get_BPC(){
		return Bpc;
	}
}
