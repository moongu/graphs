//******************************************************
// Fall 2014 CS230 FINAL PROJECT: InputPanel class
// Ana Balcells, Ye Eun Jeong, Lucie Randall for CS230
//******************************************************
//This class creates a panel which allows a user to input graph 
//data into GraphPlusPlus object.
//Authors: Ana Balcells and Ye Eun Jeong

import java.awt.*;
import java.awt.event.*; //event handler
import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.*; 

public class InputPanel extends JPanel {
  
  //instance vars
  private static GraphPlusPlus graph;
  private JPanel descriptionPanel, topPanel, bottomPanel, nodeInputPanel, nodeDisplayPanel,
    edgeInputPanel, edgeDisplayPanel, filePanel;
  private JLabel descriptionLabel, nodesLabel, edgesLabel, andLabel, filePathLabel, nListLabel, eListLabel;
  private JTextField nodeName, fileName;
  private JComboBox node1, node2;
  private JTextArea nList, eList;
  private JRadioButton scratch, file;
  private JButton nodeAdd, edgeAdd, upload, ecButton, epButton, hcButton, hpButton;
  private boolean scratchBool, fileBool;
  private int source;
  
  //Constructor
  //Authors: Ana Balcells and Ye Eun Jeong
  public InputPanel(GraphPlusPlus g) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBackground( ColorScheme.getBackground() );
    
    graph = g; //sets g to be the graph given as a parameter
    
    //creates radio buttons which determine input for graph
    ButtonGroup radiobuttons = new ButtonGroup();
    scratch = new JRadioButton("Create from scratch");
    file = new JRadioButton("Create from TGF file");
    radiobuttons.add(scratch); radiobuttons.add(file); 
    RadioListener listen = new RadioListener();
    scratch.addActionListener(listen);
    file.addActionListener(listen);
    
    //label to describe purpose of this panel
    descriptionPanel = new JPanel();
    descriptionPanel.setBackground(ColorScheme.getMedium());
    descriptionLabel = new JLabel("<html><font color = 'white'>Enter graph data by hand or by uploading a .tgf file."
                               + " Once you have entered your data, hit the Calculate button and <br>"
                               + "view the results of this caclulation in the next tab.</font></html>"); 
    descriptionLabel.setFont(new Font("Cambria", Font.PLAIN, 16));
    descriptionLabel.setBorder(BorderFactory.createMatteBorder(5,5,5,5, ColorScheme.getMedium()) ); //for padding
    descriptionPanel.add(descriptionLabel);
    
    //----------------------------
    //to create graph from scratch
    //----------------------------
    
    //constructs necessary items to add a node to a graph
    nodesLabel = new JLabel("Nodes: ");
    nodeName = new JTextField(7); //sets default length of node name to 7 characters
    nodeAdd = new JButton("Add");
    nodeAdd.addActionListener(new NodeListener());
    
    //necessary items to add edges to the graph
    edgesLabel = new JLabel("Edges: ");
    node1 = new JComboBox();
    node1.addItem("select a node");
    andLabel = new JLabel("-->");
    node2 = new JComboBox();
    node2.addItem("select a node");
    edgeAdd = new JButton("Add");
    edgeAdd.addActionListener(new EdgeListener());
    
    nListLabel = new JLabel("Nodes you have created: ");
    eListLabel = new JLabel("Edges you have created: ");
    
    nList = new JTextArea(1, 15);
    nList.setEditable(false);
    eList = new JTextArea(10, 10);
    eList.setEditable(false);
    
    //----------------------------
    //to create graph from tgf file
    //----------------------------
    filePathLabel = new JLabel("File path of .tgf file");
    fileName = new JTextField(20); 
    upload = new JButton("Upload");
    upload.addActionListener(new UploadListener());
    
    //calculate buttons
    ecButton = new JButton("Find an Euler Circuit!");
    ecButton.setHorizontalAlignment(SwingConstants.CENTER);
    ecButton.addActionListener(new CalcListener());
    
    epButton = new JButton("Find an Euler Path!");
    epButton.setHorizontalAlignment(SwingConstants.CENTER);
    epButton.addActionListener(new CalcListener());
    
    hcButton = new JButton("Find a Hamilton Circuit!");
    hcButton.setHorizontalAlignment(SwingConstants.CENTER);
    hcButton.addActionListener(new CalcListener());
    
