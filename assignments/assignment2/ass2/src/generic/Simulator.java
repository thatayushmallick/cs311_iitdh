package generic;

import java.io.*;
import java.nio.ByteBuffer;
import generic.Operand.OperandType;
import java.util.HashMap;
import java.util.Map;


public class Simulator {

	public static Map<String, String> opTable = Map.ofEntries(
            Map.entry("add", "00000"),
            Map.entry("sub", "00010"),
            Map.entry("mul", "00100"),
            Map.entry("div", "00110"),
            Map.entry("and", "01000"),
            Map.entry("or", "01010"),
            Map.entry("xor", "01100"),
            Map.entry("slt", "01110"),
            Map.entry("sll", "10000"),
            Map.entry("srl", "10010"),
            Map.entry("sra", "10100"),
            Map.entry("addi", "00001"),
            Map.entry("subi", "00011"),
            Map.entry("muli", "00101"),
            Map.entry("divi", "00111"),
            Map.entry("andi", "01001"),
            Map.entry("ori", "01011"),
            Map.entry("xori", "01101"),
            Map.entry("slti", "01111"),
            Map.entry("slli", "10001"),
            Map.entry("srli", "10011"),
            Map.entry("srai", "10101"),
            Map.entry("load", "10110"),
            Map.entry("store", "10111"),
            Map.entry("beq", "11001"),
            Map.entry("bne", "11010"),
            Map.entry("blt", "11011"),
            Map.entry("bgt", "11100"),
            Map.entry("jmp", "11000"),
            Map.entry("end", "11101")
    );
		
	static FileInputStream inputcodeStream = null;
	
	public static void setupSimulation(String assemblyProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}
	
