package edu.assignment2;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PrimalDualSolver {
	public static Solution Solve(SetCoverInstance instance){
		int[] usedSlack = new int[instance.numSets];
		boolean[] isCovered = new boolean[instance.numVertecies]; 
		
		int tigthesConstrain;
		int slackInConstrain;
		int setIndex;
		
		List<Integer> tmpPrimalsToIncrease = new ArrayList<Integer>();
		List<Set> setsThatGoesTight = new ArrayList<Set>();
		List<PrimalVariable> vars = new ArrayList<PrimalVariable>();
		List<Set> coveringSets;
		
		// Logic behinde loop
		// You know that at any point all previous vertices must be corved
		// You know that all uncovered vertercies are only in non-tight sets
		// Hence, you should increase slackUsed for all sets
		// and you shall sets the covered to true for all sets that goes tight
		for(int i = 0; i < instance.numVertecies;i++){
			if (isCovered[i]) continue;
			
			// Find the sets that first go tight and the slack value in the constrain
			tmpPrimalsToIncrease.clear();
			coveringSets = instance.CoveringSets[i];
			tigthesConstrain = Integer.MAX_VALUE;
			for(Set s : coveringSets){
				setIndex = s.Index;
				slackInConstrain = instance.Costs[setIndex] - usedSlack[setIndex];
				if(slackInConstrain > tigthesConstrain) continue;
				if(slackInConstrain == tigthesConstrain){
					setsThatGoesTight.add(s);
					continue;
				}
				
				tigthesConstrain = slackInConstrain;
				setsThatGoesTight.clear();
				setsThatGoesTight.add(s);
			}
			
			if(Utility.IsDebug)
				assert (tigthesConstrain > 0) : "There should not set with a tight constrain at this point in the algorithm";
			
			// increase tmp primals by tigthesConstrain
			for(Set s : coveringSets)
				usedSlack[s.Index] += tigthesConstrain;
			
			// add tight sets to solution and at its vertices as covered
			for(Set s : setsThatGoesTight){
				vars.add(new PrimalVariable(s, 1.0));
				for(int vertexIndex : s.Cover)
					isCovered[vertexIndex] = true;
			}
		}
		
		Solution result = new Solution(vars, instance);
		
		if(Utility.IsDebug)
			assert (result.IsILPFeasible()) : "Primal-Dual did not yield a feasible solution";
			
		return result;
	}
}
