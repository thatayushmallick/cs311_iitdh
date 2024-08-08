package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	String instruction;
	int LdResult = 0;
	int aluResult;
	int rs1;
	int rs2;
	int rd=101;
	int immediate;
	int remainder;
	int end_pc;
	boolean is_end;
	public MA_RW_LatchType()
	{
		RW_enable = false;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}
	
	public int getend_pc(){
		return end_pc;
	}

	public void setend_pc(int pc){
		end_pc = pc;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	public String getInstruction(){
		return instruction;
	}

	public void setInstruction(String Instruction){
		instruction = Instruction;
	}

	public void set_ldresult(int ldresult){
		LdResult = ldresult;
	}
	public int get_ldresult(){
		return LdResult;
	}

	public int getAluResult(){
		return aluResult;
	}

	public void setAluResult(int alu){
		aluResult = alu;
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

	public int getRd(){
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
