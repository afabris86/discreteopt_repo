package edu.aa12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OneTreeLb{
	
	Graph graph;
	BnBNode node;
	public boolean FoundTour;
	
	private final Kruskal kruskal = new Kruskal();
	
	private static int numLBCallCalls = 0;
	private final int maxNumLPRelaxation = 10;
	
	public OneTreeLb(Graph graph, BnBNode node){
		this.graph = graph;
		this.node = node;
	}
	
	public double computeLbLag(){
		double[][] nxtVertexCoords = new double[graph.vertexCoords.length - 1][2];
		double previousLB = 0;
		double currentLB = 0;
		int numLPRelaxation = 1;
		List<Edge> currentEdges;
		
		int k = 0;
		for(int i = 1 ; i < graph.vertexCoords.length ; i++ ){
			nxtVertexCoords[i-1][0] = graph.vertexCoords[i][0];
			nxtVertexCoords[i-1][1] = graph.vertexCoords[i][1];
		}
		
		GraphImp mod = new GraphImp(nxtVertexCoords, this.graph, this.node);
		currentEdges = GetOneTree(mod);
		currentLB = mod.GetCost(currentEdges);
		
		double upperbound = node.GetUpperBound();
		while(upperbound != Double.POSITIVE_INFINITY && 
				numLPRelaxation <= this.maxNumLPRelaxation && 
				!Utility.IsATour(graph, currentEdges)){
			numLPRelaxation++;
			previousLB = currentLB;
			
			if(numLBCallCalls % Utility.NumLBCallCalls == 0)
				mod.updateCost(currentEdges,false, upperbound);
			else
				mod.updateCost(currentEdges, upperbound);
			
			currentEdges = GetOneTree(mod);
			currentLB = mod.GetCost(currentEdges);
			
			if(Utility.IsDebug && numLBCallCalls % Utility.NumLBCallCalls == 0)
				System.out.println("Itreation: " + numLPRelaxation + ", previous LB: " + previousLB + ", new LB: " + currentLB);
		}
		
		
		if(Utility.IsDebug){
			numLBCallCalls++;
			if(numLBCallCalls % Utility.NumLBCallCalls == 0)
				System.out.println("Num calls to computeLbLag: " + numLBCallCalls + ". The lb is: " + currentLB);
		}
		
		FoundTour = Utility.IsATour(graph, currentEdges);
		
		return currentLB;
	}
	
	private List<Edge> GetOneTree(GraphImp g){
		List<Edge> oneTree = new ArrayList<Edge>();
		List<Edge> mstEdges = kruskal.minimumSpanningTree(g, this.node,true);
		
		for(Edge e : g.includedEdgesWithoutOneVertex)
			mstEdges.add(new Edge(e.u -1, e.v -1));
		
		if(Utility.IsDebug)
			assert(Utility.IsSpanningTree(g, mstEdges)) : "GetOneTree did not make a spanning tree";
		
		// we have to increment the edge index
		for(Edge e: mstEdges) oneTree.add(new Edge(e.u+1,e.v+1));
		
		oneTree.addAll(g.findCheapestEdgesToOneVertex());
		
		if(Utility.IsDebug)
			assert (this.graph.vertexCoords.length == oneTree.size()) : "Number of notes did not match did not match in computeLbLag with number of vertecies";
		
		return oneTree;
	}
}


