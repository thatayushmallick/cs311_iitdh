

public class Initialise {
  
  // INTIALISE POSITION AND BORDER

  public void IntialiseBorder(){
    Main main = new Main();
    int width = main.returnWidth();
    int[][] intialBorder = new int[width][10000];
    main.setBorder(intialBorder);
  }
  public void IntialisePosition(){
    Main main = new Main();
    int width = main.returnWidth();
    int[] position = {0, (int)width/2};
    main.setPosition(position);
  }

  // INITIALISE TIME
  public void InitialiseTime(){
    Main main = new Main();
    int time = 0;
    main.setTime(time);
  }
}
