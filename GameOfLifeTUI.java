public class GameOfLifeTUI {

    private Grid myGrid;
    int xDim;
    int yDim;

    public GameOfLifeTUI(int xDim, int yDim) {
        myGrid = new Grid(xDim,yDim);
        myGrid.gliderSetup();
    }

    public void run() throws InterruptedException {
       // on purpose an endless loop
       clearScreen();
       while (true) {
          displayGrid();
          myGrid.update();
          Thread.sleep(20);
          clearScreen();
       } 
    }

    public void displayGrid() {
       System.out.print(myGrid);
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