	public static void assemble(String objectProgramFile)
	{
		FileOutputStream ink;
		try{
			//1. open the objectProgramFile in binary mode
			ink = new FileOutputStream(objectProgramFile);
			BufferedOutputStream pen = new BufferedOutputStream(ink);
			//2. write the firstCodeAddress to the file
			byte[] codeAddress = ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array();
			pen.write(codeAddress);

			//3. write the data to the file
			for(int i=0; i<ParsedProgram.data.size(); i++){
				byte[] data_values = ByteBuffer.allocate(4).putInt(ParsedProgram.data.get(i)).array();
				pen.write(data_values);
			}

			//4. assemble one instruction at a time, and write to the file
			for(int i=0; i<ParsedProgram.code.size(); i++){
				String binInst = "";
				String opType = ParsedProgram.code.get(i).getOperationType().toString();
				if(opType.equals("jmp")){
					binInst += (opTable.get("jmp"));
					binInst += "00000"; 
					int destAddr = 0;
					if(ParsedProgram.code.get(i).destinationOperand.operandType == OperandType.Label){ 
						destAddr = ParsedProgram.symtab.get(ParsedProgram.code.get(i).destinationOperand.labelValue);
					}
					else if(ParsedProgram.code.get(i).destinationOperand.operandType == OperandType.Immediate){
						destAddr = ParsedProgram.code.get(i).destinationOperand.value;
					}
					int pc = ParsedProgram.code.get(i).programCounter;
					int offset = destAddr - pc;
					System.out.println("pc= "+pc);
					System.out.println("offset= "+offset);
					if(offset>=0){
						String binRep = Integer.toBinaryString(offset);
						int binOffset = 22 - binRep.length();
						String concata = "";
						if(binOffset!=0){
						    concata = "0".repeat(binOffset);
						}
						binInst = binInst.concat(concata);
						binInst = binInst.concat(binRep);
					} else {
						String offString = Integer.toBinaryString(offset);
						String subRep = offString.substring(10, 32);
						binInst = binInst.concat(subRep);
					}
				}
				else if(opType.equals("load")||opType.equals("store")){
					binInst += (opTable.get(opType));
					if(ParsedProgram.code.get(i).sourceOperand1.operandType.toString().equals("Register")){
						binInst = binInst.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand1.value)).replace(' ', '0'));
					}
					if(ParsedProgram.code.get(i).destinationOperand.operandType.toString().equals("Register")){
						binInst = binInst.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).destinationOperand.value)).replace(' ', '0'));
					}
					if(ParsedProgram.code.get(i).sourceOperand2.operandType.toString().equals("Label")){
						String labeVal = String.format("%5s", Integer.toBinaryString(ParsedProgram.symtab.get(ParsedProgram.code.get(i).sourceOperand2.labelValue))).replace(' ', '0');
						int labeSize = labeVal.length();
						String labeConcat = "";
						if(labeSize != 17){
							int toBe = 17 - labeSize;
							labeConcat = "0".repeat(toBe);
							labeConcat = labeConcat.concat(labeVal);
						}
						binInst = binInst.concat(labeConcat);
					}
					if(ParsedProgram.code.get(i).sourceOperand2.operandType.toString().equals("Immediate")){
						String labeVal = Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand2.value);
						int labeSize = labeVal.length();
						String labeConcat = "";
						if(labeSize != 17){
							int toBe = 17 - labeSize;
                            labeConcat = "0".repeat(toBe);
							labeConcat = labeConcat.concat(labeVal);
						}
						binInst = binInst.concat(labeConcat);	
					}

				}
				else if(opType.equals("add") || opType.equals("sub") || opType.equals("mul") || opType.equals("div") || opType.equals("and") || opType.equals("or") || opType.equals("xor") || opType.equals("slt") || opType.equals("sll") || opType.equals("srl") || opType.equals("sra")){
					binInst += (opTable.get(opType));
					if(ParsedProgram.code.get(i).sourceOperand1.operandType.toString().equals("Register")){
						binInst = binInst.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand1.value)).replace(' ', '0'));
					}
					if(ParsedProgram.code.get(i).sourceOperand2.operandType.toString().equals("Register")){
						binInst = binInst.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand2.value)).replace(' ', '0'));
					}
					if(ParsedProgram.code.get(i).destinationOperand.operandType.toString().equals("Register")){
						binInst = binInst.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).destinationOperand.value)).replace(' ', '0'));
					}
                    String addInst = "0".repeat(12);
                    binInst = binInst.concat(addInst);
				}
				else if(opType.equals("addi") || opType.equals("subi") || opType.equals("muli") || opType.equals("divi") || opType.equals("andi") || opType.equals("ori") || opType.equals("xori") || opType.equals("slti") || opType.equals("slli") || opType.equals("srli") || opType.equals("srai")){
					binInst += (opTable.get(opType));
					if(ParsedProgram.code.get(i).sourceOperand1.operandType.toString().equals("Register")){
						binInst = binInst.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand1.value)).replace(' ', '0'));
					}
					if(ParsedProgram.code.get(i).destinationOperand.operandType.toString().equals("Register")){
						binInst = binInst.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).destinationOperand.value)).replace(' ', '0'));
					}
					if(ParsedProgram.code.get(i).sourceOperand2.operandType.toString().equals("Immediate")){
						String labeVal = Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand2.value);
						int labeSize = labeVal.length();
						String labeConcat = "";
						if(labeSize != 17){
							int toBe = 17 - labeSize;
                            labeConcat = "0".repeat(toBe);
							labeConcat = labeConcat.concat(labeVal);
						}
						binInst = binInst.concat(labeConcat);	
					}

				}
				else if(opType.equals("beq") || opType.equals("bgt") || opType.equals("bne") || opType.equals("blt")){
					binInst += (opTable.get(opType));
					if(ParsedProgram.code.get(i).sourceOperand1.operandType.toString().equals("Register")){
						binInst = binInst.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand1.value)).replace(' ', '0'));
					}
					if(ParsedProgram.code.get(i).sourceOperand2.operandType.toString().equals("Register")){
						binInst = binInst.concat(String.format("%5s", Integer.toBinaryString(ParsedProgram.code.get(i).sourceOperand2.value)).replace(' ', '0'));
					}
					int destAddr = 0;
					if(ParsedProgram.code.get(i).destinationOperand.operandType == OperandType.Label){ 
						destAddr = ParsedProgram.symtab.get(ParsedProgram.code.get(i).destinationOperand.labelValue);
					}
					else if(ParsedProgram.code.get(i).destinationOperand.operandType == OperandType.Immediate){
						destAddr = ParsedProgram.code.get(i).destinationOperand.value;
					}
					int pc = ParsedProgram.code.get(i).programCounter;
					int offset = destAddr - pc;
					System.out.println("pc= "+pc);
					System.out.println("offset= "+offset);
					if(offset>=0){
						String binRep = Integer.toBinaryString(offset);
						int binOffset = 17 - binRep.length();
						String concata = "";
						if(binOffset!=0){
                            concata = "0".repeat(binOffset);
						}
						binInst = binInst.concat(concata);
						binInst = binInst.concat(binRep);
	
					} else {
						String binRep = Integer.toBinaryString(offset);
						String subRep = binRep.substring(15, 32);
						binInst = binInst.concat(subRep);
					}
				}
				else if(opType.equals("end")){
                    binInst += (opTable.get(opType));
                    String concata = "0".repeat(27);
                    binInst = binInst.concat(concata);
				}
				System.out.println(binInst);
				int b_int = (int)Long.parseLong(binInst, 2);
				byte[] toThefile = ByteBuffer.allocate(4).putInt(b_int).array();
				pen.write(toThefile);
			}
			pen.close();

		} catch(FileNotFoundException e){
			e.printStackTrace();

		} catch(IOException e){
			e.printStackTrace();

		}
	}

	public static Map<String, String> getOpTable() {
		return opTable;
	}

	public static void setOpTable(HashMap<String, String> opTable) {
		Simulator.opTable = opTable;
	}}