    hpButton = new JButton("Find a Hamilton Path!");
    hpButton.setHorizontalAlignment(SwingConstants.CENTER);
    hpButton.addActionListener(new CalcListener());
    
    
    //sub-panels
    topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
    topPanel.setBackground( ColorScheme.getBackground() );
    
    bottomPanel = new JPanel();
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
    bottomPanel.setBackground( ColorScheme.getBackground() );
    
    nodeInputPanel = new JPanel();
    nodeInputPanel.add(nodesLabel); nodeInputPanel.add(nodeName); nodeInputPanel.add(nodeAdd);
    nodeDisplayPanel = new JPanel();
    nodeDisplayPanel.add(nListLabel); nodeDisplayPanel.add(nList);
    nodeInputPanel.setOpaque(false); nodeDisplayPanel.setOpaque(false);
    
    edgeInputPanel = new JPanel();
    edgeInputPanel.add(edgesLabel); edgeInputPanel.add(node1); edgeInputPanel.add(andLabel); edgeInputPanel.add(node2); 
    edgeInputPanel.add(edgeAdd); 
    edgeDisplayPanel = new JPanel();
    edgeDisplayPanel.add(eListLabel); edgeDisplayPanel.add(eList); 
    edgeInputPanel.setOpaque(false); edgeDisplayPanel.setOpaque(false);
    
    filePanel = new JPanel();
    filePanel.add(filePathLabel); filePanel.add(fileName); filePanel.add(upload);
    filePanel.setOpaque(false);
    
    //adding it all!
    add(descriptionPanel);
    topPanel.add(scratch);
    topPanel.add(nodeInputPanel); topPanel.add(nodeDisplayPanel); 
    topPanel.add(edgeInputPanel); topPanel.add(edgeDisplayPanel);
    add(topPanel);
    
    bottomPanel.add(file); bottomPanel.add(filePanel);
    add(bottomPanel);
    
