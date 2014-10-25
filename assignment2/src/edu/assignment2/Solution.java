package edu.assignment2;

import java.util.ArrayList;
import java.util.List;

public class Solution {
	
	/** List of non-zero primal variables */
	public final List<PrimalVariable> NonZeroPrimals;
	
	/** Instance the solution coorespond to */
	public final SetCoverInstance Instance;
	
	public Solution(List<PrimalVariable> vars, SetCoverInstance in){
		NonZeroPrimals = vars;
		Instance = in;
	}
	
	public Solution(double[] primalValues, SetCoverInstance in){
		if(Utility.IsDebug)
			assert (primalValues.length == in.numSets) : "Number of primals values do not match with number of sets in instance";
		
		int i =-1;
		this.Instance = in;
		this.NonZeroPrimals = new ArrayList<PrimalVariable>();
		for(double d : primalValues){
			i++;
			if (d==0) continue;
			NonZeroPrimals.add(new PrimalVariable(in.Sets[i], d));
		}
	}
	
	public double GetObjectiveValue(){
		double result = 0;
		for(PrimalVariable var : NonZeroPrimals)
			result += var.value * Instance.Costs[var.Set.Index];
		return result;
	}
	
	public boolean IsILPFeasible(){
		boolean[] isVertexCovered = new boolean[Instance.numVertecies];
		for(PrimalVariable var : NonZeroPrimals){
			if(var.value > 0.0 && var.value < 1.0)
				return false;
			
			for(int vertexIndex : var.Set.Cover)
				isVertexCovered[vertexIndex] = true;
		}
		
		for(boolean b : isVertexCovered)
			if(!b) return false;
		
		return true;
	}
}
