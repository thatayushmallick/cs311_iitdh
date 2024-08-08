import java.util.Scanner;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Printer{
  public static void main(String[] args){
    Main main = new Main();

    String fileName = "output.txt";
    Scanner scanner = new Scanner(System.in);

      System.out.print("ENTER WIDTH: ");
      int width = scanner.nextInt();
      scanner.nextLine();
      
      System.out.print("ENTER PROBABILITY: ");
      double probability = scanner.nextDouble();
      scanner.nextLine();

      main.setWidth(width);
      main.setProbability(probability);

      Sensor sensor = new Sensor();
      Initialise initialise = new Initialise();
      Infiltrator attack = new Infiltrator();
      Clock clock = new Clock();
      initialise.IntialiseBorder();
      initialise.IntialisePosition();
      initialise.InitialiseTime();

      int yCoordinate = main.returnPosition()[0];
      while(yCoordinate < width - 1){
        sensor.activateSensor();
        attack.startAttack();
        clock.clockWork();
        yCoordinate = main.returnPosition()[0];
      }

      int time = main.returnTime();
      System.out.println("TOTAL TIME FOR CROSSING BORDER: " + time + " SECONDS");
      
      try{
        File file = new File(fileName);
        FileWriter writer = new FileWriter(file, true);
        BufferedWriter appender = new BufferedWriter(writer);
        appender.write(width + " " + probability + " " + time + "\n");
        appender.close();
        writer.close();
      } catch(IOException e){
        System.out.println("AN ERROR OCCURED");
        e.printStackTrace();
      }
      scanner.close();
  }
}