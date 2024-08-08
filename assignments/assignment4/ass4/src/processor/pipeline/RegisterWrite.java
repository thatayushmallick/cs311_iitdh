package processor.pipeline;

import generic.Simulator;
import generic.Instruction.OperationType;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType  iF_OF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
			System.out.println("THIS IS RW");
			int rs1 = MA_RW_Latch.getRs1();
			int rs2 = MA_RW_Latch.getRs2();
			int rd = MA_RW_Latch.getRd();
			String instruction = MA_RW_Latch.getInstruction();
			int aluResult = MA_RW_Latch.getAluResult();
			int ldresult = MA_RW_Latch.get_ldresult();
			int rem = MA_RW_Latch.getrem();
			System.out.println("this is rw");
			if(rd==100){
				System.out.println("this is nop in rw");
				return;
			}
			OperationType operation = OperationType.values()[Integer.parseInt(instruction.substring(0,5),2)];
			switch (operation) {
				case add:
				case sub:
				case mul:
				case and:
				case or:
				case xor:
				case slt:
				case sll:
				case srl:
				case sra:
					containingProcessor.getRegisterFile().setValue(rd, aluResult);
					break;
				
				case addi:
				case subi:
				case muli:
				case andi:
				case ori:
				case xori:
				case slti:
				case slli:
				case srli:
				case srai:
					containingProcessor.getRegisterFile().setValue(rd, aluResult);
					break;
				case load:
					containingProcessor.getRegisterFile().setValue(rd, ldresult);
					System.out.println("load");
					break;
				case div:
				case divi:
					containingProcessor.getRegisterFile().setValue(rd, aluResult);
					containingProcessor.getRegisterFile().setValue(31, rem);
					break;
				case end:
					Simulator.setSimulationComplete(true);
					int end_pc=MA_RW_Latch.getend_pc();
					containingProcessor.getRegisterFile().setProgramCounter(end_pc);
					IF_EnableLatch.setIF_enable(false);
					System.out.println("end in rw");

					return;
					// break;
				default:
					break;
			}
			System.out.println("value should "+containingProcessor.getRegisterFile().getValue(5));
			System.out.println("sdcdsd " +containingProcessor.getRegisterFile().getValue(6));
			System.out.println("RW rd "+rd);
			System.out.println("RW LD "+ ldresult);
			System.out.println("rw alu "+ aluResult);
			System.out.println("memo 0 "+containingProcessor.getMainMemory().getWord(0));
			System.out.println("memo 1 "+containingProcessor.getMainMemory().getWord(1));
			System.out.println("memo 2 "+containingProcessor.getMainMemory().getWord(2));
			// MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
