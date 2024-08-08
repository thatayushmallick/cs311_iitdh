package processor.pipeline;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int aluResult;
	int immediate;
	int rs1;
	int rs2;
	String instruction;
	public static int rd=101;
	int offset;
	int remainder;
	int end_pc;
	
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public int getend_pc(){
		return end_pc;
	}

	public void setend_pc(int pc){
		end_pc = pc;
	}

	public int getAluResult(){
		return aluResult;
	}

	public void setAluResult(int alu){
		aluResult = alu;
	}

	public String getInstruction(){
		return instruction;
	}

	public void setInstruction(String Instruction){
		instruction = Instruction;
	}

	public int getRs1(){
		return rs1;
	}

	public void setRs1(int Rs1){
		rs1 = Rs1;
	}

	public int getRs2(){
		return rs2;
	}

	public void setRs2(int Rs2){
		rs2 = Rs2;
	}

	public static int getRd(){
		return rd;
	}

	public void setRd(int Rd){
		rd = Rd;
	}

	public int getImm(){
		return immediate;
	}

	public void setImm(int imm){
		immediate = imm;
	}

	public int getrem(){
		return remainder;
	}

	public void setrem(int rem){
		remainder = rem;
	}

}
