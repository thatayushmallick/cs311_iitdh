package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfCycles;
	

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			
			// TODO add code here to print statistics in the output file
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	public static void setNumberOfInstructions(int numberOfInst) {
			Statistics.numberOfInstructions = numberOfInst;
	}

	public  static void setNumberOfCycles(int numberOfCycle) {
		Statistics.numberOfCycles = numberOfCycle;
	}
	public static int getNumberOfCycles() {
		return numberOfCycles;
	}
	public static int getNumberofInstruction() {
		return numberOfInstructions;
	}
}
