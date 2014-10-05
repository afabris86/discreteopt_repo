package edu.aa12;

import java.sql.Array;

public class HungarianAlgorithm {
	
	private int numVertecies;
	private double costMatrix[][];
	
	public HungarianAlgorithm(Graph graph, BnBNode node){
		numVertecies = graph.getVertices();
		
		costMatrix = new double[numVertecies][numVertecies];
		for(int i = 0; i<numVertecies;i++)
			for(int j = 0; j<numVertecies;j++)
				costMatrix[i][j] = graph.getDistance(i, j);
		
		BnBNode n = node;
		while(n.parent != null){
			if(n.edgeIncluded) 
				costMatrix[n.edge.u][n.edge.v] = costMatrix[n.edge.v][n.edge.u] = 0;
			else
				costMatrix[n.edge.u][n.edge.v] = costMatrix[n.edge.v][n.edge.u] = Double.POSITIVE_INFINITY;
			n = n.parent;
		}
		
		MakeZerosInColumnsAndRows(costMatrix);
		int[] uncoveredRows;
		int[] uncoveredColumns;
	}

	private boolean IsAllElementsCorverd(double[][] matrix, int[] uncoveredRows, int[] uncoveredColumns) {
		// TODO Auto-generated method stub
		return false;
	}

	private void MakeZerosInColumnsAndRows(double[][] matrix) {
		double minValue;
		for(int i=0;i<numVertecies;i++){
			minValue = Double.POSITIVE_INFINITY;
			for(int j=0;j<numVertecies; j++)
				if(matrix[i][j] < minValue)
					minValue = matrix[i][j];
			
			for(int j =0;j<numVertecies;j++)
				matrix[i][j] -= minValue;
		}
		
		for(int j=0;j<numVertecies;j++){
			minValue = Double.POSITIVE_INFINITY;
			for(int i=0;i<numVertecies; i++)
				if(matrix[i][j] < minValue)
					minValue = matrix[i][j];
			
			for(int i =0;i<numVertecies;i++)
				matrix[i][j] -= minValue;
		}
		
		if(Utility.IsDebug)
			for(int i=0;i<numVertecies;i++)
				for(int j=0;j<numVertecies; j++)
					assert (matrix[i][j]>=0) : "MakeZerosInColumnsAndRows in HungarianAlgorithm made negative values!";
	}
}
