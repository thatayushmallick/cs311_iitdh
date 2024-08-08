package processor.pipeline;

import configuration.Configuration;
import generic.*;
import generic.Instruction.OperationType;
import processor.Clock;
import processor.Processor;

import java.util.Arrays;

public class Execute implements Element {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	MA_RW_LatchType MA_RW_Latch;

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch,
			EX_IF_LatchType eX_IF_Latch, IF_OF_LatchType iF_OF_Latch, MA_RW_LatchType mA_RW_Latch) {
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}

	public void performEX() {
		if (EX_MA_Latch.isMA_busy()) {
			return;
		} else if (OF_EX_Latch.isEX_enable()) {
			if (OF_EX_Latch.isEX_busy()) {
				EX_MA_Latch.setMA_enable(false);
				return;
			}
			Instruction cmd = OF_EX_Latch.getInstruction();
			OperationType op_type = cmd.getOperationType();
			String opType = op_type.toString();
			EX_MA_Latch.setInstruction(cmd);
			OperationType cmd_op = cmd.getOperationType();
			int cmd_op_opcode = Arrays.asList(OperationType.values()).indexOf(cmd_op);
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter() - 1;
			int ALU_output = 0;
			boolean b = opType.equals("addi") || opType.equals("subi") || opType.equals("muli") || opType.equals("divi")
					|| opType.equals("andi") || opType.equals("ori") || opType.equals("xori") || opType.equals("slti")
					|| opType.equals("slli") || opType.equals("srli") || opType.equals("srai") || opType.equals("load");
			switch (op_type) {
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
					int op1 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand1().getValue());
					int op2 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand2().getValue());
					Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime() + Configuration.ALU_latency, this, this));
					OF_EX_Latch.setEX_busy(true);
					EX_MA_Latch.setMA_enable(false);

					if(opType.equals("add")){ALU_output = op1 + op2;}
					else if(opType.equals("sub")){ALU_output = op1 - op2;}
					else if(opType.equals("mul")){ALU_output = op1 * op2;}
					else if(opType.equals("div"))
					{
						ALU_output = op1 / op2;
						containingProcessor.getRegisterFile().setValue(31, op1%op2);
					}
					else if(opType.equals("and")){ALU_output = op1 & op2;}
					else if(opType.equals("or")){ALU_output = op1 | op2;}
					else if(opType.equals("xor")){ALU_output = op1 ^ op2;}
					else if(opType.equals("slt"))
					{
						if (op1 < op2)
							ALU_output = 1;
						else
							ALU_output = 0;
					}
					else if(opType.equals("sll")){ALU_output = op1 << op2;}
					else if(opType.equals("srl")){ALU_output = op1 >>> op2;}
					else if(opType.equals("sra")){ALU_output = op1 >> op2;}
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
					Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime() + Configuration.ALU_latency, this, this));
					OF_EX_Latch.setEX_busy(true);
					EX_MA_Latch.setMA_enable(false);
					int i = cmd.getSourceOperand1().getValue();
					op1 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand1().getValue());
					op2 = cmd.getSourceOperand2().getValue();
					if(opType.equals("addi")){ALU_output = op1 + op2;}
					else if(opType.equals("subi")){ALU_output = op1 - op2;}
					else if(opType.equals("muli")){ALU_output = op1 * op2;}
					else if(opType.equals("divi"))
					{
						ALU_output = op1 / op2;
						containingProcessor.getRegisterFile().setValue(31, op1%op2);
					}
					else if(opType.equals("andi")){ALU_output = op1 & op2;}
					else if(opType.equals("ori")){ALU_output = op1 | op2;}
					else if(opType.equals("xori")){ALU_output = op1 ^ op2;}
					else if(opType.equals("slti"))
					{
						if (op1 < op2)
							ALU_output = 1;
						else
							ALU_output = 0;
					}
					else if(opType.equals("slli")){ALU_output = op1 << op2;}
					else if(opType.equals("srli")){ALU_output = op1 >>> op2;}
					else if(opType.equals("srai")){ALU_output = op1 >> op2;}
					else if(opType.equals("load")){ALU_output = op1 + op2;}
					break;
				case store:
					int SOP1 = containingProcessor.getRegisterFile().getValue(cmd.getDestinationOperand().getValue());
					int SOP2 = cmd.getSourceOperand2().getValue();
					ALU_output = SOP1 + SOP2;
					break;
				case beq:
				case blt:
				case bgt:
				case bne:
					op1 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand1().getValue());
					op2 = containingProcessor.getRegisterFile().getValue(cmd.getSourceOperand2().getValue());
					int imm = cmd.getDestinationOperand().getValue();
					if(opType.equals("beq"))
					{
						if (op1 == op2) {
							ALU_output = imm + currentPC;
							containingProcessor.getRegisterFile().setProgramCounter(ALU_output);
							EX_MA_Latch.setFlag(1);
						}
					}
					else if(opType.equals("bne"))
					{
						if (op1 != op2) {
							ALU_output = imm + currentPC;
							containingProcessor.getRegisterFile().setProgramCounter(ALU_output);
							EX_MA_Latch.setFlag(1);
						}
					}
					else if(opType.equals("blt"))
					{
						if (op1 < op2) {
							ALU_output = imm + currentPC;
							containingProcessor.getRegisterFile().setProgramCounter(ALU_output);
							EX_MA_Latch.setFlag(1);
						}
					}
					else if(opType.equals("bgt"))
					{
						if (op1 > op2) {
							ALU_output = imm + currentPC;
							containingProcessor.getRegisterFile().setProgramCounter(ALU_output);
							EX_MA_Latch.setFlag(1);
						}
					}
					break;
				case nop:
					break;
				default:
					break;
			}
			EX_MA_Latch.setALU_result(ALU_output);
			if (!IF_OF_Latch.isOF_enable()) {
				OF_EX_Latch.setEX_enable(false);
			}
		}
	}

	@Override
	public void handleEvent(Event e) {
		// System.out.println(" Handling MemoryResponse Event in EX Stage");
		if (EX_MA_Latch.isMA_busy()) {
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		} else {
			// System.out.println(" is in ex");
			EX_MA_Latch.setMA_enable(true);
			OF_EX_Latch.setEX_busy(false);
		}
	}
}
