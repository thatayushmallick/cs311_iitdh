package generic;

import java.io.PrintWriter;

public class Statistics {

    // TODO add your statistics here
    static int numberOfInstructions;
    static int numberOfCycles;
    static float CPI;
    static float IPC;
    int CurrentCount;
    int CurreCycle;

    // TODO write functions to update statistics
    public void setNumberOfInstructions(int numberOfInstructions) {
        Statistics.numberOfInstructions = numberOfInstructions;
    }

    public void setCPI() {
        Statistics.CPI = (float)numberOfCycles/(float)numberOfInstructions;
    }

    public void setNumberOfCycles(int numberOfCycles) {
        Statistics.numberOfCycles = numberOfCycles;
    }

    public void setIPC() {
        Statistics.IPC = (float)numberOfInstructions/(float)numberOfCycles;
    }

    public static void printStatistics(String statFile)
    {
        try
        {
            PrintWriter writer = new PrintWriter(statFile);

            writer.println("Number of instructions executed = " + numberOfInstructions);
            writer.println("Number of cycles taken = " + numberOfCycles);
            writer.println("CPI = " + CPI);
            writer.println("IPC = " + IPC);

            // TODO add code here to print statistics in the output file

            writer.close();
        }
        catch(Exception e)
        {
            Misc.printErrorAndExit(e.getMessage());
        }
    }

    public int getCurrentCycle(){
        return CurreCycle;
    }

    public void setCurrentCycle(int c){
        CurreCycle = c;
    }

    public void setCount(int C){
        CurrentCount = C;
    }

    public int getCount(){
        return CurrentCount;
    }
}
