package generic;

import processor.Clock;
import processor.Processor;
import processor.memorysystem.MainMemory;
import processor.pipeline.RegisterFile;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Simulator {

    static Processor processor;
    static boolean simulationComplete;
    static EventQueue eventQueue;
    public static int ins_count;
    public static long storeresp;

    public static void simulate() {
        int cycles = 0;
        while (!Simulator.simulationComplete) {
            processor.getRWUnit().performRW();
            processor.getMAUnit().performMA();
            processor.getEXUnit().performEX();
            eventQueue.processEvents();
            processor.getOFUnit().performOF();
            processor.getIFUnit().performIF();
            Clock.incrementClock();
            cycles++;
        }
        Statistics stat = new Statistics();
        stat.setNumberOfCycles(cycles);
        stat.setNumberOfInstructions(ins_count);
        stat.setCPI();
        stat.setIPC();
    }

    public static void setupSimulation(String assemblyProgramFile, Processor p) {
        Simulator.processor = p;
        loadProgram(assemblyProgramFile);
        simulationComplete = false;
        eventQueue = new EventQueue();
        ins_count=0;
        storeresp=0;
    }

    public static EventQueue getEventQueue() {
        return eventQueue;
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

    public static void setSimulationComplete(boolean value) {
        simulationComplete = value;
    }
}
