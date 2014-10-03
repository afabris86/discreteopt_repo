package edu.aa12;

import java.util.HashSet;
import java.util.List;

public class Utility {
	// Remember to turn on assertions: http://stackoverflow.com/questions/5509082/eclipse-enable-assertions
	public static final boolean IsDebug = true;
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
}