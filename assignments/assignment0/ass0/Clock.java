public class Clock {
  public void clockWork(){
    Main main = new Main();
    int currentTime = main.returnTime();
    currentTime += 10;
    main.setTime(currentTime);
  }
}
