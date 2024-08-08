package processor.pipeline;

import generic.Simulator;

// import java.util.HashMap;

import generic.Instruction.OperationType;
// import generic.Operand.OperandType;
import processor.Processor;
// import generic.Operand;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;


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
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			String binaryInstruction = IF_OF_Latch.getInstruction();
			// System.out.println(binaryInstruction);
			String opCode = binaryInstruction.substring(0, 5);
			OperationType operation = OperationType.values()[Integer.parseInt(opCode, 2)];
			int immediate=0;
			int rs1=0;
			int rs2=0;
			int rd=0;
			int offset;
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
				case store:
					rs1 = Integer.parseInt(binaryInstruction.substring(5, 10),2);
					rd = Integer.parseInt(binaryInstruction.substring(10, 15),2);
					immediate = Integer.parseInt(binaryInstruction.substring(15, 32),2);
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
					break;
				case end:
					Simulator.setSimulationComplete(true);
					break;
			}
			System.out.println("rs1 "+ rs1);
			System.out.println("rs2 "+rs2);
			System.out.println("rd "+rd);
			System.out.println("imme "+immediate);
			OF_EX_Latch.setInstruction(binaryInstruction);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
