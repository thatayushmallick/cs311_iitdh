package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfCycles;
	static int numberofOFStageInstruction;
	static int numberOfWrongBranch;
	

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			// TODO add code here to print statistics in the output file
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("Number of OF stage needed to stall = "+ numberofOFStageInstruction);
			writer.println("Number of times an instruction on a wrong branch path entered the pipeline = "+numberOfWrongBranch);			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	public static void setNumberOfInstructions(int numberOfInstructions) {
		Statistics.numberOfInstructions = numberOfInstructions;
	}

	public static int getNumberOfInstruction(){
		return numberOfInstructions;
	}

	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}

	public static int getNumberOfCycles(){
		return numberOfCycles;
	}

	public static int getNumberOfOFInstruction(){
		return numberofOFStageInstruction;
	}

	public static void setNumberOfOFInstruction(int a){
		numberofOFStageInstruction = a; 
	}

	public static void setNumberOfWrongBranch(int b){
		numberOfWrongBranch = b;
	}

	public static int getNumberOfWrongBranch(){
		return numberOfWrongBranch;
	}

}
