package edu.aa12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OneTreeLb{
	
	Graph graph;
	BnBNode node;
	
	private final Kruskal kruskal = new Kruskal();
	
	public OneTreeLb(Graph graph, BnBNode node){
		this.graph = graph;
		this.node = node;
	}
	
	public double computeLbLag(){
		double[][] nxtVertexCoords = new double[graph.vertexCoords.length - 1][2];
		double currrentSolution = 0;
		
		int k = 0;
		for(int i = 1 ; i < graph.vertexCoords.length ; i++ ){
			nxtVertexCoords[i-1][0] = graph.vertexCoords[i][0];
			nxtVertexCoords[i-1][1] = graph.vertexCoords[i][1];
		}
		
		GraphImp mod = new GraphImp(nxtVertexCoords, this.graph, this.node);
		
		List<Edge> minSpanTree = kruskal.minimumSpanningTree(mod, node,true);
		// we have to increment the edge index
		for(Edge e: minSpanTree){
			e.u++;
			e.v++;
		}
		
		List<Edge> cheapest = mod.findCheapest();
		
		minSpanTree.addAll(cheapest);
		minSpanTree.addAll(mod.includedEdgesWithoutOneVertex);
		
		//TODO: Delete but nice for debugging
		//System.out.println(this.node.toString());
		//PrintRoute(minSpanTree);
		
		// Assert that num edges = num veterex 
		
		currrentSolution = GetCost(minSpanTree);
		
		// Assert cost > 0
		
		//while(true){
		//	mod.updateCost(minSpanTree);
		//	minSpanTree = kruskal.minimumSpanningTree(mod, node);
		//	cheapest = mod.findCheapest();
		//	minSpanTree.addAll(cheapest);
		//	
		//	currrentSolution = GetCost(minSpanTree);
		//}
		
		return currrentSolution;
	}
	
	private double GetCost(List<Edge> path){
		//PrintRoute(path);
		double result = 0;
		for(Edge e : path){
			result = result + graph.getDistance(e.u, e.v);
		}
		return result;
	}
	
	private void PrintRoute(List<Edge> path){
		System.out.println("");
		for(Edge e : path){
			System.out.println(" " + e.u + "<->" + e.v + " ");
		}
	}
}


