package edu.aa12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Utility {
	// Remember to turn on assertions: http://stackoverflow.com/questions/5509082/eclipse-enable-assertions
	public static final boolean IsDebug = false;
	public static final boolean IsSettingUpperBound = false;
	public static final int NumLBCallCalls = 100;
	
	public static void PrintRoute(List<Edge> path){
		String output = "";
		for(Edge e : path) output += " | " + e.u + "<->" + e.v;
		System.out.println(output);
	}
	
	public static boolean IsATour(Graph g, List<Edge> path){
		if(g.vertexCoords.length != path.size()) return false;
		if(new HashSet(path).size() < path.size()) return false;
		
		int[] degrees = new int[path.size()];
		
		for(Edge e : path){
			degrees[e.u]++;
			degrees[e.v]++;
			if(degrees[e.u] > 2 || degrees[e.v] > 2) return false;
		}
		
		return true;
	}
	
	public static boolean IsSpanningTree(Graph g, List<Edge> path){
		if(g.vertexCoords.length -1 != path.size()) return false;
		if(new HashSet(path).size() < path.size()) return false;
		
		List<Edge> potentialEdges = new ArrayList<Edge>(path);
		List<Edge> incidentEdges = new ArrayList<Edge>();
		List<Edge> deepestEdges = new ArrayList<Edge>();
		
		for(Edge e : potentialEdges) 
			if (e.u == 0 ||e.v == 0) deepestEdges.add(e);
		potentialEdges.removeAll(deepestEdges);
		
		do{
			incidentEdges.clear();
			
			for(Edge e : deepestEdges){
				for(Edge innerEdge: potentialEdges){
					if ((e.u == innerEdge.u || e.u == innerEdge.v) || 
							(e.v == innerEdge.u || e.v == innerEdge.v)){
						incidentEdges.add(innerEdge);
					}
				}
			}
			
			potentialEdges.removeAll(incidentEdges);
			deepestEdges.clear();
			deepestEdges.addAll(incidentEdges);
		} while(incidentEdges.size() > 0);
		
		return potentialEdges.size() == 0;
	}
	
	public static double GetCost(List<Edge> path, Graph g){
		double result = 0;
		for(Edge e : path){
			result += g.getDistance(e.u, e.v);
		}
		
		if(Utility.IsDebug)
			assert (result > 0) : "Computed LB of zero";
		
		return result;
	}
}