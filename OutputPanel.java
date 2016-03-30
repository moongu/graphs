//******************************************************
// Fall 2014 CS230 FINAL PROJECT: OutputPanel class
// Ana Balcells, Ye Eun Jeong, Lucie Randall for CS230
//******************************************************
//This class defines the methods used to create a display of the Eulerian/Hamiltonian calculations
//It also explains to the user why certain features are or are not present
//Authors: Ana Balcells and Ye Eun Jeong

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.lang.*;
import java.util.*; 
import java.lang.Exception.*;

public class OutputPanel extends JPanel {
  
  //instance variables
  private JPanel resultPanel, circuitPanel, buttonsPanel;
  private JLabel results, explainLabel, eulerLabel, isEulerianLabel, isConnectedLabel, isAllEvenLabel, 
    nodeSequenceLabel, circuitLabel, homeLabel;
  private JButton homeButton; //inputButton;
  private static GraphPlusPlus graph;
    
 //constructor
 //Author: Ana Balcells
  public OutputPanel (GraphPlusPlus g) { 
    graph = g; //takes in a graph as input, assigns it to instance variable of graph
    
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBackground( ColorScheme.getBackground());
  
    //make panels
    resultPanel = new JPanel();
    resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
    resultPanel.setBackground(ColorScheme.getBackground());
    
    circuitPanel = new JPanel();
    circuitPanel.setLayout(new BoxLayout(circuitPanel, BoxLayout.Y_AXIS));
    circuitPanel.setBackground(ColorScheme.getBackground());
    
    buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
    buttonsPanel.setBackground(ColorScheme.getBackground());
    
    //make labels
    results = new JLabel("<html><font color='white'>RESULTS</font></html>"); 
    results.setFont(new Font("Cambria", Font.BOLD, 30));
    results.setOpaque(true);
    results.setBackground( ColorScheme.getDark() );
    results.setHorizontalAlignment(SwingConstants.CENTER); //center-align text
    results.setBorder( BorderFactory.createMatteBorder(15,15,15,15, ColorScheme.getDark() ) ); //for padding
    
    eulerLabel = new JLabel("<html><font color='white'>Is your graph Eulerian?</font></html>"); 
    eulerLabel.setFont(new Font("Cambria", Font.BOLD, 20));
    eulerLabel.setHorizontalAlignment(SwingConstants.CENTER); //center-align text
    eulerLabel.setBackground( ColorScheme.getMedium() );
    eulerLabel.setOpaque(true);
    eulerLabel.setBorder( BorderFactory.createMatteBorder(15,15,15,15, ColorScheme.getMedium() ) ); //for padding
    
    isEulerianLabel = new JLabel();
    isEulerianLabel.setFont(new Font("Cambria", Font.PLAIN, 18));
    isEulerianLabel.setBorder( BorderFactory.createMatteBorder(15,15,15,15, ColorScheme.getBackground()));
    
    isConnectedLabel = new JLabel();
    isConnectedLabel.setFont(new Font("Cambria", Font.PLAIN, 18));
    isConnectedLabel.setBorder(BorderFactory.createMatteBorder(15,15,15,15, ColorScheme.getBackground() ) ); //for padding
    
    isAllEvenLabel = new JLabel(); 
    isAllEvenLabel.setFont(new Font("Cambria", Font.PLAIN, 18));
    isAllEvenLabel.setBorder(BorderFactory.createMatteBorder(15,15,15,15, ColorScheme.getBackground() ) ); //for padding
   
    circuitLabel = new JLabel();
    circuitLabel.setFont(new Font("Cambria", Font.PLAIN, 18));
    circuitLabel.setBorder(BorderFactory.createMatteBorder(15,15,15,15, ColorScheme.getBackground() ) ); //for padding
    
    explainLabel = new JLabel("<html><font color='white'>Why?</font></html>"); 
    explainLabel.setFont(new Font("Cambria", Font.BOLD, 20));
    explainLabel.setHorizontalAlignment(SwingConstants.CENTER); //center-align text
    explainLabel.setBackground( ColorScheme.getMedium() );
    explainLabel.setOpaque(true);
    explainLabel.setBorder( BorderFactory.createMatteBorder(15,15,15,15, ColorScheme.getMedium() ) ); //for padding
    
    nodeSequenceLabel = new JLabel("<html><font color='white'>Euler circuit node sequence </font></html>"); 
    nodeSequenceLabel.setFont(new Font("Cambria", Font.BOLD, 20));
    nodeSequenceLabel.setOpaque(true);
    nodeSequenceLabel.setHorizontalAlignment(SwingConstants.CENTER); //center-align text
    nodeSequenceLabel.setBackground(ColorScheme.getAccent());
    nodeSequenceLabel.setBorder( BorderFactory.createMatteBorder(15,15,15,15, ColorScheme.getAccent() ) ); //for padding
    
    homeLabel = new JLabel("Click to return to the main page and begin again.");
    
    //make buttons
    homeButton = new JButton("Go back to the main page");
    homeButton.addActionListener(new HomeListener());
   
    //add components to sub-panels
    resultPanel.add(results); resultPanel.add(eulerLabel);resultPanel.add(isEulerianLabel); resultPanel.add(explainLabel);
    resultPanel.add(isConnectedLabel); resultPanel.add(isAllEvenLabel); 
    circuitPanel.add(nodeSequenceLabel); circuitPanel.add(circuitLabel); 
    buttonsPanel.add(homeLabel); buttonsPanel.add(homeButton);
    
    //adds sup-panels into Output panel
    add(resultPanel);
    add(circuitPanel);
    add(Box.createRigidArea( new Dimension(0, 20) ) );
    add(buttonsPanel);
  }

