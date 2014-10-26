package edu.assignment2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainMethods {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Solution solution;
		long start, end;
		double loadTime;
		
		//String data  = "./scpa3.txt";
		//String data = "./scpc3.txt";
		String data = "./scpnrf1.txt";
		//String data = "./scpnrg5.txt";
		
		// Load instance
		start = System.nanoTime();
		SetCoverInstance instance = LoadInstancce.Load(data);
		end = System.nanoTime();
		loadTime = (end-start)/1000000.0;
		System.out.printf("Took %.2fms to load and initalise instance",loadTime);
		System.out.println("\n");
		
		//NB: Depends on order due to catch. Thus rounding and random rounding have almost same speed
		
		start = System.nanoTime();
		solution = PrimalDualSolver.Solve(instance);
		end = System.nanoTime();
		System.out.println("Objective value with primal-dual is: " + solution.GetObjectiveValue());
		System.out.printf("Took %.2fms (%.2fms with load time)",(end-start)/1000000.0,(end-start)/1000000.0+loadTime);
		System.out.println("\n");
		
		start = System.nanoTime();
		solution = SolveWithRandomizedRounding(instance);
		end = System.nanoTime();
		System.out.println("Objective value with random rounding is: " + solution.GetObjectiveValue());
		System.out.printf("Took %.2fms (%.2fms with load time)",(end-start)/1000000.0,(end-start)/1000000.0+loadTime);
		System.out.println("\n");
		
		start = System.nanoTime();
		solution = SolveWithRounding(instance);
		end = System.nanoTime();
		System.out.println("Objective value with rounding is: " + solution.GetObjectiveValue());
		System.out.printf("Took %.2fms (%.2fms with load time)",(end-start)/1000000.0,(end-start)/1000000.0+loadTime);
		System.out.println("\n");
		
		start = System.nanoTime();
		solution = CplexSolver.Exact(instance);
		end = System.nanoTime();
		System.out.println("Cplex exact solution is: " + solution.GetObjectiveValue());
		System.out.printf("Took %.2fms (%.2fms with load time)",(end-start)/1000000.0,(end-start)/1000000.0+loadTime);
		System.out.println("\n");
	}
	
	public static Solution SolveWithRounding(SetCoverInstance instance){
		// Get fractional solution
		Solution solution = CplexSolver.LPRelxation(instance);
		
		// Do rounding
		double fInv = 1.0 / instance.f;
		/*PrimalVariable var;
		for(int i = 0; i < solution.NonZeroPrimals.size(); i++){
			var = solution.NonZeroPrimals.get(i);
			if(var.value >= fInv)
				var.value = 1.0;
			else {
				solution.NonZeroPrimals.remove(var);
				i--;
			}
		}*/
		
		List<PrimalVariable> vars = new ArrayList<PrimalVariable>();
		for(PrimalVariable var : solution.NonZeroPrimals)
			if(var.value >= fInv){
				vars.add(var);
				var.value = 1.0;
			}
		
		solution = new Solution(vars, instance);
		
		if (Utility.IsDebug)
			assert (solution.IsILPFeasible()) : "Solution in rouding was not feasible";
		
		if(Utility.IsDebug)
			System.out.println("f is : " + instance.f + " (the upper bound factor)");
		
		return solution;
	}
	
	public static Solution SolveWithRandomizedRounding(SetCoverInstance instance){
		// Get fractional solution
		Solution fractionalSolution = CplexSolver.LPRelxation(instance);
		
		if(Utility.IsDebug)
			System.out.println("Fractional solutions is: " + fractionalSolution.toString());
		
		// Perform random rounding
		int simulation;
		double constant = (instance.numVertecies >=4) ? 2 : Math.log(4*instance.numVertecies) / Math.log(instance.numVertecies);
		double maxNumReps = constant*Math.log(instance.numVertecies);
		double upperBound = 4*maxNumReps*fractionalSolution.GetObjectiveValue();
		
		boolean isFeasibleAndHaveCostWithinBounds = false;
		Solution integerSolution = null;
		Random randomGenerator = new Random();
		List<PrimalVariable> includedVars = new ArrayList<PrimalVariable>();
		List<PrimalVariable> excludedVars;
		PrimalVariable excludedVar;
		
		simulation = 0;
		while(simulation <= 10 && !isFeasibleAndHaveCostWithinBounds){
			simulation++;
			includedVars.clear();
			excludedVars = new ArrayList<PrimalVariable>(fractionalSolution.NonZeroPrimals);
			
			for(int i = 0; i <maxNumReps;i++){
				for(int j =0; j<excludedVars.size();j++){
					excludedVar = excludedVars.get(j);
					if(randomGenerator.nextFloat() > excludedVar.value) continue;
				
					excludedVars.remove(excludedVar);
					includedVars.add(new PrimalVariable(excludedVar.Set, 1.0));
					j--;
				}
			}
			
			integerSolution = new Solution(includedVars, instance);
			isFeasibleAndHaveCostWithinBounds = integerSolution.IsILPFeasible() && 
												integerSolution.GetObjectiveValue() <= upperBound;
			
			if(!isFeasibleAndHaveCostWithinBounds && Utility.IsDebug)
				System.out.println("This solution is not feasible or do not meet UB criteria: " + integerSolution.toString());
		}
		
		if(Utility.IsDebug)
			assert (simulation <= 10) : "Rounding solution exited while loop due to max simulation constrain";
		
		if(Utility.IsDebug){ 
			System.out.println("Number of simulations before feasible solution in random rounding: " + simulation);
			System.out.println("Upper bound factor is: " + maxNumReps / constant);
		}
		return integerSolution;
	}
}
