//************************************************************
// Fall 2014 CS230 FINAL PROJECT: GraphPlusPlus class
// Ana Balcells, Ye Eun Jeong, Lucie Randall for CS230
// Authors: Ye Eun Jeong and Ana Balcells
//************************************************************
// Extends AdjMatGraph class.
// New methods serve to power back-end calculations for Graphs++ program.
// 
// Methods summary: two constructors, clone(), dfsTraversal(),
// vertexDegree(), isConnected(), isAllEvenDegrees(), isEulerian(),
// hasTwoOddDegrees(), hasEulerPath(),
// traverseEdges, traverseEdgesEP, traverseVertices, traverseVerticesHP,
// getEulerCircuit(), getEulerPath(), getHamiltonCircuit(), getHamiltonPath().

import java.util.LinkedList;
import javafoundations.LinkedStack;
import java.io.FileNotFoundException;
import java.util.*; 

public class GraphPlusPlus<T> extends AdjMatGraph<T> {
  
  //parent class instance variables are protected and accessible
  private boolean hamCircuitFound, hamPathFound;
  
//-------------------------------------------------------------
// Constructors: call parent constructor or make from TGF file
// Author: Ana Balcells
//-------------------------------------------------------------  
  public GraphPlusPlus() {
    super(); //calls parent constructor
  }
  
  public static GraphPlusPlus<String> fromFile(String file) throws FileNotFoundException{
    //constructs new empty graph
    GraphPlusPlus<String> g = new GraphPlusPlus<String>(); 
    //fills in graph info using method in parent class
    AdjMatGraph.loadTGF(file,g);
    
    return g;//returns GraphPlusPlus object
  }  
  
//-------------------------------------------------------------
// clone(): Constructs a copy of "this" GraphPlusPlus object.
// Used for destructive algorithms.
// Author: Ana Balcells
//-------------------------------------------------------------
  public GraphPlusPlus<T> clone() {
    GraphPlusPlus<T> cloned = new GraphPlusPlus<T>(); //creates an empty graph
    
    //defines instance variables of clone to be the same as the this object
    cloned.n = n;
    cloned.vertices = (T[]) new Object[vertices.length];
    cloned.arcs = new boolean[arcs.length][arcs.length];
    
    //fills in vertices array and arcs[][] with same info as in this
    for (int i = 0; i < n; i++) {
      cloned.vertices[i] = vertices[i];
      for (int j = 0; j < n; j++) {
        cloned.arcs[i][j] = arcs[i][j];
      }
    }
    
    return cloned; //returns GraphPlusPlus clone
  }

//-------------------------------------------------------------
// vertexDegree(T vertex): Returns an int value of the degree
// of a given vertex. Used to test for Eulerian graph conditions.
// private helpder method for various methods testing for
// a graph's vertex degrees overall.
// Author: Ye Eun Jeong
//------------------------------------------------------------- 

  private int vertexDegree(T vertex) { 
    int vd = 0; //int to hold the vertex degree, starts counter at 0
    
    //go through the vertex's row in the adjacency matrix horizontally
    for(int i=0; i < n; i++) {
      int j = getIndex(vertex); 
      if(j != NOT_FOUND) //checks to make sure the vertex exists in the graph
        if (arcs[j][i])
        vd++; //increment degree each time there is an arc between
              //the vertex and another vertex
    }
    return vd; //returns degree count
  }

//-------------------------------------------------------------
// isAllEvenDegrees(): Checks whether all vertices in a given
// graph have even degrees. Public method, since resulting boolean is
// accessed when displaying test results in program GUI.
// Author: Ye Eun Jeong
//------------------------------------------------------------- 

  public boolean isAllEvenDegrees() {
    boolean result;
    
    //for each vertex, check if the degree is even
    for(T v : this.vertices) {
      //remainder 0 when divided by 2 means it's even
      if(vertexDegree(v)%2!=0)
        return false; //method returns false if any single vertex
                      //in the graph does not have even degree
    }

    return true; //true when all vertex degrees are divisible by 2
  }

//-------------------------------------------------------------
// isEulerian(): Check the two conditions required for an Eulerian
// graph--whether it is connected and all vertex degrees are even.
// Public method, since resulting boolean is accessed when
// displaying test results in program GUI.
// Author: Ye Eun Jeong
//-------------------------------------------------------------  

