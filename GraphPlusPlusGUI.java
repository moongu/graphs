//******************************************************
// Fall 2014 CS230 FINAL PROJECT: GraphPlusPlusGUI class
// Ana Balcells, Ye Eun Jeong, Lucie Randall for CS230
//******************************************************
//This class is the driver class for our Graph++ application.
//It defines a main method which runs the program, and three getters 
//which retrieve each type of panel which is used within the frame.
//This class also defines a switchpanel method which is used to switch 
//between panels depending on button listeners. 
//Author: Ye Eun Jeong

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;

public class GraphPlusPlusGUI {
  
  //instance variables
  private static JFrame frame;
  private static GraphPlusPlus graph = new GraphPlusPlus();
  
  //creates instance variables for each type of panel
  private static MainPanel mainpanel = new MainPanel();
  private static InputPanel inputpanel = new InputPanel(graph);
  private static OutputPanel outputpanel = new OutputPanel(graph);
  
  //Make constant int vars to store min width and height of window
  private final static int MIN_WIDTH = 900;
  private final static int MIN_HEIGHT = 600;
  
  //Main method to create the panels and 
  public static void main (String[] args) {
    
    //create and show a Frame 
    frame = new JFrame("Graphs++");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    //set minimum size
    frame.setMinimumSize(new Dimension (MIN_WIDTH, MIN_HEIGHT) );

    //set frame content to be mainpanel at first
    frame.getContentPane().add(mainpanel);
    frame.pack();
    frame.setVisible(true);
   }
   
  //This method retrieves the MainPanel
  public static MainPanel getMainPanel() {
    return mainpanel;
  }
  
   //This method retrieves the InputPanel
   public static InputPanel getInputPanel() {
     return inputpanel;
   }
   
   //This method retrieves the OutputPanel
   public static OutputPanel getOutputPanel() {
     return outputpanel;
   }

   
   //This method allows the program to move between panel by changing panel contents 
   //(i.e. setting updatePanel to visible and originalPanel to not be visible).
   public static void switchPanel(JPanel originalPanel, JPanel updatePanel) {
     originalPanel.setVisible(false); //hides originalPanel 
     updatePanel.setVisible(true); //makes updatePanel visible
     
     //adds new panel to the frame, refreshes frame contents to show changes
     frame.getContentPane().add(updatePanel);
     frame.validate();
     frame.repaint();
   }

   
}//end of class