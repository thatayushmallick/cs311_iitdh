package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	String instruction;
	boolean case1;
	boolean case2;
	int num=0;
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public boolean isControlHazard(){
		return case2;
	}

	public void setControlHazard(boolean Case2){
		case2 = Case2;
	}

	public boolean isdata_hazard() {
		return case1;
	}

	public void sethazard(boolean Case1) {
		case1 = Case1;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public int num() {
		return num;
	}

	public void setnum() {
		num = num+1;
	}

}
