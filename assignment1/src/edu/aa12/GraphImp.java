package edu.aa12;

import java.util.List;
import java.util.ArrayList;

public  class GraphImp extends Graph{
	
	public List<Edge> incident = new ArrayList<Edge>();
	
	public GraphImp(double[][] nxtVertexCoords, Graph g){
		super(nxtVertexCoords);
		
		for(Edge e: g.edges){
			if(e.u == 0 || e.v == 0){
				incident.add(e);
				continue;
			}
			
			createEdge(e.u - 1, e.v - 1);
		}
	}
}
