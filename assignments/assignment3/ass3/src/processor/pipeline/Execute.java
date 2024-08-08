package processor.pipeline;

import generic.Instruction.OperationType;
import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX()
	{
		//TODO
		String binaryInstruction = OF_EX_Latch.getInstruction();
		// System.out.println(binaryInstruction);
		String opCode = binaryInstruction.substring(0,5);
		OperationType operation = OperationType.values()[Integer.parseInt(opCode,2)];
		int PC = containingProcessor.getRegisterFile().getProgramCounter();
		int rs1 = OF_EX_Latch.getRs1();
		int rs2 = OF_EX_Latch.getRs2();
		int rd = OF_EX_Latch.getRd();
		int immediate = OF_EX_Latch.getImm();
		int Offset = OF_EX_Latch.getOffset();
		int aluResult = 0;
		int A=0;
		int B=0;
		int rem= 0;
		switch (operation) {
			case add:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rs2);
				aluResult = A+B;
				break;
			case sub:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rs2);
				aluResult = A-B;
				break;
			case mul:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rs2);
				aluResult = A*B;
				break;
			case div:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rs2);
				aluResult = A/B;
				rem = A%B;
				break;
			case and:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rs2);
				aluResult = A&B;
				break;
			case or:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rs2);
				aluResult = A|B;
				break;
			case xor:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rs2);
				aluResult = A^B;
				break;
			case slt:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rs2);
				if(A>B){aluResult = 0;}
				else{aluResult=1;}
				break;
			case sll:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rs2);
				aluResult = A<<B;
				break;
			case srl:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rs2);
				aluResult = A>>B;
				break;
			case sra:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rs2);
				aluResult = A>>>B;
				break;
			
			case addi:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = immediate;
				aluResult = A+B;
				break;
			case subi:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = immediate;
				aluResult = A-B;
				break;
			case muli:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = immediate;
				aluResult = A*B;
				break;
			case divi:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = immediate;
				aluResult = A/B;
				rem = A%B;
				break;
			case andi:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = immediate;
				aluResult = A&B;
				break;
			case ori:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = immediate;
				aluResult = A|B;
				break;
			case xori:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = immediate;
				aluResult = A^B;
				break;
			case slti:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = immediate;
				if(A>B){aluResult = 0;}
				else{aluResult=1;}
				break;
			case slli:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = immediate;
				aluResult = A<<B;
				break;
			case srli:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = immediate;
				aluResult = A>>B;
				break;
			case srai:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = immediate;
				aluResult = A>>>B;
				break;
			case load:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = immediate;
				aluResult = A+B;
				break;
			case store:
				A = containingProcessor.getRegisterFile().getValue(rd);
				B = immediate;
				aluResult = A+B;
				break;
			
			case beq:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rd);
				if(A==B){
					EX_IF_Latch.set_If_enable(true);
					int bpc = Offset+PC-1;
					EX_IF_Latch.set_BPC(bpc);
					System.out.println("BPC " +bpc);
				}
				break;
				case bne:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rd);
				if(A!=B){
					EX_IF_Latch.set_If_enable(true);
					int bpc = Offset+PC-1;
					EX_IF_Latch.set_BPC(bpc);
					System.out.println("BPC " +bpc);
				}
				break;
				case blt:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rd);
				if(A<B){
					EX_IF_Latch.set_If_enable(true);
					int bpc = Offset+PC-1;
					EX_IF_Latch.set_BPC(bpc);
					System.out.println("BPC " +bpc);
				}
				break;
				case bgt:
				A = containingProcessor.getRegisterFile().getValue(rs1);
				B = containingProcessor.getRegisterFile().getValue(rd);
				if(A>B){
					EX_IF_Latch.set_If_enable(true);
					int bpc = Offset+PC-1;
					EX_IF_Latch.set_BPC(bpc);
					System.out.println("BPC " +bpc);
				}
				break;
				case jmp:
					EX_IF_Latch.set_If_enable(true);
					int bpc = Offset + PC-1;
					EX_IF_Latch.set_BPC(bpc);
					System.out.println("BPC " +bpc);
				break;

			default:
				break;
		}
		System.out.println("EX rs1 "+ rs1);
		System.out.println("EX rs2 "+ rs2);
		System.out.println("EX rd "+ rd);
		System.out.println("A "+A);
		System.out.println("B "+B);
		System.out.println("aluresult "+aluResult);
		EX_MA_Latch.setAluResult(aluResult);
		EX_MA_Latch.setImm(immediate);
		EX_MA_Latch.setInstruction(binaryInstruction);
		EX_MA_Latch.setRs1(rs1);
		EX_MA_Latch.setRs2(rs2);
		EX_MA_Latch.setRd(rd);
		EX_MA_Latch.setrem(rem);

		OF_EX_Latch.setEX_enable(false);
		EX_MA_Latch.setMA_enable(true);
	}

}
