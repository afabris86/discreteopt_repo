package edu.aa12;

import java.util.ArrayList;
import java.util.List;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class CPLEXSolver {

	private Graph graph;
	private IloCplex model;
	private int numEdges;
	private int numVertecies;
	private IloNumVar[] vars;
	
	public double minimumValue;
	public List<Edge> tour = new ArrayList<Edge>();
	
	public CPLEXSolver(Graph g){
		graph = GetSymmetricGraph(g);
		numEdges = graph.edges.size();
		numVertecies = graph.getVertices();
		
		model = GetModel();
		SolveModel();
	}
	private Graph GetSymmetricGraph(Graph g) {
		LocalGraphImp result = new LocalGraphImp(g.vertexCoords);
		for(Edge e : g.edges)
			result.createEdge(e.u, e.v);
		return result;
	}
	private void SolveModel() {
		try {
			model.solve();
			minimumValue = model.getObjValue();
			
			for(int i = 0; i < numEdges; i++){
				if(model.getValue(vars[i]) == 0)
					continue;
				tour.add(graph.edges.get(i));
			}
			
			model.end();
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private IloCplex GetModel() {
		try {
			IloCplex result = new IloCplex();
			vars = result.boolVarArray(numEdges);
			
			// Create objective function
			IloLinearNumExpr objective = result.linearNumExpr();
			for(int i = 0; i < numEdges; i++){
				Edge edge = graph.edges.get(i);
				objective.addTerm(graph.getDistance(edge.u, edge.v), vars[i]);
			}
			result.addMinimize(objective);
			
			// All vertex must be entered once
			Edge edge;
			for(int j = 0; j < numVertecies; j++){
				IloLinearNumExpr constrain = result.linearNumExpr();
				for(int k = 0; k<numEdges ; k++){ //Have to loop this way due to the override of equal opperator on edge
					edge = graph.edges.get(k);
					if(edge.u != j) continue;
					constrain.addTerm(1.0, vars[k]);
				}
				result.addEq(constrain, 1.0);
			}
			
			// All vertex must be left exactly once
			for(int i = 0; i < numVertecies; i++){
				IloLinearNumExpr constrain = result.linearNumExpr();
				for(int k = 0; k<numEdges ; k++){ //Have to loop this way due to the override of equal opperator on edge
					edge = graph.edges.get(k);
					if(edge.v != i) continue;
					constrain.addTerm(1.0, vars[k]);
				}
				result.addEq(constrain, 1.0);
			}
			
			//Add sub tour constrain
			int edgeIndex;
			IloNumVar[] dummyVars = result.numVarArray(numVertecies, 0, Double.MAX_VALUE);
			for(int i=1; i<numVertecies; i++){
				for(int j=1; j<numVertecies; j++ ){
					if(j==i) continue;
					
					edgeIndex = Integer.MIN_VALUE;
					for(int k = 0; k<numEdges ; k++){ //Have to loop this way due to the override of equal opperator on edge{
						edge = graph.edges.get(k);
						if(edge.u != i || edge.v != j) continue;
						edgeIndex = k;
						break;
					}
					
					if(edgeIndex == Integer.MIN_VALUE) continue;
					
					IloLinearNumExpr constrain = result.linearNumExpr();
					constrain.addTerm(1.0, dummyVars[i]);
					constrain.addTerm(-1.0, dummyVars[j]);
					constrain.addTerm(numVertecies-1, vars[edgeIndex]);
					result.addLe(constrain, numVertecies -2);
				}
			}
			
			return result;
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private  class LocalGraphImp extends Graph{
		LocalGraphImp(double[][] coords) {
			super(coords);
		}
		
		public void createEdge(int i, int j){
			super.createEdge(i, j);
			Edge e = new Edge(j,i);
			edges.add(e);
			incidentEdges[i].add(e);
			incidentEdges[j].add(e);
		}
	}
}
