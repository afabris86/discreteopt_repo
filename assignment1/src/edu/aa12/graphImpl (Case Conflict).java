package edu.aa12;

public class GraphImpl extends Graph{
	private static double[][] nxtVertexCoords;
	
	public GraphImpl(double[][] coords){
		super(nxtVertexCoords);
		
		nxtVertexCoords = new double[coords.length][2];
		int k = 0;
		
		for(int i = 1 ; i < coords.length ; i++ ){
			nxtVertexCoords[i-1][0] = coords[i][0];
			nxtVertexCoords[i-1][1] = coords[i][1];
		}
		
	}
}