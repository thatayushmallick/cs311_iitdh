package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	int rs1;
	int rs2;
	int rd=101;
	int immediate;
	int branchTarget;
	String instruction;
	boolean cond;
	int end_pc;
	
	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}
	public boolean iscond() {
		return cond;
	}

	public void setcond(boolean Cond) {
		cond = Cond;
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

	public int getOffset(){
		return branchTarget;
	}

	public void setOffset(int brnchT){
		branchTarget = brnchT;
	}
	public int getend_pc(){
		return end_pc;
	}

	public void setend_pc(int pc){
		end_pc = pc;
	}

}