    //determines which calculate buttons to add depending on function user clicked in main panel
    source = MainPanel.getSource();
    if (source == 0) {
      add(ecButton); add(epButton);
    } else if (source == 1) {
      add(hcButton); add(hpButton);
    }
  }
  
  //This listener determines source of graph input info by setting either scratch or file boolean to be true
  //If "scratch" radio button selected, graph will be constructed from info which user inputs.
  //If "file" radio button selected, graph will be constructed from tgf file
  //Authors: Ana Balcels and Ye Eun Jeong
  private class RadioListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      //if scratch radio button selected
      if(e.getSource() == scratch) {
        graph = new GraphPlusPlus(); //refresh graph when clicked
        //sets scratchBool to true, highlights the "From scratch" section of GUI
        scratchBool = true; fileBool = false;
        fileName.setText(""); fileName.setBackground(Color.WHITE); //refresh the file upload display
        topPanel.setBackground(ColorScheme.getAccent());
        bottomPanel.setBackground(ColorScheme.getBackground());
      }
      //if file radio button selected
      if(e.getSource() == file) {
        graph = new GraphPlusPlus(); //refresh graph when clicked
        //sets fileBool to true, highlights the "From file" section of GUI
        fileBool = true; scratchBool = false;
        
        //refresh scratch creation display
        nList.setText(""); eList.setText(""); 
        node1.removeAllItems(); node1.addItem("select a node"); 
        node2.removeAllItems(); node2.addItem("select a node");
        
        
        bottomPanel.setBackground(ColorScheme.getAccent());
        topPanel.setBackground(ColorScheme.getBackground());
      }
      
      //refreshes panels to reflect changes made above
      topPanel.validate();
      topPanel.repaint();
      bottomPanel.validate();
      bottomPanel.repaint();
    }
  }
  
  //Listener for the nodeAdd button
  //Author: Ana Balcells
  private class NodeListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if(scratchBool){ //if scratch radio button selected
        String vertex = nodeName.getText(); //sets vertex name to string in textfield
        if (vertex.length() > 10) //limits vertex names to 10 characters, else displays error message
          JOptionPane.showMessageDialog(null, "Please limit node names to < 10 characters.");
        else {
          if (vertex != null && !graph.containsVertex(vertex)) { //if nodeName is valid and not already in graph
            graph.addVertex(vertex); //adds vertex to graph
            nList.append(vertex + "  "); //adds vertex to string list in textArea
            nodeName.setText(""); //empties out the nodeName textfield to prepare for a new entry
            node1.addItem(vertex); node2.addItem(vertex); //adds new vertex to edge comboboxes
          }
        }
      }
    }
  }
  
  //Listener for edgeAdd button
  //Author: Ana Balcells
  private class EdgeListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if(scratchBool){//if scratch radio button selected
        //sets vertices of edge to the values selected in the comboboxes
        String vertex1 = (String) node1.getSelectedItem();
        String vertex2 = (String) node2.getSelectedItem();
        
        //check if edge is not already in graph, that it's not making an edge with itself, 
        //and that default combobox value isn't selected
        if (!graph.isEdge(vertex1,vertex2) && !vertex1.equals(vertex2) && 
            !vertex1.equals(node1.getItemAt(0)) && !vertex2.equals(node2.getItemAt(0))) {
          graph.addEdge(vertex1, vertex2); //adds edge to graph
          eList.append(vertex1 + " - " + vertex2 + "\n"); //displays edge in textarea
        
        } else { //displays error message if any of the edge checks fails
          JOptionPane.showMessageDialog(null, "Please select a valid edge.\n"+ 
                 "Note: An edge cannot be added more than once and no node can connect with itself.");
        }
      }
    }
  }
  
  //Listener for upload button
  // Author: Ana Balcells
  private class UploadListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (fileBool) { //if radio button for from file is selected
        String file = fileName.getText(); //reads in file name from text field
        try {
          graph = GraphPlusPlus.fromFile(file); //creates graph from the tgf file
          //sets new text and color in text field to let user know that upload was successful
          fileName.setText("Successfully uploaded!"); 
          fileName.setBackground(Color.GREEN);
          
        } catch (FileNotFoundException f) { //displays error message if the file upload was unsuccessful
          JOptionPane.showMessageDialog(null, "File not found. Graph could not be created.");
        }
      }
    }
  }
 
  //Listener for calc buttons
  // Author: Ana BalCells and Ye Eun Jeong
  private class CalcListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (graph.isEmpty()) //checks that user has created a graph, returns an error message if they haven't
        JOptionPane.showMessageDialog(null, "Graph is empty. Please add vertices/edges.");
      else if(!graph.isUndirected()){
        graph = new GraphPlusPlus(); //clear graph if graph from TGF is a directed graph
        fileName.setText(""); fileName.setBackground(Color.WHITE); //refresh file input display
        JOptionPane.showMessageDialog(null, "Graph is directed. Please add an undirected graph.");
      } 
     
     else {
        //reset all input data to empty in case user returns to create a new graph
        fileName.setText(""); fileName.setBackground(Color.WHITE);
        eList.setText(""); nList.setText(""); 
        node1.removeAllItems(); node1.addItem("select a node"); 
        node2.removeAllItems(); node2.addItem("select a node");
        bottomPanel.setBackground(ColorScheme.getBackground());
        topPanel.setBackground(ColorScheme.getBackground());
        buttonUpdate();
        
        //switches frame to display output panel
        OutputPanel outputpanel = GraphPlusPlusGUI.getOutputPanel();
        
        //Updates layout of OutputPanel to match the results of the calculation selected
        if(e.getSource()==ecButton)
          outputpanel.updateEC();
        if(e.getSource()==epButton)
          outputpanel.updateEP();
        if(e.getSource()==hcButton)
          outputpanel.updateHC();
        if(e.getSource()==hpButton)
          outputpanel.updateHP();
        
        //switches to output panel
        GraphPlusPlusGUI.switchPanel(InputPanel.this , outputpanel);
        
        graph = new GraphPlusPlus(); //refresh the graph
      }
    }    
  }
  
  //When a user starts a new graph, the calculation buttons are removed so that the new panel will reflect 
  //whatever calc the user chooses
  // Author: Ana Balcells
  public void buttonUpdate() {
    if (source == 0) {
        remove(ecButton); remove(epButton);
      } else if (source == 1) {
        remove(hcButton); remove(hpButton);
      }
  }
 
  //Used in OutputPanel's update() method, returns the graph object created in InputPanel
  //Author: Ye Eun Jeong
  public static GraphPlusPlus getGraph() {
    return graph;
  }

}//end of InputPanel class