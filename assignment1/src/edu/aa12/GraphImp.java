package edu.aa12;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public  class GraphImp extends Graph{
	
	BnBNode node;
	public List<EdgeWithDistance> potentialEdges = new ArrayList<EdgeWithDistance>();
	public List<EdgeWithDistance> includedEdges = new ArrayList<EdgeWithDistance>();
	
	double[][] costsOfOneEdge;
	
	public GraphImp(double[][] nxtVertexCoords, Graph g, BnBNode n){
		super(nxtVertexCoords);
		
		node = n;
		
		// Add edges
		for(Edge e: g.edges){
			if(e.u == 0 || e.v == 0){
				potentialEdges.add(new EdgeWithDistance(e, g.getDistance(e.u, e.v)));
				continue;
			}
			
			createEdge(e.u - 1, e.v - 1);
		}

		// Add potential or included edges to one vertex
		while(node.parent != null){
			if(node.edge.v != 0 && node.edge.u != 0){
				//do nothing
			}else{
				if(node.edgeIncluded)
					includedEdges.add(new EdgeWithDistance(node.edge, g.getDistance(node.edge.u, node.edge.v)));
				
				potentialEdges.remove(node.edge);
			}
			
			node = node.parent;
		}
	}
	
	public List<Edge> findCheapest(){
		ArrayList<Edge> minimal = new ArrayList<Edge>();
		
		if(includedEdges.size() == 2){
			minimal.add(includedEdges.get(0));
			minimal.add(includedEdges.get(1));
		}
		else
		{
			List<EdgeWithDistance> edges = new ArrayList<EdgeWithDistance>(potentialEdges);
			
			if(includedEdges.size() == 0){
				Edge first = Collections.min(edges);
				minimal.add(first);
				edges.remove(first);
				minimal.add(Collections.min(edges));
				
			}
			else 
			{
				minimal.add(includedEdges.get(0));
				minimal.add(Collections.min(edges));
			}
		}
		return minimal;
	}
	
	public void updateCost(List<Edge> solution){
		//Do stuff
	}
}
