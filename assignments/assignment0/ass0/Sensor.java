public class Sensor{

  public void activateSensor(){
    Main main = new Main();
    double probability = main.returnProbability();
    int[] position = main.returnPosition();
    int[][] border = main.returnBorder();
    int xCoordinate = position[1];
    int yCoordinate = position[0];
    double[] randomList = new double[4];
    randomList[0] = Math.random();
    randomList[1] = Math.random();
    randomList[2] = Math.random();
    randomList[3] = Math.random();
    if(randomList[0] < probability){
      border[yCoordinate][xCoordinate] = 1;
    }
    else{
      border[yCoordinate][xCoordinate] = 0;
    }
    if(randomList[1] < probability){
      border[yCoordinate + 1][xCoordinate] = 1;
    }
    else{
      border[yCoordinate + 1][xCoordinate] = 0;
    }
    if(randomList[0] < probability){
      border[yCoordinate + 1][xCoordinate + 1] = 1;
    }
    else{
      border[yCoordinate + 1][xCoordinate + 1] = 0;
    }
    if(randomList[0] < probability){
      border[yCoordinate + 1][xCoordinate - 1] = 1;
    }
    else{
      border[yCoordinate + 1][xCoordinate - 1] = 0;
    }
    main.setBorder(border);
  }
}