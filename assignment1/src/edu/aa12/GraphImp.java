package edu.aa12;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public  class GraphImp extends Graph{
	
	BnBNode node;
	public List<EdgeWithDistance> allOneVertexEdges = new ArrayList<EdgeWithDistance>();
	public List<EdgeWithDistance> potentialOneVertexEdges = new ArrayList<EdgeWithDistance>();
	public List<Edge> includedEdgesWithoutOneVertex = new ArrayList<Edge>();
	public List<EdgeWithDistance> includedEdgesWithOneVertex = new ArrayList<EdgeWithDistance>();
	
	public GraphImp(double[][] nxtVertexCoords, Graph g, BnBNode n){
		super(nxtVertexCoords);
		
		node = n;
		
		// Add edges
		for(Edge e: g.edges){
			if(e.u == 0 || e.v == 0){
				potentialOneVertexEdges.add(new EdgeWithDistance(e, g.getDistance(e.u, e.v)));
				continue;
			}
			
			createEdge(e.u - 1, e.v - 1);
		}
		
		//TODO: Delete
		//System.out.println("Edges in initial graph: " + g.edges.toString());
		//System.out.println("Edges in with one vertex: " + this.potentialOneVertexEdges.toString());
		//System.out.println("Edges in new graph: " + this.edges.toString());

		// Add potential or included edges to one vertex
		while(node.parent != null){
			if(node.edge.v != 0 && node.edge.u != 0){
				if(node.edgeIncluded)
					includedEdgesWithoutOneVertex.add(node.edge);
			}else{
				if(node.edgeIncluded)
					includedEdgesWithOneVertex.add(new EdgeWithDistance(node.edge, g.getDistance(node.edge.u, node.edge.v)));
				
				potentialOneVertexEdges.remove(node.edge);
			}
			
			node = node.parent;
		}
		
		allOneVertexEdges.addAll(includedEdgesWithOneVertex);
		allOneVertexEdges.addAll(potentialOneVertexEdges);
	}
	
	public List<Edge> findCheapestEdgesToOneVertex(){
		ArrayList<Edge> minimal = new ArrayList<Edge>();
		
		if(includedEdgesWithOneVertex.size() == 2){
			minimal.add(includedEdgesWithOneVertex.get(0));
			minimal.add(includedEdgesWithOneVertex.get(1));
			return minimal;
		}
		
		List<EdgeWithDistance> edges = new ArrayList<EdgeWithDistance>(potentialOneVertexEdges);
		Edge first = Collections.min(edges);
		minimal.add(first);
		
		if(includedEdgesWithOneVertex.size() == 1){
			minimal.add(includedEdgesWithOneVertex.get(0));
			return minimal;
		}
		
		edges.remove(first);
		minimal.add(Collections.min(edges));
		return minimal;
	}
	
	public double GetCost(List<Edge> path, Graph g){
		//System.out.println(path.toString());
		double result = 0;
		for(Edge e : path){
			result += g.getDistance(e.u, e.v);
		}
		
		if(Utility.IsDebug)
			assert (result > 0) : "Computed LB of zero";
		
		return result;
	}
	
	/* NOT WORKING
	public double GetCost(List<Edge> path){
		System.out.println(path.toString());
		
		double result = 0;
		for(Edge e : path){
			if(e.u==0 || e.v ==0){
				int notOneVertexIndex = (e.u==0) ? e.v : e.u; 
				for(EdgeWithDistance innerEdge : allOneVertexEdges){
					if(innerEdge.u == notOneVertexIndex || innerEdge.v == notOneVertexIndex){
						System.out.println("Made it, distance is: " + innerEdge.distance);
						result += innerEdge.distance;
						break;
					}
				}
				continue;
			}
			
			System.out.println("Made it too, distance is: " + this.getDistance(e.u-1, e.v-1));
			result += this.getDistance(e.u-1, e.v-1);
		}
		
		if(Utility.IsDebug)
			assert (result > 0) : "Computed LB of zero";
		
		return result;
	}
	*/
	
	public void updateCost(List<Edge> solution){
		updateCost(solution, false);
	}
	
	public void updateCost(List<Edge> solution, boolean isPrinting){
		int [] punishment = new int[this.vertexCoords.length + 1];
		Arrays.fill(punishment, -2);
		
		for(Edge e : solution){
			punishment[e.u]++;
			punishment[e.v]++;
		}
		
		if(Utility.IsDebug){
			int sum = 0;
			for(double d : punishment) sum += d;
			assert (sum == 0) : "Sum of punishment was not zero in updateCost";
		}
		
		if(isPrinting){
			System.out.println("Distance matrix before update");
			PrintDistanceMatrix();
		}
		
		// Update cost of edges incident to vertex one
		for(EdgeWithDistance e : this.includedEdgesWithOneVertex){
			e.distance += punishment[e.u] + punishment[e.v];
		}
				
		for(EdgeWithDistance e : this.potentialOneVertexEdges){
			e.distance += punishment[e.u] + punishment[e.v];
		}
		
		// Update cost of edges at initial index 1 or greater
		for(Edge e : this.edges){
			distances[e.u][e.v] += punishment[e.u +1] + punishment[e.v +1]; // +1 due to the one lower dimension
			distances[e.v][e.u] = distances[e.u][e.v]; // symmetric TSP
		}
		
		if(isPrinting){
			System.out.println("Previous OneTree solution:");
			Utility.PrintRoute(solution);
			
			String debugMessage = "Punishments are: ";
			for(int i = 0; i < punishment.length; i++) 
				debugMessage += "V" + String.format("%-2d", i) + ": " + String.format("%-4d", punishment[i]);
			System.out.println(debugMessage);
			
			System.out.println("Distance matrix after update");
			PrintDistanceMatrix();
		}
	}
	
	private void PrintDistanceMatrix(){
		String output = "";
		
		for(EdgeWithDistance e : this.potentialOneVertexEdges) 
			output += e.u + "<->" + e.v + ": " + String.format("%-5.2f", e.distance) + "  ";
		for(EdgeWithDistance e : this.includedEdgesWithOneVertex) 
			output += e.u + "<->" + e.v + ": " + String.format("%-5.2f", e.distance) + "  ";
		System.out.println(output);
		
		output = "  ";
		for(int i = 1; i <= vertexCoords.length;i++) output += "  "  + String.format("%-8d", i);
		System.out.println(output);
		
		int i =0;
		for(double[] row : this.distances){
			i++;
			output = String.format("%-2d", i);
			for(double d : row) output += "  "  + String.format("%-8.2f", d);
			System.out.println(output);
		}
	}
}