  public boolean isEulerian() {
    
    if(n==1 && m()==0)
      return true; //an island graph is technically Eulerian
    else
      return (isConnected() && isAllEvenDegrees());
  }
 
//-------------------------------------------------------------
// hasTwoOddDegrees(): Checks whether a graph as exactly two
// vertices of odd degree. Used to check whether a graph has
// an euler path. Public method, since resulting boolean is
// accessed when displaying test results in program GUI.
// Author: Ye Eun Jeong
//-------------------------------------------------------------  
  
  public boolean hasTwoOddDegrees() {
    int oddDegree=0; //counter for odd degree vertices
    for(T v : this.vertices) {
      if(vertexDegree(v)%2==1)
        oddDegree++; //for each vertex, if vertex degree is odd,
                     //increment odd degree counter
    }
    return (oddDegree==2);
    //true if there is exactly two odd degree vertices
  }

//-------------------------------------------------------------
// hasEulerPath(): Checks necessary conditions for an Euler path,
// connectivity and exactly two vertices of odd degree.
// Public method, since resulting boolean is accessed when
// displaying test results in program GUI.
// Author: Ye Eun Jeong
//-------------------------------------------------------------  
  public boolean hasEulerPath() {
    return (isConnected() && hasTwoOddDegrees());
  }

//-------------------------------------------------------------
// traverseEdges: Private recursive helpder method, used to find
// an euler circuit. Destructive algorithm that looks for a vertex's
// neighbor, and "traverses" the edge between vertex and the neighbor
// by deleting it from the adjacency matrix. Once edge is traversed,
// current vertex is added to a "path" of node sequences.
// Recursed on the neighbor vertex unless the neighboring vertex
// is not found. In an eulerian graph that has an euler circuit,
// not being able to find a neighbor vertex means that a sub-cycle
// has been formed within the euler circuit, or that the entire
// euler circuit has formed.
// Author: Ye Eun Jeong
//-------------------------------------------------------------  

  private void traverseEdges(Vector path, GraphPlusPlus graph, T currentVertex){ 
    
    T nextVertex = null;
    
    for (int i = 0; i < graph.n; i++) { //itereate on all vertices in the graph
    //algorithm destructive: all operations are on the temporary copy graph that was passed in.
      if(graph.getIndex(currentVertex) != NOT_FOUND){ //proceed if vertex is valid
        //look horizontally in a vertex's AdjMat row for a neighbor
        if (graph.arcs[getIndex(currentVertex)][i]) {
          nextVertex = (T) graph.getVertex(i); //assign next vertex
          graph.removeEdge(currentVertex, nextVertex); //"traverse" the edge
          path.add(currentVertex); //add current vertex to node sequence
          break; //if found a next vertex, save it and exit
        }
      }
    }
    
    if(nextVertex!=null){ //if there is a next neighbor, recurse
      traverseEdges(path, graph, nextVertex);
    }
  }

//-------------------------------------------------------------
// getEulerCircuit(): Creates a vector holding a sequence of nodes
// visited in order to complete an Euler circuit. If circuit is not
// found, returns null. Calls the helpder method traverseEdges.
// After getting back a sealed circuit from one full iteration of
// the traverseEdges method, check whether all edges were traversed.
// If there are still edges left to be traversed, test which vertex
// in the returned sub-circuit should be the next starting point
// for another traversal. Whichever vertex that still has more neighbors
// left after a destructive traversal of edges shall be the next
// starting point. Do another traversal and get back another sub-circuit.
// Merge the two sub-circuits, doing a detour at the "bottleneck"
// vertex which connects the two sub-circuits. Repeat until all
// edges have been traversed.
// Author: Ye Eun Jeong
//-------------------------------------------------------------  

