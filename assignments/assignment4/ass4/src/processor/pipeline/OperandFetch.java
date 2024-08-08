package processor.pipeline;

import generic.Simulator;
import generic.Statistics;

// import java.util.HashMap;

import generic.Instruction.OperationType;
// import generic.Operand.OperandType;
import processor.Processor;
// import generic.Operand;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;

	public static String twoComplement(String binaryString) {
    // Find the index of the first '1' from the right
    int index = binaryString.length() - 1;
    while (index >= 0 && binaryString.charAt(index) != '1') {
        index--;
    }

    // If no '1' is found, return the original string as it's already in two's complement form
    if (index == -1) {
        return binaryString;
    }

    StringBuilder result = new StringBuilder(binaryString.length());

    // Flip all bits after the rightmost '1'
    for (int i = 0; i < index; i++) {
        result.append(binaryString.charAt(i) == '0' ? '1' : '0');
    }

    // Append the bits before the rightmost '1'
    result.append(binaryString, index, binaryString.length());

    return result.toString();
  }
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch,MA_RW_LatchType ma_RW_Latch,EX_MA_LatchType ex_MA_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.MA_RW_Latch = ma_RW_Latch;
		this.EX_MA_Latch = ex_MA_Latch;

	}
	
	public boolean checkIfConflict(int rs1, int rs2)
	{
		int rdMARW,rdEXMA,rdOFEX;
		rdEXMA = EX_MA_LatchType.getRd();
		rdMARW = MA_RW_Latch.getRd();
		rdOFEX = OF_EX_Latch.getRd();
		System.out.println("1: " + rdOFEX);
		System.out.println("2: " + rdEXMA);
		System.out.println("3: " + rdMARW);

		if(rs1 == rdEXMA ||rs1 == rdMARW || rs1 == rdOFEX || rs2 == rdEXMA || rs2 == rdMARW || rs2 == rdOFEX){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			System.out.println("THIS IS OF");
			if(OF_EX_Latch.iscond()){
				System.out.println("NOP in OF");
				OF_EX_Latch.setRd(100);
				Statistics.setNumberOfWrongBranch(Statistics.getNumberOfWrongBranch()+1);
				return;
			}
			String binaryInstruction = IF_OF_Latch.getInstruction();
			String opCode = binaryInstruction.substring(0, 5);
			OperationType operation = OperationType.values()[Integer.parseInt(opCode, 2)];
			int immediate=0;
			int rs1=0;
			int rs2=0;
			int rd=0;
			int offset;
			boolean a=false;
			boolean isBranch = false;
			switch(operation){
				case add:
				case sub:
				case mul:
				case div:
				case and:
				case or:
				case xor: 
				case slt: 
				case sll: 
				case srl: 
				case sra:
					rs1 = Integer.parseInt(binaryInstruction.substring(5, 10),2);
					rs2 = Integer.parseInt(binaryInstruction.substring(10, 15),2);
					rd = Integer.parseInt(binaryInstruction.substring(15, 20),2);
					if(checkIfConflict(rs1, rs2))
					{
						a=true;
						Statistics.setNumberOfOFInstruction(Statistics.getNumberOfOFInstruction()+1);					
					}
					OF_EX_Latch.setRs1(rs1);
					OF_EX_Latch.setRs2(rs2);
					OF_EX_Latch.setRd(rd);
					break;
				case addi:
				case subi:
				case muli:
				case divi:
				case andi:
				case ori:
				case xori:
				case slti:
				case slli:
				case srli:
				case srai:
				case load:
					rs1 = Integer.parseInt(binaryInstruction.substring(5, 10),2);
					rd = Integer.parseInt(binaryInstruction.substring(10, 15),2);
					immediate = Integer.parseInt(binaryInstruction.substring(15, 32),2);
					if(checkIfConflict(rs1, 90))
					{
						a=true;
						Statistics.setNumberOfOFInstruction(Statistics.getNumberOfOFInstruction()+1);
					}
					OF_EX_Latch.setRs1(rs1);
					OF_EX_Latch.setRd(rd);
					OF_EX_Latch.setImm(immediate);
					break;
				case store:
					rs1 = Integer.parseInt(binaryInstruction.substring(5, 10),2);
					rd = Integer.parseInt(binaryInstruction.substring(10, 15),2);
					immediate = Integer.parseInt(binaryInstruction.substring(15, 32),2);
					if(checkIfConflict(rd, 90))
					{
						a=true;
						Statistics.setNumberOfOFInstruction(Statistics.getNumberOfOFInstruction()+1);
					}
					OF_EX_Latch.setRs1(rs1);
					OF_EX_Latch.setRd(rd);
					OF_EX_Latch.setImm(immediate);
					break;
				case beq:
				case bgt:
				case blt:
				case bne:
					rs1 = Integer.parseInt(binaryInstruction.substring(5, 10),2);
					rd = Integer.parseInt(binaryInstruction.substring(10, 15),2);
					if(checkIfConflict(rs1, rd))
					{
						a=true;
						Statistics.setNumberOfOFInstruction(Statistics.getNumberOfOFInstruction()+1);
					}
					if(binaryInstruction.substring(15, 16).equals("1")){
						offset = -1*Integer.parseInt(twoComplement(binaryInstruction.substring(15, 32)),2);
						OF_EX_Latch.setOffset(offset);
						OF_EX_Latch.setRs1(rs1);
						OF_EX_Latch.setRd(rd);
					}else{
						offset = Integer.parseInt(binaryInstruction.substring(15, 32),2);
						OF_EX_Latch.setOffset(offset);
						OF_EX_Latch.setRs1(rs1);
						OF_EX_Latch.setRd(rd);
					}
					isBranch = true;
					break;
				case jmp:
					rd = Integer.parseInt(binaryInstruction.substring(5, 10),2);
					if(binaryInstruction.substring(10, 11).equals("1")){
						offset = -1*Integer.parseInt(twoComplement(binaryInstruction.substring(10, 32)),2);
					}else{
						offset = Integer.parseInt(twoComplement(binaryInstruction.substring(10, 32)),2);
					}
					OF_EX_Latch.setRd(rd);
					OF_EX_Latch.setOffset(offset);
					isBranch = true;
					break;
				case end:
					// Simulator.setSimulationComplete(true);
					int end_pc=containingProcessor.getRegisterFile().getProgramCounter();
					OF_EX_Latch.setend_pc(end_pc);
					break;
			}
			if(a==true){
				System.out.println("Data Hazard Caught In OF");
				IF_OF_Latch.sethazard(true);
				OF_EX_Latch.setRd(100);
			}else{
			IF_OF_Latch.sethazard(false);
			System.out.println("rs1 "+ rs1);
			System.out.println("rs2 "+rs2);
			System.out.println("rd "+rd);
			System.out.println("imme "+immediate);
			OF_EX_Latch.setInstruction(binaryInstruction);
			}
			OF_EX_Latch.setEX_enable(true);
			// IF_OF_Latch.setOF_enable(false);
		}
	}

}
