public class Infiltrator {
  public void startAttack(){
    Main main = new Main();
    int[] position = main.returnPosition();
    int[][] border = main.returnBorder();
    int yCoordinate = position[0];
    int xCoordinate = position[1];
    if(border[yCoordinate][xCoordinate] != 1){
      if(border[yCoordinate + 1][xCoordinate] != 1){
        yCoordinate = yCoordinate + 1;
        position[0] = yCoordinate;
        main.setPosition(position);
      }else if(border[yCoordinate + 1][xCoordinate + 1] != 1){
        yCoordinate = yCoordinate + 1;
        xCoordinate = xCoordinate + 1;
        position[0] = yCoordinate;
        position[1] = xCoordinate;
        main.setPosition(position);
      }else if(border[yCoordinate + 1][xCoordinate - 1] != 1){
        xCoordinate = xCoordinate - 1;
        yCoordinate = yCoordinate + 1;
        position[0] = yCoordinate;
        position[1] = xCoordinate;
        main.setPosition(position);
      }
    }
  }
}