  public Vector<T> getEulerCircuit() { 
    
    //make a temporary copy of the graph
    GraphPlusPlus<T> tempGraph = clone();
    
    //checks to ensure graph meets 2 necessary Eulerian conditions
    if(tempGraph.isEulerian()){
      
      //we can start at whatever is at 0 of vertices array
      T currentVertex = tempGraph.vertices[0];
      T nextVertex = null;
      //holds node sequence for first resulting sub-circuit
      Vector<T> p = new Vector<T>();
      //holds node sequence for a possible second sub-circuit
      Vector<T> q = new Vector<T>();
      
      if(tempGraph.n==1 && tempGraph.m()==0){ //if graph is a single island
        p.add(currentVertex);
        return p; //euler circuit is just that island
      } else { //if it's not an island, start finding circuit
        
        traverseEdges(p, tempGraph, currentVertex);
        //returns when first sub-circuit is sealed
        
        while(tempGraph.m()!=0){
        //after getting a sealed circuit, check if all edges were traversed
        //if there are still edges left to be traversed, find another sub-circuit
          
          //find vertex in the first path to serve as next starting point
          for(T v : p){
            if(tempGraph.vertexDegree(v)!=0)
              nextVertex = v;
          }
          
          //traverse edges starting at next vertex
          traverseEdges(q, tempGraph, nextVertex);
          
          p.addAll(p.indexOf(nextVertex), q);
          //merge q to p at index of "bottleneck vertex"
          
          q.clear(); //empties q after every merge
                     //so that the vector can be used again if necessary
        } 
        
        p.add(p.get(0)); //adds the starting vertices to the vector
                         //(since each circuit ends at its starting vertices)
        return p; //returns completed circuit
        
      }
    } else return null; //if not Eulerian, returns null
  }

//-------------------------------------------------------------
// traverseEdgesEP: private recursive helpder method, used to find
// an euler path. similar to traverseEdges method. The key difference
// is remembering that while traversing edges destructively,
// when a neighbor with vertex degree 1 comes along, saving that
// vertex as the exit vertex. Only traverse an edge adjacent to
// a neighbor with degree 1 if there is 1 edge left to traverse.
// Author: Ye Eun Jeong
//-------------------------------------------------------------  

  private void traverseEdgesEP(Vector path, GraphPlusPlus graph, T currentVertex){ 
    
    T nextVertex = null;
    
    for (int i = 0; i < graph.n; i++) {
      if(graph.getIndex(currentVertex) != NOT_FOUND){ //proceed if current vertex is valid
        if (graph.arcs[getIndex(currentVertex)][i]) { //and if there is a neighbor
          if(graph.vertexDegree(graph.getVertex(i))!=1) { //and that neighbor's degree is not 1
            nextVertex = (T) graph.getVertex(i);
            graph.removeEdge(currentVertex, nextVertex);
            break; //if found a next vertex, save it and exit
            
            //if there is only one edge (two arcs) left,
            //then go ahead to the 1-degree vertex
          } else if (graph.m()==2){
            nextVertex = (T) graph.getVertex(i);
            graph.removeEdge(currentVertex, nextVertex);
            break;
          }
        }
      }
    }
    
    if(nextVertex!=null){ //if there is a next neighbor, recurse
      path.add(nextVertex);
      traverseEdgesEP(path, graph, nextVertex);
    }
  }
  
//-------------------------------------------------------------
// getEulerPath(): method to find an euler path. returns a vector
// that contains a node sequence of an euler path. returns null
// if a path is not found. A key difference from finding an
// Euler circuit is remembering that the two odd-degree vertices
// will only be used for entry to and exit from the path. Therefore
// start at an odd degree vertex. Since we know exactly where to
// start and finish, there will be no sub-circuits.
// Author: Ye Eun Jeong
//-------------------------------------------------------------  
    
  public Vector<T> getEulerPath() { 
    
    //make a temporary copy of the graph
    GraphPlusPlus<T> tempGraph = clone();
    
    if(tempGraph.hasEulerPath()){
      
      //initialize variables
      T currentVertex=null;
      for(T v : this.vertices) {
        if(vertexDegree(v)%2==1){//start at an odd-degree vertex
          currentVertex = v;
          break; //find the starting vertex and break from loop
        }
      }
      Vector<T> p = new Vector<T>();
      p.add(currentVertex);
      
      if(tempGraph.n==1 && tempGraph.m()==0){ //if graph is a single island
        return null; //then that vertex technically has degree 0, which is even
                     //and therefore does not have an euler path.
     
      } else { //if it's not an island, start finding path
        
        traverseEdgesEP(p, tempGraph, currentVertex);
        return p;  
      }
    } else return null; //it path does not exist, returns null
  }

//-------------------------------------------------------------
// isConnected(): Uses dfsTraversal to maked a LinkedList of all nodes
// in a single component. Compares the length of this list to the number
// of total nodes in the graph. If these numbers do not match, graph
// is not complete and returns false. Else returns true. 
// Author: Ana Balcells
//-------------------------------------------------------------  

