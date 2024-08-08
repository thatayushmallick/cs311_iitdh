package processor.pipeline;

import generic.Instruction.OperationType;
import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		int rd = EX_MA_Latch.getRd();
		int rs1 = EX_MA_Latch.getRs1();
		int rs2 = EX_MA_Latch.getRs2();
		int rem = EX_MA_Latch.getrem();
		String Instruction = EX_MA_Latch.getInstruction();
		int aluresult = EX_MA_Latch.getAluResult();
		int Ldresult = 0;
		OperationType operation = OperationType.values()[Integer.parseInt(Instruction.substring(0,5),2)];
		switch (operation) {
			case load:
				Ldresult = containingProcessor.getMainMemory().getWord(aluresult);
				break;
			case store:
				containingProcessor.getMainMemory().setWord(aluresult, containingProcessor.getRegisterFile().getValue(rs1));
				break;
			default:
				break;
		
		}
		System.out.println("ldresult "+Ldresult);
		System.out.println("aluresult "+aluresult);
		MA_RW_Latch.setRs1(rs1);
		MA_RW_Latch.setRs2(rs2);
		MA_RW_Latch.setRd(rd);
		MA_RW_Latch.setAluResult(aluresult);
		MA_RW_Latch.set_ldresult(Ldresult);
		MA_RW_Latch.setInstruction(Instruction);
		MA_RW_Latch.setrem(rem);

		EX_MA_Latch.setMA_enable(false);
		MA_RW_Latch.setRW_enable(true);

		
	}

}