  //If the user clicked Euler circuit in the InputPanel - this method updates the labels accordingly 
  //Author: Ye Eun Jeong
  public void updateEC() {
    graph = InputPanel.getGraph();
    
    if(!graph.isEulerian()){
      isEulerianLabel.setText("No.");
    }else if (graph.isEulerian()){
      isEulerianLabel.setText("Yes!");
    }
    
    if(!graph.isConnected())
      isConnectedLabel.setText("The graph is NOT connected.");
    else if (graph.isConnected())
      isConnectedLabel.setText("The graph is connected.");
    
    if(!graph.isAllEvenDegrees())
      isAllEvenLabel.setText("NOT all of the graph's vertices have even degrees.");
    else if(graph.isAllEvenDegrees())
      isAllEvenLabel.setText("All of the graph's vertices have even degrees.");
    
    if(graph.getEulerCircuit()==null)
      circuitLabel.setText("Euler circuit could not be found.");
    else if(graph.getEulerCircuit()!=null)
      circuitLabel.setText(graph.getEulerCircuit().toString());  
    
    resultPanel.validate();
    resultPanel.repaint();
    
    circuitPanel.validate();
    circuitPanel.repaint();
  }
  
  
  //If the user clicked Euler path in the InputPanel - this method updates the labels accordingly 
  // Author: Ye Eun Jeong
  public void updateEP() {
    graph = InputPanel.getGraph();
    
    eulerLabel.setText("<html><font color='white'>Does your graph have an Eulerian path?</font></html>"); 
    nodeSequenceLabel.setText("<html><font color='white'>Euler path node sequence </font></html>");
    
    if(!graph.hasEulerPath()){
      isEulerianLabel.setText("No.");
      isEulerianLabel.setHorizontalAlignment(SwingConstants.CENTER); 
    } else if (graph.hasEulerPath()){
      isEulerianLabel.setText("Yes!");
      isEulerianLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    if(!graph.isConnected())
      isConnectedLabel.setText("The graph is NOT connected.");
    else if (graph.isConnected())
      isConnectedLabel.setText("The graph is connected.");
    
    if(!graph.hasTwoOddDegrees())
      isAllEvenLabel.setText("The graph does NOT have exactly two vertices of odd degree.");
    else if(graph.hasTwoOddDegrees())
      isAllEvenLabel.setText("The graph has exactly two vertices of odd degree.");
    
    if(graph.getEulerPath()==null)
      circuitLabel.setText("Euler path could not be found.");
    else if(graph.getEulerPath()!=null)
      circuitLabel.setText(graph.getEulerPath().toString());  
    
    resultPanel.validate();
    resultPanel.repaint();
    
    circuitPanel.validate();
    circuitPanel.repaint();
  }
  
  //If the user clicked Hamiltonian circuit in the InputPanel - this method updates the labels accordingly 
  // Author: Ana Balcells
  public void updateHC() {
    graph = InputPanel.getGraph();
    
    eulerLabel.setText("<html><font color='white'>Does your graph have a Hamiltonian circuit?</font></html>"); 
    nodeSequenceLabel.setText("<html><font color='white'>Hamiltonian node sequence </font></html>");
    resultPanel.remove(explainLabel); resultPanel.remove(isConnectedLabel); resultPanel.remove(isAllEvenLabel); 
    if(graph.getHamiltonCircuit() == null){
      isEulerianLabel.setText("No.");
    }else {
      isEulerianLabel.setText("Yes!");
    }

    if(graph.getHamiltonCircuit()==null)
      circuitLabel.setText("Hamilton circuit could not be found.");
    else if(graph.getHamiltonCircuit()!=null)
      circuitLabel.setText(graph.getHamiltonCircuit().toString());  
    
    resultPanel.validate();
    resultPanel.repaint();
    
    circuitPanel.validate();
    circuitPanel.repaint();
  }
  
  //If the user clicked Hamiltonian path in the InputPanel - this method updates the labels accordingly 
  //Author: Ana Balcells
  public void updateHP() {
    graph = InputPanel.getGraph();
    
    eulerLabel.setText("<html><font color='white'>Does your graph have a Hamiltonian path?</font></html>"); 
    nodeSequenceLabel.setText("<html><font color='white'>Hamiltonian node sequence </font></html>");
    resultPanel.remove(explainLabel); resultPanel.remove(isConnectedLabel); resultPanel.remove(isAllEvenLabel); 
    
    if(graph.getHamiltonPath() == null){
      isEulerianLabel.setText("No.");
    }else {
      isEulerianLabel.setText("Yes!");
    }
    
    if(graph.getHamiltonPath()==null)
      circuitLabel.setText("Hamilton path could not be found.");
    else if(graph.getHamiltonPath()!=null)
      circuitLabel.setText(graph.getHamiltonPath().toString());  
    
    resultPanel.validate();
    resultPanel.repaint();
    
    circuitPanel.validate();
    circuitPanel.repaint();
  }

  //Listener which uses switchPanel to return user to main page to begin again 
  //Author: Ye Eun Jeong
  private class HomeListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      GraphPlusPlusGUI.switchPanel(GraphPlusPlusGUI.getOutputPanel() , new MainPanel());
    }
  }
  
}//end of outputpanel class