  public boolean isConnected() {
    T startVertex = getVertex(0); //uses first vertex in vertex array as starting point in traversal
    
    //uses dfsTraversal to create a LinkedList with all nodes in component of startVertex
    LinkedList<T> component = dfsTraversal(startVertex);
    
    //if the number of elements in components LinkedList != # nodes, returns false. Else returns true. 
    if (component.size() != n()) return false;
    else return true;
  }

//--------------------------------------------
// dfsTraversal(T startVertex): Returns a LinkedList containing a DEPTH-first search
// traversal of the graph starting at the given vertex. The result
// list should contain all vertices visited during the traversal in
// the order they were visited. throws exception if node not in graph
// Author: Ana Balcells
//--------------------------------------------
  public LinkedList<T> dfsTraversal(T startVertex) throws IllegalArgumentException {
    int currentVertex = getIndex(startVertex);
    //int to hold the index of current vertex, startVertex = start index
    LinkedStack<Integer> traversed = new LinkedStack<Integer>();
    //stack to hold the vertices which are passed through
    LinkedList<T> dfs = new LinkedList<T>();
    //list to hold items which have been visited 
    boolean[] visited = new boolean[this.n()];
    //one boolean/vertex. Records whether or not a vertex has been visited
    boolean found; //records whether or not a given vertex has been found yet
    
    if (!indexIsValid(currentVertex)) throw new IllegalArgumentException();
    //throws exception if vertex not in graph
    
    //populates boolean array with all values = false to start (none have been visited)
    for (int i = 0; i < this.n(); i++) {
      visited[i] = false;
    }
    
    //Adjusts all data structures to convey that startVertex has been visited
    traversed.push(currentVertex);
    dfs.add(getVertex(currentVertex));
    visited[currentVertex] = true;
    
    
    while (!traversed.isEmpty()) {
      //continues as long as there are vertices unvisited (have been passed through)
      currentVertex = traversed.peek(); //sets current index to index of top element
      found = false; //resets found to false since we haven't found currentVertex yet
      
      for (int index = 0; index < n() && !found; index++)
        //iterates through vertices array, int represents vertex index
        //completes following actions when there is a connection between the currentVertex
        //and vertex at index and when the vertex hasn't been visited yet
        if (isArc(currentVertex, index) && !visited[index]) { 
        traversed.push(index); //adds vertex index at index into the stack
        dfs.add(getVertex(index)); //adds the vertex to dfs 
        visited[index] = true; //changes boolean value of this vertex 
                               //so is indicated that has now been visited
        found = true; //since vertex has now been found, found = true
      }
      
      //if two conditions are true, removes vertex from stack since it has already been visited
      if (!found && !traversed.isEmpty()) traversed.pop(); 
    }
    
    return dfs; //returns list in dfs order
  }
 
  
//-------------------------------------------------------------
// traverseVertices: Private recursive helpder method, used to find
// an hamiltonian circuit. Not a destructive algorithm. Starting at
// a given start vertex, get its successors. Evaluate them in order
// to see if a successor is already included in the traversed path.
// If the vertex has not yet been traversed, then push it into the
// stack and add it to the traversal path. Recurse on that successor.
// When none of the successors of the current vertex are availalbe,
// pop off that current vertex from the stack, and remove it from the
// traversal path. This backtracks to the next point available for
// a viable traversal path. While pushing vertices into the stack,
// check whether the path is "full," that is, the number of vertices
// in the path is as big as the number of total vertices in the graph.
// If a path is full, then the current vertex must have the initial
// starting vertex as one of the successors--otherwise it cannot
// seal off the hamilton circuit, and has to be popped off the stack
// for more backtracking to another possible circuit.
// This is like going down, depth-first, a giant tree of possible
// sequence of nodes in a given graph, and is an exhaustive search.
// Author: Ye Eun Jeong
//-------------------------------------------------------------  
  
