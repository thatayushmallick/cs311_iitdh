package generic;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import processor.Clock;
import processor.Processor;
import generic.Statistics;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static EventQueue eventQueue;
	public static int ins_count;
  public static long storeresp;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		eventQueue = new EventQueue();
		simulationComplete = false;
		ins_count=0;
    storeresp=0;
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
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			eventQueue.processEvents();
			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			Statistics.setNumberOfCycles(Statistics.getNumberOfCycles()+1);
		}
		
		// TODO
		// set statistics
		Statistics.setCPI();
		Statistics.setIPC();
		
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}

	public static EventQueue getEventQueue(){
		return eventQueue;
	}
}
