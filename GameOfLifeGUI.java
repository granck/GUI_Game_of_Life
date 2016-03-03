import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

public class GameOfLifeGUI extends JPanel implements Observer{
  
   private Grid grid;

   //Keyboard and Mouse listeners
   private MouseListener mouse;
   private KeyAdapter keyListener;
   private ActionListener  menuListener;
   
   //Menu bar with items 
   private JMenuBar menuBar;
   private JMenu patterns;
   private JMenuItem glider;
   private JMenuItem blinker;
   private JMenuItem beacon;
   private JMenuItem accorn;

   private int numRows; //number of rows
   private int numColumns; //number of columns

   public GameOfLifeGUI(Grid gridThing){
      super();

      //initialize variables
      this.grid = gridThing;
      numRows = grid.getNumRow();
      numColumns = grid.getNumColumns();
      mouse = new MouseListener();
      keyListener = new KeyboardListener();
      menuListener = new MenuSelectListener();

      //creates JFrame
      JFrame window = new JFrame("Game of Life");
      window.setFocusable(true);
      window.setSize(numRows*10 + 10, numColumns*10 + 80);
      window.setVisible(true);
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setLayout(new BorderLayout());
      window.add(this, BorderLayout.CENTER);

      //creates menuBar
      menuBar = new JMenuBar();

      //Makes pattern menu
      patterns = new JMenu("Patterns");
      menuBar.add(patterns);

      //creates patterns for menu options
      glider = new JMenuItem("Glider Pattern");
      patterns.add(glider);

      blinker = new JMenuItem("Blinker Pattern");
      patterns.add(blinker);

      beacon = new JMenuItem("Beacon Pattern");
      patterns.add(beacon);

      accorn = new JMenuItem("Accorn Pattern");
      patterns.add(accorn);

      window.setJMenuBar(menuBar);

      //add listeners to various components
      this.addMouseListener(mouse);
      this.addMouseMotionListener(mouse);
      glider.addActionListener(menuListener);
      blinker.addActionListener(menuListener);
      beacon.addActionListener(menuListener);
      accorn.addActionListener(menuListener);
      window.addKeyListener(keyListener);

      this.setBackground(Color.black);

      //register with Observable
      this.grid.addObserver(this);

   }//end constructor


   public void paintComponent(Graphics g){

      for(int row = 0; row < numRows; row++){
         for(int column = 0; column < numColumns; column++){
            
            if(grid.cellIsAlive(row, column)){
               g.setColor(Color.black);
               g.fillRect(row*10, column*10 , 8, 8);
            }//end if

            else{
               g.setColor(Color.white);
               g.fillRect(row*10 ,column*10 , 8, 8);
            }//end else

         }//end inner for
      }//end end for

   }//end method paintComponent
   
   public void update(Observable o, Object arg){
      
      repaint();

   }//end method update
   
   private class MouseListener extends MouseAdapter{
      
      public void mouseClicked(MouseEvent e){
         int xCord;
         int yCord;

         //divide by 10 to turn pixel count into cell count
         xCord = e.getX() / 10;
         yCord = e.getY() / 10;
         
         //if cell is alive make cell dead
         //else make cell alive
         if(grid.cellIsAlive(xCord, yCord))
            grid.setCellAlive(xCord, yCord, false);
         else
            grid.setCellAlive(xCord, yCord, true);

         repaint();

         
      }//end method mouseClicked

      public void mouseDragged(MouseEvent e){
         int xCord;
         int yCord;
         
         //divided by 10 to turn pixel count into cell count
         xCord = e.getX() / 10;
         yCord = e.getY() / 10;
         
         //if cell is alive make it dead
         //else make cell alive
         if(grid.cellIsAlive(xCord, yCord))
            grid.setCellAlive(xCord, yCord, false);
         else
            grid.setCellAlive(xCord, yCord, true);

         repaint();

      }//end method mouseDragged

   }//end inner class MouseListener
   
   private class KeyboardListener extends KeyAdapter{
      
      public void keyPressed(KeyEvent e){
         int keyCode;
         keyCode = e.getKeyCode();
         if(keyCode == KeyEvent.VK_B)
            grid.update();
        else if(keyCode == KeyEvent.VK_S)
           grid.save();
        else if(keyCode == KeyEvent.VK_L)
           grid.load();
      }//end method keyPressed
      
   }//end inner class KeyboardListener

   private class MenuSelectListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         if(e.getSource() == glider)
            grid.gliderSetup();
         else if(e.getSource() == beacon)
            grid.beaconSetup();
         else if(e.getSource() == blinker)
            grid.blinkerSetup();
         else if(e.getSource() == accorn)
            grid.accornSetup();
      }//end method actionPerformed

   }//end inner class MenuSelectListener

}//end class GameOfLifeGUI