    private void traverseVertices(Stack<T> stack, Vector<T> path, T currentVertex, T start) {
      
      //Method remembers the original start vertex so it can check whether the last vertex
      //in the traversal path connects back to the start.
    
    //start evaluating current vertex's successors
    for(T v: getSuccessors(currentVertex)) {
      
      if(path.size() >=n) { //if traversal path is full,
                            //check whether current vertex connects back to start
        for(T v2: getSuccessors(currentVertex)) {
          if(v2.equals(start)){
            hamCircuitFound=true; //if so, then we have found the hamCircuit
            return;
          }
        }
        
      } else {
        if(!path.contains(v)){ //if v is not already in the traversal path,
          stack.push(v); //push it in the stack and add it to the path
          path.add(v);
          traverseVertices(stack,path,stack.peek(),start);
        }
      }
    }
    
    //if this testing statement is reached, that means that there were
    //no viable candidate vertices to be visited during the for-loop.
    if(!hamCircuitFound){
      stack.pop(); path.remove(currentVertex);
      //so pop off and remove from path the current vertex to backtrack.
    }
  }
  
//-------------------------------------------------------------
// traverseVerticesHP: private recursive helpder method, used to find
// an hamiltonian path. similar to traverseVertices method. The key
// difference from the hamCircuit finding algorithm is that we do not
// have to check whether the last vertex in a "full(in length)" path
// connects back to the start vertex.
// Author: Ye Eun Jeong
//-------------------------------------------------------------  

  private void traverseVerticesHP(Stack<T> stack, Vector<T> path, T currentVertex) {
    
    for(T v: getSuccessors(currentVertex)) {
      if(path.size() >=n) { //if path is full
        hamPathFound=true; //we have found the hamilton path.
        return;
      } else {
        if(!path.contains(v)){
          stack.push(v);
          path.add(v);
          //find a candidate vertex that has not yet been traversed,
          //push into stack and add to path. then recurse on that vertex.
          traverseVerticesHP(stack,path,stack.peek());
        }
      }
    }
    
    //if there were no viable candidate vertices,
    //and hamilton path is not yet found, pop off current vertex
    //and remove it from the path to backtrack.
    if(!hamPathFound){
      if(!stack.isEmpty()) //**to prevent errors
      stack.pop(); path.remove(currentVertex);
    }
  }  
  
  
//-------------------------------------------------------------
// getHamiltonianCircuit(): calculates a hamiltonian circuit and
// returns a vector containing the node sequence of the circuit.
// returns null if circuit not found.
// Author: Ye Eun Jeong
//-------------------------------------------------------------  
  public Vector<T> getHamiltonCircuit() {
    Vector<T> circuit = new Vector<T>();
    Stack<T> stack = new Stack<T>();
    T start = vertices[0]; //we can start at whatever vertex is at 0 of vertices array
    stack.push(start); //insert the start vertex onto the traversal stack and path
    circuit.add(start);
    
    //call recursive helpder method to traverse vertices in graph
    traverseVertices(stack, circuit, vertices[0], start);
    circuit.add(start); //seal off the circuit
    
    if(hamCircuitFound){
      hamCircuitFound=false; //reset boolean var as we exit method
      return circuit;
    } else {
      return null; //return null vector if path not found
    }
  }
  
//-------------------------------------------------------------
// getHamiltonianCircuit(): calculates a hamiltonian circuit and
// returns a vector containing the node sequence of the circuit.
// returns null if circuit not found.
// Author: Ye Eun Jeong
//-------------------------------------------------------------  
  public Vector<T> getHamiltonPath() {
    Vector<T> path = new Vector<T>();
    Stack<T> stack = new Stack<T>();
    T start = vertices[0];
    
    traverseVerticesHP(stack, path, start);
    
    if(hamPathFound){
      hamPathFound=false; //reset boolean var as we exit method
      return path;
    } else {
      return null; //return null vector if path not found
    }
}
  
//-------------------------------------------------------------  
// TESTING CODE BELOW
// Authors: Ana BalCells and Ye Eun Jeong
//-------------------------------------------------------------  

