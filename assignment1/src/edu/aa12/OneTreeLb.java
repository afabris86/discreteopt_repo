package edu.aa12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OneTreeLb{
	
	Graph graph;
	BnBNode node;
	
	private final Kruskal kruskal = new Kruskal();
	private List<Edge> excluded = new ArrayList<Edge>();
	private List<Edge> included = new ArrayList<Edge>();
	
	public OneTreeLb(Graph graph, BnBNode node){
		this.graph = graph;
		this.node = node;
		
		while(node.parent != null){
			if(node.edge.v != 0 && node.edge.u != 0){
				//do nothing
			}else{
				if(node.edgeIncluded)
					included.add(node.edge);
				else
					excluded.add(node.edge);
			}
			
			node = node.parent;
		}
	}
	
	public double computeLbLag(){
		double[][] nxtVertexCoords = new double[graph.vertexCoords.length - 1][2];
		int k = 0;
		for(int i = 1 ; i < graph.vertexCoords.length ; i++ ){
			nxtVertexCoords[i-1][0] = graph.vertexCoords[i][0];
			nxtVertexCoords[i-1][1] = graph.vertexCoords[i][1];
		}
		
		GraphImp mod = new GraphImp(nxtVertexCoords, this.graph);
		
		List<Edge> minSpanTree = kruskal.minimumSpanningTree(mod, node);
		List<Edge> cheapest = findCheapest(mod, node);
		
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	private List<Edge> findCheapest(GraphImp g, BnBNode node){
		if(included.size() == 2){
			return included;
		}else{
			List<Edge> minimal = new ArrayList<Edge>();
			List<Edge> edges = new ArrayList<Edge>(g.incident);
			
			edges.removeAll(excluded);
			
			if(included.size() == 0){
				Edge first = Collections.min(edges);
				
				minimal.add(first);
				edges.remove(first);
				minimal.add(Collections.min(edges));
				
			}else minimal.add(Collections.min(edges));
				
			return minimal;
		}
	}

}


