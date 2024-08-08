package processor.pipeline;

import processor.Processor;

public class InstructionFetch {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		int CurrentPC = 0;
		if(IF_EnableLatch.isIF_enable())
		{
			if(EX_IF_Latch.get_If_enable()){
				CurrentPC = EX_IF_Latch.get_BPC();
				EX_IF_Latch.set_If_enable(false);
				System.out.println("hello i ma in if_ex");
			}
			else{
			CurrentPC = containingProcessor.getRegisterFile().getProgramCounter();
			}
			int newInstruction = containingProcessor.getMainMemory().getWord(CurrentPC);
			String newBinaryInstruction = Integer.toBinaryString(newInstruction);
			// System.out.println(newBinaryInstruction.length());
			newBinaryInstruction = String.format("%32s", newBinaryInstruction).replace(' ', '0');
			// System.out.println(newBinaryInstruction.length());
			IF_OF_Latch.setInstruction(newBinaryInstruction);
			containingProcessor.getRegisterFile().setProgramCounter(CurrentPC + 1);
			System.out.println("pc "+ CurrentPC);
			System.out.println("inst "+ newBinaryInstruction);
			IF_EnableLatch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(true);
		}
	}

}