  public static void main (String[] args) {
//  try {
//    GraphPlusPlus<String> falseTest = fromFile("falseTest.tgf");
//    System.out.println("Is this graph connected? (false): " + falseTest.isConnected());
//  } catch (FileNotFoundException ex) {
//    System.out.println(ex);
//  }
//  try {
//    GraphPlusPlus<String> trueTest = fromFile("trueTest.tgf");
//    System.out.println("Is this graph connected? (true): " + trueTest.isConnected());
//  } catch (FileNotFoundException ex) {
//    System.out.println(ex);
//  }
//  try {
//    GraphPlusPlus<String> falseTest2 = fromFile("falseTest2.tgf");
//    System.out.println("Is this graph connected? (false): " + falseTest2.isConnected());
//  } catch (FileNotFoundException ex) {
//    System.out.println(ex);
//  }
//  try {
//    GraphPlusPlus<String> cs = fromFile("cs-courses.tgf");
//    System.out.println("Is this graph connected? (true): " + cs.isConnected());
//  } catch (FileNotFoundException ex) {
//    System.out.println(ex);
//  }
//  try {
//    GraphPlusPlus<String> csFalse = fromFile("cs-coursesFalse.tgf");
//    System.out.println("Is this graph connected? (false): " + csFalse.isConnected());
//  } catch (FileNotFoundException ex) {
//    System.out.println(ex);
//  }

//  try {
//    GraphPlusPlus<String> trueTest = fromFile("trueTest2.tgf");
//    System.out.println(trueTest); //testing for non-destructive algorithm
//    System.out.println("Is this graph empty? " + trueTest.isEmpty());
//    System.out.println("IsConnected? " + trueTest.isConnected());
//    System.out.println("IsAllEvenDegree? " + trueTest.isAllEvenDegrees());
//    System.out.println("Is this graph eulerian? " + trueTest.isEulerian());
//    System.out.println("Euler circuit of this graph: " + trueTest.getEulerCircuit());
//    System.out.println(trueTest); //testing for non-destructive algorithm
//  } catch (FileNotFoundException ex) {
//    System.out.println(ex);
//  }
    
//    try {
//      GraphPlusPlus<String> house = fromFile("house.tgf");
//      System.out.println(house); //testing for non-destructive algorithm
//      System.out.println("IsAllEvenDegree? " + house.isAllEvenDegrees());
//      System.out.println("HasTwoOddDegrees? " + house.hasTwoOddDegrees());
//      System.out.println("Get vertex degrees: " + house.getVertexDegrees());
//      System.out.println("Does it have an Euler path? " + house.hasEulerPath());
//      System.out.println("Is this graph eulerian? " + house.isEulerian());
//      System.out.println("Euler path of this graph: " + house.getEulerPath());
//      System.out.println(house); //testing for non-destructive algorithm
//    } catch (FileNotFoundException ex) {
//      System.out.println(ex);
//    }
    
//    try {
//      GraphPlusPlus<String> eulerPathTest = fromFile("eulerPathTest.tgf");
//      System.out.println(eulerPathTest);
//      System.out.println("IsAllEvenDegree? " + eulerPathTest.isAllEvenDegrees());
//      System.out.println("HasTwoOddDegrees? " + eulerPathTest.hasTwoOddDegrees());
//      System.out.println("Get vertex degrees: " + eulerPathTest.getVertexDegrees());
//      System.out.println("Does it have an Euler path? " + eulerPathTest.hasEulerPath());
//      System.out.println("Is this graph eulerian? " + eulerPathTest.isEulerian());
//      System.out.println("Euler path of this graph: " + eulerPathTest.getEulerPath());
//      System.out.println(eulerPathTest);
//    } catch (FileNotFoundException ex) {
//      System.out.println(ex);
//    }
    
//    try{
//      GraphPlusPlus<String> hctest = fromFile("hctest.tgf");
//      System.out.println("Find hamilton circuit: " + hctest.getHamiltonCircuit());
//      System.out.println("Find hamilton path   : " + hctest.getHamiltonPath());
//      System.out.println(hctest);
//    } catch (FileNotFoundException ex) {
//      System.out.println(ex);
//    }

//    try{
//      GraphPlusPlus<String> cycle3test = fromFile("cycle3.tgf");
//      System.out.println(cycle3test);
//      System.out.println("Find hamilton circuit: " + cycle3test.getHamiltonCircuit());
//      System.out.println(cycle3test);
//    } catch (FileNotFoundException ex) {
//      System.out.println(ex);
//    }
    
//    try{
//      GraphPlusPlus<String> hptest = fromFile("hptest.tgf");
//      System.out.println("Find hamilton circuit: " + hptest.getHamiltonCircuit());
//      System.out.println("Find hamilton path   : " + hptest.getHamiltonPath());
//    } catch (FileNotFoundException ex) {
//      System.out.println(ex);
//    }
    
//    try{
//      GraphPlusPlus<String> hptest2 = fromFile("hptest2.tgf");
//      System.out.println("Find hamilton circuit: " + hptest2.getHamiltonCircuit());
//      System.out.println("Find hamilton path   : " + hptest2.getHamiltonPath());
//    } catch (FileNotFoundException ex) {
//      System.out.println(ex);
//    }
    
}

}//end of GraphPlus class