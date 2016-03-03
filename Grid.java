import java.util.Observable;
import java.io.*;

public class Grid extends Observable {

    private Cell[][] itsCurrentState;
    private Cell[][] itsNextState;
    private int numRows;
    private int numColumns;

    // construct a grid of cells
    public Grid(int numRows, int numColumns) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        itsCurrentState = new Cell[numRows][numColumns];
        itsNextState = new Cell[numRows][numColumns];
        for (int row=0; row< itsCurrentState.length; row++) {
            for (int column=0; column < itsCurrentState[row].length; column++) {
                itsCurrentState[row][column] = new Cell();
                itsNextState[row][column] = new Cell();
            }
        }
        setChanged();
        notifyObservers();
    }

    public int getNumRow(){
      return numColumns; 
    }//end method getNumRow

    public int getNumColumns(){
      return numRows;
    }//end method getNumColumns`

    public boolean cellIsAlive(int x, int y) {
        return itsCurrentState[x][y].isAlive();
    }
    public void setCellAlive(int x, int y, boolean val){
      itsCurrentState[x][y].setAlive(val);
    }//end method setCellAlive

    public void update() {
        // loop over rows
        for (int row=0; row < itsCurrentState.length; row++) {
            // loop over columns
            for (int column=0; column < itsCurrentState[row].length; column++) {
                boolean isAliveNextRound = aliveNextRound(row,column);
                itsNextState[row][column].setAlive(isAliveNextRound);
                
            }
        }
        swapStates();
    }


    private boolean aliveNextRound(int row, int column) {
        boolean aliveNextRound = false;;
        boolean currentAliveState = itsCurrentState[row][column].isAlive();
        int liveNeighbors = getCountOfLiveNeighbors(row,column);
        if (currentAliveState == true && liveNeighbors <2)
            aliveNextRound = false;
        else if ( currentAliveState == true && (liveNeighbors == 2 || liveNeighbors == 3) )
            aliveNextRound = true;
        else if ( currentAliveState == true && liveNeighbors > 3)
            aliveNextRound = false;
        else if ( currentAliveState == false && liveNeighbors == 3)
            aliveNextRound = true;
        return aliveNextRound;
    }

    private int getCountOfLiveNeighbors(int row, int column) {
        int numLiveNeighbors = 0;
        // check general case first - here we know we don't need
        // to check boundary conditions explicitly
        if (row != 0 && row != itsCurrentState.length - 1 &&
            column != 0 && column != itsCurrentState.length - 1) {

            for (int rowMod = -1; rowMod < 2; rowMod++) {
                for (int colMod = -1; colMod <2; colMod++) {
                    if ( itsCurrentState[row+rowMod][column+colMod].isAlive() &&
                         !(rowMod == 0 && colMod ==0) ) // don't count myself
                        numLiveNeighbors++;
                }
            }

        } // end of general case calculation
        else {  // now deal with edge cases
            int up = row-1;
            int down = row+1;
            int right = column+1;
            int left = column-1;
            if (row == 0) { // top edge case
                up = itsCurrentState.length-1;
            } else if (row == itsCurrentState.length-1) { // bottom edge case
                down = 0;
            }

            if (column == 0) { // left edge case
                left = itsCurrentState[0].length-1;
            } else if (column == itsCurrentState[0].length-1) { // right edge case
                right = 0;
            }

            int[][] neighborsToConsider = { {up,left},   {up,column}, {up,right},
                                            {row,left},               {row,right},
                                            {down,left},{down,column},{down,right} };

            for (int neighborIndex = 0; neighborIndex < neighborsToConsider.length; neighborIndex++) {
                if (itsCurrentState[neighborsToConsider[neighborIndex][0]][neighborsToConsider[neighborIndex][1]].isAlive())
                    numLiveNeighbors++;
            }

        } // end of edge cases clause
        return numLiveNeighbors;
    }

    // this swaps current and next references
    private void swapStates() {
        Cell[][] temp = itsCurrentState;
        itsCurrentState = itsNextState;
        itsNextState = temp;
        setChanged();
        notifyObservers();
    }
   
   //saves current grid instance to serialized file
   public void save(){
      FileOutputStream name = null;
      ObjectOutputStream out = null;
      
      //try to create file
      try{
         name = new FileOutputStream("GameOfLife.ser");
         out = new ObjectOutputStream(name);
      }
      catch(IOException e){
         System.out.println("Could not create file GameOfLife.ser");
      }//end try catch
      
      //try to save this instance of Grid to a serialized file
      try{
         out.writeObject(itsCurrentState);
         out.writeObject(itsNextState);
         out.write(numRows);
         out.write(numColumns);
      }
      catch(IOException e){
         System.out.println("Could not write \"TestGrid\" object to file");
      }//end try catch

      //try to close ObjectOutputStream
      try{
         out.close();
      }
      catch(IOException e){
         System.out.println("Could not close ObjectOutputStream \"out\"");
      }//end try catch

   }//end method save

    //reads in serialized instance of Grid
    public void load(){
      //read object from a file
      FileInputStream name = null;
      ObjectInputStream in = null;
      Cell[][] testCurrent = null;
      Cell[][] testNext = null;

      //try catch attempts to open saved state 
      try{
         name = new FileInputStream("GameOfLife.ser");
         in = new ObjectInputStream(name);
      }
      catch(IOException e){
         System.out.println("Could not open \"GameOfLife.ser\"");
      }//end try catch
      
      //try to read in currentState and nextState to temporary variables
      //before assigning them to testGrid.itsCurrentState and testGrid.itsNextState
      try{
         testCurrent = (Cell[][]) in.readObject();
         testNext = (Cell[][]) in.readObject();
         numRows = (int) in.readObject();
         numColumns = (int) in.readObject();
      }
      catch(IOException e){
         System.out.println("Could not read \"GameOfLife.ser\"");
      }
      catch(ClassNotFoundException e){
         System.out.println("Could not read \"GameOfLife.ser\"");
      }//end try catch

      //try to close ObjectInputStream
      try{
         in.close();
      }
      catch(Exception e){
         e.printStackTrace();
      }//end try catch

      //assigns testCurrent and testNext to testGrid's current and next
      itsCurrentState = testCurrent;
      itsNextState = testNext;

      //loaded different currentState. Must tell Observers
      setChanged();
      notifyObservers();

   }//end method load
          
    // @Override
    public String toString() {
        String returnVal = "";
        for (int i=0; i<itsCurrentState.length; i++) {
            for (int j=0; j<itsCurrentState[i].length; j++) {
                returnVal += itsCurrentState[i][j];
                returnVal += " ";
            }
            returnVal += "\n";
        }
        return returnVal;
    }

    public void gliderSetup() {
        reset();
        itsCurrentState[5][5].setAlive(true);
        itsCurrentState[6][5].setAlive(true);
        itsCurrentState[7][5].setAlive(true);
        itsCurrentState[7][4].setAlive(true);
        itsCurrentState[6][3].setAlive(true);

        setChanged();
        notifyObservers();
    }//end method gliderSetup

    public void blinkerSetup(){
       reset();
       itsCurrentState[3][3].setAlive(true);
       itsCurrentState[3][4].setAlive(true);
       itsCurrentState[3][5].setAlive(true);
       
       setChanged();
       notifyObservers();

    }//end method blinkerSetup

    public void beaconSetup(){
       reset();
       itsCurrentState[3][3].setAlive(true);
       itsCurrentState[3][4].setAlive(true);
       itsCurrentState[4][3].setAlive(true);
       itsCurrentState[5][6].setAlive(true);
       itsCurrentState[6][6].setAlive(true);
       itsCurrentState[6][5].setAlive(true);

       setChanged();
       notifyObservers();

    }//end method beaconSetup()

    public void accornSetup(){
        reset();
        itsCurrentState[3][2].setAlive(true);
        itsCurrentState[2][4].setAlive(true);
        itsCurrentState[3][4].setAlive(true);
        itsCurrentState[5][3].setAlive(true);
        itsCurrentState[6][4].setAlive(true);
        itsCurrentState[7][4].setAlive(true);
        itsCurrentState[8][4].setAlive(true);

        setChanged();
        notifyObservers();


    }//end method diehardSetup()
      
    //sets all cells to dead
    public void reset(){
        for (int row=0; row< itsCurrentState.length; row++) {
            for (int column=0; column < itsCurrentState[row].length; column++) {
                itsCurrentState[row][column].setAlive(false);
                itsNextState[row][column].setAlive(false);
            }//end for
        }//end for

    }//end method reset
}
