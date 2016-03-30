//******************************************************
// Fall 2014 CS230 FINAL PROJECT: MainPanel class
// Ana Balcells, Ye Eun Jeong, Lucie Randall for CS230
//******************************************************
//This class defines the MainPanel which describes the app's function ot the user 
//and which allows them to select either an Eulerian or Hamiltonian algorithm
//Author: Ana Balcells and Ye Eun Jeong

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainPanel extends JPanel {
  
  //instance vars
  private JLabel introLabel, middleLabel, instructionsLabel, creditsLabel;
  private JButton eulerButton, hamiltonButton;
  private static boolean sourceEuler, sourceHamilton;
  
  //constructor
  //Author: Ye Eun Jeong
  public MainPanel() {
    
    //use vertical box layout and set background color
    setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) ); 
    setBackground( ColorScheme.getBackground() );
    
    
    //create buttons
    eulerButton = new JButton("Eulerian Graphs");
    eulerButton.addActionListener(new ButtonListener());
    
    hamiltonButton = new JButton("Hamiltonian Graphs");
    hamiltonButton.addActionListener(new ButtonListener());
    
    //create intro label
    introLabel = new JLabel("<html><font color='white'>GRAPHS++</font></html>"); 
    introLabel.setFont(new Font("Cambria", Font.BOLD, 40));
    introLabel.setHorizontalAlignment(SwingConstants.CENTER);
    introLabel.setBackground( ColorScheme.getDark() );
    introLabel.setOpaque(true);
    introLabel.setBorder( BorderFactory.createMatteBorder(15,15,15,15, ColorScheme.getDark() ) );

    //create instructions label
    instructionsLabel = new JLabel("<html>Welcome to Graphs++! Build a graph from scratch or upload a .tgf file<br>"
                                     +"to test for Euler or Hamilton circuits and paths. Select from the options<br>"
                                     +"below to get started.</html>");
    instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
    instructionsLabel.setFont(new Font("Cambria", Font.PLAIN, 20));
    instructionsLabel.setBorder( BorderFactory.createMatteBorder(15,15,15,15, ColorScheme.getBackground()) );
 
    //create credits label
    creditsLabel = new JLabel("<html><font color = 'white'>(C) Ana Balcells, Ye Eun Jeong, "
                                + "Lucie Randall for CS230 Fall 2014.</font></html>");
    creditsLabel.setFont(new Font("Cambria", Font.PLAIN, 11));
    creditsLabel.setBackground( ColorScheme.getMedium() );
    creditsLabel.setOpaque(true);
    creditsLabel.setBorder( BorderFactory.createMatteBorder(15,15,15,15, ColorScheme.getMedium()) );
    
    //add all labels and rigid areas to help with layout
    add(introLabel);
    add ( Box.createRigidArea( new Dimension(0, 10) ) );
    add(instructionsLabel);
    add ( Box.createRigidArea( new Dimension(0, 20) ) );
    add(eulerButton);
    add(Box.createRigidArea( new Dimension(0, 10)));
    add(hamiltonButton);
    add ( Box.createRigidArea( new Dimension(0, 300) ) ); 
    add(creditsLabel);
    add ( Box.createRigidArea( new Dimension(0, 10) ) );
  }
  
  //Getter to be used in the InputPanel class to determine which button calculation options appear
  //Returns 0 if user selected Euler grpahs, 1 if selected Hamiltonian
  //Author: Ana Balcells
  public static int getSource() {
    if (sourceEuler) return 0;
    else return 1; 
  }
  
  //Listener which uses switch panel method to bring up the input panel
  //sets booleans which tell input panel which calculation buttons to present
  //Author: Ana Balcells
  private class ButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent e){
      //sets type of algorithm to be used based on buttons clicked
      if(e.getSource() == eulerButton) {
        sourceHamilton = false;
        sourceEuler = true;
      }
      else if (e.getSource() == hamiltonButton){ 
        sourceHamilton = true;
        sourceEuler = false;
      }
      GraphPlusPlusGUI.switchPanel(MainPanel.this, new InputPanel(new GraphPlusPlus())); //switches to InputPanel
    }
  }//end of ButtonListener class 
    
}//end of MainPanel class
