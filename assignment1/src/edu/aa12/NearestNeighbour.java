package edu.aa12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class NearestNeighbour {
	
	/** Initially I thought it would when the some notes was fixed, some excluded or when there are not edges between all notes. However, this obviously does not work! 
	 * @throws Exception */
	public static List<Edge> GetTour(Graph g, BnBNode n) throws Exception{
		List<Edge> result = new ArrayList<Edge>();
		
		List<Edge> potentialEdges = new ArrayList<Edge>(g.edges);
		List<Edge> includedEdges = new ArrayList<Edge>();
		
		List<Edge> newEdges = new ArrayList<Edge>();
		List<Edge> tailEdges = new ArrayList<Edge>();
		
		Edge nextEdge;
		int headIndex;
		int tailIndex;
		double currentMin;
		
		while(n.parent!=null){
			potentialEdges.remove(n.edge);
			if(n.edgeIncluded)
				includedEdges.add(n.edge);
			n = n.parent;
		}
		
		// First find the tree that zero vertex is a part of
		result = GetIncidentEdges(includedEdges,0,2);
		includedEdges.removeAll(result);
		
		if(result.size() == 0){
			headIndex = tailIndex =0;
		}
		else if (result.size() == 1){
			tailIndex = 0;
			System.out.println("Yo");
			// Step as fare as possible in the one possible direction
			headIndex = (result.get(0).u == 0) ? result.get(0).v : result.get(0).u;
			do{
				newEdges.clear();
				newEdges = GetIncidentEdges(includedEdges,headIndex,1);
				if(newEdges.size() == 0) break;
				
				nextEdge = newEdges.get(0);
				includedEdges.remove(nextEdge);
				result.add(nextEdge);
				
				// Remove potential edges with previous head index
				if(headIndex != 0){
					for(Iterator<Edge> i = potentialEdges.iterator();i.hasNext(); ){
						Edge e = i.next();
						if(e.u == headIndex || e.v == headIndex)
							i.remove();
					}
				}
				
				headIndex = (nextEdge.u == headIndex) ? nextEdge.v : nextEdge.u;
			} while(newEdges.size() > 0);
		}
		else{
			// Step as fare as possible in both direction
			headIndex = (result.get(0).u == 0) ? result.get(0).v : result.get(0).u;
			tailIndex = (result.get(1).u == 0) ? result.get(1).v : result.get(1).u;
			
			do{
				newEdges.clear();
				newEdges = GetIncidentEdges(includedEdges,headIndex,1);
				if(newEdges.size() == 0) break;
				
				nextEdge = newEdges.get(0);
				includedEdges.remove(nextEdge);
				result.add(nextEdge);
				
				// Remove potential edges with previous head index
				if(headIndex != 0){
					for(Iterator<Edge> i = potentialEdges.iterator();i.hasNext(); ){
						Edge e = i.next();
						if(e.u == headIndex || e.v == headIndex)
							i.remove();
					}
				}
				
				headIndex = (nextEdge.u == headIndex) ? nextEdge.v : nextEdge.u;
			} while(newEdges.size() > 0);
			do{
				newEdges.clear();
				newEdges = GetIncidentEdges(includedEdges,tailIndex,1);
				if(newEdges.size() == 0) break;
				
				nextEdge = newEdges.get(0);
				includedEdges.remove(nextEdge);
				result.add(nextEdge);
				
				// Remove potential edges with previous tail index
				if(tailIndex != 0){
					for(Iterator<Edge> i = potentialEdges.iterator();i.hasNext(); ){
						Edge e = i.next();
						if(e.u == tailIndex || e.v == tailIndex)
							i.remove();
					}
				}
				
				tailIndex = (nextEdge.u == tailIndex) ? nextEdge.v : nextEdge.u;
			} while(newEdges.size() > 0);
		}
		
		// Make list of "tailedges"
		tailEdges = GetIncidentEdges(potentialEdges, tailIndex, Integer.MAX_VALUE);
		
		/*Utility.PrintRoute(result);
		System.out.println("Initial head: " + headIndex + ", tail: " + tailIndex);
		Utility.PrintRoute(tailEdges);*/
		
		//Find the missing edges in tour
		do{
			nextEdge = null;
			// First check if edges is all ready included
			for(Edge e: includedEdges){
				if(e.u == headIndex || e.v == headIndex){
					nextEdge = e;
					includedEdges.remove(nextEdge);
					break;
				}
			}
			
			// Else find the cheapest 
			if(nextEdge == null){
				currentMin = Double.POSITIVE_INFINITY;
				for(Edge e: g.incidentEdges[headIndex]){
					if(!potentialEdges.contains(e) || g.getDistance(e.u, e.v) > currentMin) 
						continue;
					if((headIndex != tailIndex) && (e.u == tailIndex || e.v == tailIndex))
						continue;
					
					currentMin = g.getDistance(e.u, e.v);
					nextEdge = e;
				}
				potentialEdges.remove(nextEdge);
			}
			if(headIndex != tailIndex){
				for(Edge e: g.incidentEdges[headIndex])
					potentialEdges.remove(e);
			}
			
			result.add(nextEdge);
			headIndex = (nextEdge.u == headIndex) ? nextEdge.v : nextEdge.u;
		} while(result.size() < g.vertexCoords.length - 1);
		
		
		nextEdge = null;
		// Add the cheapest or included one between head and tail
		for(Edge e : tailEdges) {
			if((e.u == headIndex && e.v == tailIndex) || 
					(e.u == tailIndex && e.v == headIndex)){
				nextEdge = e;
				break;
			}
		}
		
		if(nextEdge == null)
			throw new Exception("Could finish route in NearestNeighbour between last two edges. It is due to restriction from node or avialable edges in graph");
		
		result.add(nextEdge);
		
		if(Utility.IsDebug){
			System.out.println("Found the following route with ub of : " + Utility.GetCost(result, g));
			Utility.PrintRoute(result);
		}
		
		if(Utility.IsDebug)
			if(!Utility.IsATour(g, result)){
				int[] degree = new int[g.vertexCoords.length];
				for(Edge e : result){
					degree[e.u]++;
					degree[e.v]++;
				}
				
				for(int i =0; i < degree.length; i++){
					if(degree[i] == 2) continue;
					System.out.println("Vertex " + i + " had degree " + degree[i] + " in NearestNeighbour result");
				}
				
				assert (false) : "Result in NearestNeighbour is not a tour";
			}
		
		return result;
	}
	
	
	private static List<Edge> GetIncidentEdges(List<Edge> edges, int vertexIndex, int maxMatch){
		List<Edge> result = new ArrayList<Edge>();
		for(Edge e : edges){
			if(e.u == vertexIndex || e.v == vertexIndex){
				result.add(e);
				if(result.size() >= maxMatch) 
					break;
			}
		}
		return result;
	}
}
