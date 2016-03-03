import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GameOfLife {

    public static void main(String[] args) throws InterruptedException {

       Grid grid = new Grid(60, 60);
       GameOfLifeGUI test = new GameOfLifeGUI(grid);

    }
}
