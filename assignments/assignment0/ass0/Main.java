
public class Main {
  // SETTING WIDTH

  private static int width;
  public int returnWidth(){
    return width;
  }
  public void setWidth(int newWidth){
    width = newWidth;
  }

  // SETTING PROBABILITY
  private static double probability;
  public double returnProbability(){
    return probability;
  }
  public void setProbability(double newProbability){
    probability = newProbability;
  }

  // SETTING BORDER
  private static int[][] border;
  public int[][] returnBorder(){
    return border;
  }
  public void setBorder(int[][] newBorder){
    border = newBorder;
  }

  // SETTING POSITION
  private static int[] position;
  public int[] returnPosition(){
    return position;
  }
  public void setPosition(int[] newPosition){
    position = newPosition;
  }

  // SETTING TIME
  private static int time;
  public int returnTime(){
    return time;
  } 
  public void setTime(int newTime){
    time = newTime;
  }

  // SETTING FILENAME
  
  public static int fileNameValue;
  public int returnFileNameValue(){
    return fileNameValue;
  }
  public void setFileNameValue(int newFileNameValue){
    fileNameValue = newFileNameValue;
  }
}
