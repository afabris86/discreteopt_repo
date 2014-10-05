package edu.aa12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class NearestNeighbour {
		/*
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
		/*
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
	}*/
	
	public static class Path{
		public int head;
		public int tail;
		public List<Integer> intermediateVertercies = new ArrayList<Integer>();
		
		public Path(int startingIndex, List<Edge> includedEdges, boolean[] isPartOfAPath){
			int initialNumIncident;
			isPartOfAPath[startingIndex] = true;
			
			List<Edge> incidentEdges = GetIncidentEdges(includedEdges,startingIndex,2);
			initialNumIncident = incidentEdges.size(); 
			if(initialNumIncident == 0){
				head = tail = startingIndex;
				return;
			}
			
			Edge edge = incidentEdges.get(0);
			Edge tailEdge = (initialNumIncident == 2) ? incidentEdges.get(1) : null;
			int previousHead = startingIndex;
			head = (edge.u == startingIndex) ? edge.v : edge.u;
			isPartOfAPath[head] = true;
			
			//System.out.println("Yo? s: " + startingIndex + " t: " + head);
			
			do{
				incidentEdges = GetIncidentEdges(includedEdges,head,1,previousHead);
				if(incidentEdges.size() == 0) break;
				edge = incidentEdges.get(0);
				intermediateVertercies.add(head);
				previousHead = head;
				head = (edge.u == head) ? edge.v : edge.u;
				isPartOfAPath[head] = true;
				
				//System.out.println("Yo/ prev: " + previousHead + " this: " + head);
			}while(incidentEdges.size() > 0);
			
			if(tailEdge == null) return;
			
			intermediateVertercies.add(startingIndex);
			edge = tailEdge;
			int previousTail = startingIndex;
			tail = (edge.u == startingIndex) ? edge.v : edge.u;
			isPartOfAPath[tail] = true;
			
			//System.out.println("Yo s: " + startingIndex + " t: " + tail);
			
			do{
				incidentEdges = GetIncidentEdges(includedEdges,tail,1,previousTail);
				if(incidentEdges.size() == 0) break;
				edge = incidentEdges.get(0);
				intermediateVertercies.add(tail);
				previousTail = tail;
				tail = (edge.u == tail) ? edge.v : edge.u;
				isPartOfAPath[tail] = true;
				
				//System.out.println("Yo: prev: " + previousTail + " this: " + tail);
			}while(incidentEdges.size() > 0);
		}

		public void Merge(Path otherPath, Edge connectingEdge) {
			this.intermediateVertercies.addAll(otherPath.intermediateVertercies);
			if(connectingEdge.ConnectsVertercies(this.head,otherPath.head)){
				if(this.head != this.tail)
					intermediateVertercies.add(this.head);
				if(otherPath.head != otherPath.tail)
					intermediateVertercies.add(otherPath.head);
				this.head = otherPath.tail;
			}
			else if (connectingEdge.ConnectsVertercies(this.head,otherPath.tail)){
				if(this.head != this.tail)
					intermediateVertercies.add(this.head);
				if(otherPath.head != otherPath.tail)
					intermediateVertercies.add(otherPath.tail);
				this.head = otherPath.head;
			}
			else if(connectingEdge.ConnectsVertercies(this.tail,otherPath.head)){
				if(this.head != this.tail)
					intermediateVertercies.add(this.tail);
				if(otherPath.head != otherPath.tail)
					intermediateVertercies.add(otherPath.head);
				this.tail = otherPath.tail;
			}
			else if (connectingEdge.ConnectsVertercies(this.tail,otherPath.tail)){
				if(this.head != this.tail)
					intermediateVertercies.add(this.tail);
				if(otherPath.head != otherPath.tail)
					intermediateVertercies.add(otherPath.tail);
				this.tail = otherPath.head;
			}
		}
		
		public String toString(){
			return String.format("Path[%d-%d w " + intermediateVertercies.toString() + "]",head,tail);
		}

		public boolean CanMutuallyConnectOrCannot(List<Edge> potentialEdges, Path other){
			int canConnectHeadAndHead = 0;
			int canConnectHeadAndTail = 0;
			int canConnectTailAndHead = 0;
			int canConnectTailAndTail = 0;
			
			for(Edge e: potentialEdges){
				if(e.ConnectsVertercies(this.head, other.head)){
					canConnectHeadAndHead = 1;
				}
				if (e.ConnectsVertercies(this.head, other.tail)){
					canConnectHeadAndTail = 1;
				}
				if (e.ConnectsVertercies(this.tail, other.head)){
					canConnectTailAndHead = 1;
				}
				if (e.ConnectsVertercies(this.tail, other.tail)){
					canConnectTailAndTail = 1;
				}
				
				if(canConnectHeadAndHead + canConnectTailAndTail == 2)
					return true;
				
				if(canConnectHeadAndHead + canConnectHeadAndTail + canConnectTailAndHead + canConnectTailAndTail > 2)
					return true;
			}
			
			if(canConnectHeadAndHead + canConnectHeadAndTail + canConnectTailAndHead + canConnectTailAndTail < 1)
				return true;
			
			return false;
		}
	}
	
	/** Returns null if there is not found a tour */
	public static List<Edge> GetTour(Graph g, BnBNode node){
		List<Path> paths = new ArrayList<Path>();
		boolean[] isPartOfAPath = new boolean[g.getVertices()];
		
		List<Edge> potentialEdges = new ArrayList<Edge>(g.edges);
		List<Edge> includedEdges = new ArrayList<Edge>();
		List<Edge> result = new ArrayList<Edge>();
		List<Integer> intermediateVertercies = new ArrayList<Integer>();
		
		//System.out.println(potentialEdges.toString());
		
		BnBNode n = node;
		while(n.parent!=null){
			potentialEdges.remove(n.edge);
			if(n.edgeIncluded) includedEdges.add(n.edge);
			n = n.parent;
		}
		
		//System.out.println(includedEdges.toString());
		
		// Create all paths
		for(int i = 0; i < g.getVertices();i++){
			if(isPartOfAPath[i]) continue;
			paths.add(new Path(i, includedEdges, isPartOfAPath));
		}
		
		// Remove redundant potential paths
		for(Path path : paths)
			intermediateVertercies.addAll(path.intermediateVertercies);
		for(int i=0; i < potentialEdges.size(); i++)
		{
			Edge e = potentialEdges.get(i);
			if(intermediateVertercies.contains(e.u) || 
					intermediateVertercies.contains(e.v)){
				potentialEdges.remove(e);
				i--;
			}
		}
		
		Path currentPath = paths.get(0);
		Edge connectingEdge;
		boolean foundConnectingEdge;
		double cheapestVal;
		Path cheapestPath;
		Edge cheapestEdge;
		Edge e;
		
		//System.out.println(paths.toString());
		//System.out.println(currentPath.toString());
		while(paths.size() > 1){
			foundConnectingEdge = false;
			
			// See if any paths have to be connected
			for(Path innerPath : paths){
				if(currentPath == innerPath) continue;
				if(innerPath.CanMutuallyConnectOrCannot(potentialEdges, currentPath)) continue;
				
				//System.out.println("Needs to connect with: " + innerPath.toString());
				connectingEdge = GetCheapestEdge(potentialEdges,innerPath,currentPath,g);
				result.add(connectingEdge);
				currentPath.Merge(innerPath,connectingEdge);
				paths.remove(innerPath);
				foundConnectingEdge = true;
				
				//Utility.PrintRoute(result);
				//System.out.println("Added " + connectingEdge.toString() + " due to accesability lag. The path went from " + innerPath.head + " to " + innerPath.tail);
				
				// Remove edges of cheapest path and current
				//TODO: Was lazy. At mose there are only two new intermediate vertex which we need to check. I.e. redundant loops are performed ...
				for(int i=0; i < potentialEdges.size(); i++)
				{
					e = potentialEdges.get(i);
					if(currentPath.intermediateVertercies.contains(e.u) || 
							currentPath.intermediateVertercies.contains(e.v)){
						potentialEdges.remove(e);
						i--;
					}
				}
				
				break;
			}
			if(foundConnectingEdge) continue;
			
			// Find the cheapest path to connect with 
			cheapestVal = Double.POSITIVE_INFINITY;
			cheapestPath = null;
			cheapestEdge = null;
			for(Path innerPath : paths){
				if(currentPath == innerPath) continue;
				Edge pathEdge = GetCheapestEdge(potentialEdges, innerPath, currentPath, g,false);
				if(pathEdge == null) continue;
				double val = g.getDistance(pathEdge.u, pathEdge.v);
				if(val < cheapestVal){
					cheapestPath = innerPath;
					cheapestEdge = pathEdge;
					cheapestVal = val;
				}
			}
			
			if(cheapestEdge == null || cheapestPath == null){
				if(Utility.IsDebug)
					System.out.println("Did not find cheapest edge or cheapest val!");
				result.addAll(includedEdges);
				//Visualization.visualizeSolution(g, result);
				//assert (false) : "Stuff failed. Is it an error?"; //TODO
				return null;
			}
				
			result.add(cheapestEdge);
			currentPath.Merge(cheapestPath, cheapestEdge);
			paths.remove(cheapestPath);
			
			//Utility.PrintRoute(result);
			//System.out.println("Added " + cheapestPath.toString() + " cuz it was cheapest");
			
			// Remove edges of cheapest path and current
			//TODO: Was lazy. At most there are only two new intermediate vertex which we need to check. I.e. redundant loops are performed ...
			for(int i=0; i < potentialEdges.size(); i++)
			{
				e = potentialEdges.get(i);
				if(currentPath.intermediateVertercies.contains(e.u) || 
						currentPath.intermediateVertercies.contains(e.v)){
					potentialEdges.remove(e);
					i--;
				}
			}
		}
		
		result.addAll(includedEdges);
		
		if(Utility.IsDebug && potentialEdges.size() > 1){
			System.out.println(potentialEdges.size());
			Utility.PrintRoute(result);
			Utility.PrintRoute(potentialEdges);
			Visualization.visualizeSolution(g, result);
			assert (false) : "Had more than one edge left in NN after while loop";
		}
			
		result.addAll(potentialEdges);
		
		if(!Utility.IsATour(g, result)){
			if(Utility.IsDebug){
				System.out.println("NN did not give a tour");
				//Visualization.visualizeSolution(g, Utility.GetFinalNode(result));
			}
			
			//assert (false) : "Stuff failed. Is it an error?"; //TODO
			return null;
		}
		
		return result;
	}
	
	private static Edge GetCheapestEdge(List<Edge> potentialEdges,Path innerPath, Path currentPath,Graph g)
	{
		return GetCheapestEdge(potentialEdges, innerPath, currentPath, g,true);
	}
	
	/** NOTE: Removes edges from potential edges!!! */
	private static Edge GetCheapestEdge(List<Edge> potentialEdges,Path innerPath, Path currentPath,Graph g, boolean isRemovingEdges) {
		double cheapestDistance = Double.POSITIVE_INFINITY;
		Edge e;
		Edge result = null;
		for(int i = 0; i < potentialEdges.size(); i++){
			e = potentialEdges.get(i);
			if(!IsEdgeConnectingPaths(e,innerPath,currentPath)) continue;
			if(g.getDistance(e.u, e.v) < cheapestDistance){
				result = e;
				cheapestDistance = g.getDistance(e.u, e.v);
			}
			
			if(isRemovingEdges){
				//System.out.println("Removed edge: " + e.toString());
				potentialEdges.remove(e);
				i--;
			}
		}
		
		return result;
	}

	private static boolean IsEdgeConnectingPaths(Edge e, Path innerPath,
			Path currentPath) {
		return e.ConnectsVertercies(innerPath.head, currentPath.head) || 
				e.ConnectsVertercies(innerPath.tail, currentPath.head) ||
				e.ConnectsVertercies(innerPath.head, currentPath.tail) ||
				e.ConnectsVertercies(innerPath.tail, currentPath.tail);
	}
	
	private static List<Edge> GetIncidentEdges(List<Edge> edges, int vertexIndex, int maxMatch){
		return GetIncidentEdges(edges, vertexIndex, maxMatch, Integer.MIN_VALUE);
	}

	private static List<Edge> GetIncidentEdges(List<Edge> edges, int vertexIndex, int maxMatch, int notAllowedVertex){
		List<Edge> result = new ArrayList<Edge>();
		for(Edge e : edges){
			if(e.u == vertexIndex || e.v == vertexIndex){
				if(e.u == notAllowedVertex || e.v == notAllowedVertex) continue;
				
				result.add(e);
				if(result.size() >= maxMatch) 
					break;
			}
		}
		return result;
	}
}
