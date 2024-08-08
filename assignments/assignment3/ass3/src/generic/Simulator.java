package generic;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
		// load program into memory.
		try{
			InputStream inputStream = new FileInputStream(assemblyProgramFile);
			DataInputStream dataStream = new DataInputStream(inputStream);
			if(dataStream.available()>0)
			{
				int fourByte = dataStream.readInt();
				System.out.println(fourByte);
				processor.getRegisterFile().setProgramCounter(fourByte);
			}
			int address = 0;
			while(dataStream.available()>0){
				int fourByte = dataStream.readInt();
				System.out.println(fourByte);
				processor.getMainMemory().setWord(address, fourByte);
				address++;
			}
			processor.getRegisterFile().setValue(0, 0);
			processor.getRegisterFile().setValue(1, 65535);
			processor.getRegisterFile().setValue(2, 65535);
			dataStream.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void simulate()
	{
		while(simulationComplete == false)
		{
			processor.getIFUnit().performIF();
			processor.getOFUnit().performOF();
			processor.getEXUnit().performEX();
			processor.getMAUnit().performMA();
			processor.getRWUnit().performRW();
			Clock.incrementClock();

			Statistics.setNumberOfCycles(Statistics.getNumberOfCycles()+1);
			Statistics.setNumberOfInstructions(Statistics.getNumberofInstruction() + 1); 
		}
		
		// TODO
		// set statistics
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
