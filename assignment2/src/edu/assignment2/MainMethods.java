package edu.assignment2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainMethods {
	/**
	 * @param args
	 * @throws IOException 
	 */
	private static double loadTime;
	
	public static void main(String[] args) throws IOException {
		Solution solution;
		long start, end;
		
		// Paths to data file
		String data  = "./scpa3.txt";
		//String data = "./scpc3.txt";
		//String data = "./scpnrf1.txt";
		//String data = "./scpnrg5.txt";
		
		// Load instance
		start = System.nanoTime();
		SetCoverInstance instance = LoadInstancce.Load(data);
		end = System.nanoTime();
		loadTime = (end-start)/1000000.0;
		System.out.printf("Took %.2fms to load and initalise instance",loadTime);
		System.out.println("\n");
		
		// Solve instance
		Solve(Method.primal_dual, instance);
		//Solve(Method.rounding, instance);
		//Solve(Method.random_rounding, instance);
		//Solve(Method.exact, instance);
		//Solve(Method.lp_relxation, instance);
	}
	
	public enum Method{
		lp_relxation, exact, rounding, random_rounding, primal_dual
	}
	
	public static void Solve(Method method, SetCoverInstance instance){
		long start, end;
		Solution solution = null;
		
		if(method == Method.exact){
			start = System.nanoTime();
			solution = CplexSolver.Exact(instance);
			end = System.nanoTime();
			System.out.println("Cplex exact solution is: " + solution.GetObjectiveValue());
			System.out.printf("Took %.2fms (%.2fms with load time)",(end-start)/1000000.0,(end-start)/1000000.0+loadTime);
			System.out.println("\n");
		}
		else if (method == Method.primal_dual){
			start = System.nanoTime();
			solution = PrimalDualSolver.Solve(instance);
			end = System.nanoTime();
			System.out.println("Objective value with primal-dual is: " + solution.GetObjectiveValue());
			System.out.printf("Took %.2fms (%.2fms with load time)",(end-start)/1000000.0,(end-start)/1000000.0+loadTime);
			System.out.println("\n");
		}
		else if (method == Method.rounding){
			start = System.nanoTime();
			solution = SolveWithRounding(instance);
			end = System.nanoTime();
			System.out.println("Objective value with rounding is: " + solution.GetObjectiveValue());
			System.out.printf("Took %.2fms (%.2fms with load time)",(end-start)/1000000.0,(end-start)/1000000.0+loadTime);
			System.out.println("\n");
		}
		else if(method == Method.random_rounding){
			// Special handling for random
			int min, max, result,current;
			double mean = 0;
			int num_samples = 200;
			Map<Integer, Integer> results = new HashMap<Integer, Integer>();
			
			start = System.nanoTime();
			solution = SolveWithRandomizedRounding(instance);
			end = System.nanoTime();
			System.out.printf("First calculation of random rounding took %.2fms (%.2fms with load time)",(end-start)/1000000.0,(end-start)/1000000.0+loadTime);
			
			min = max = solution.GetObjectiveValue();
			results.put(min, 1);
			for(int i=0;i<num_samples;i++){
				solution = SolveWithRandomizedRounding(instance);
				result = solution.GetObjectiveValue();
				min = Math.min(min, result);
				max = Math.max(max, result);
				
				if(results.containsKey(result)){
					current = results.get(result); 
					results.put(result, current + 1);
				}
				else
					results.put(result, 1);
			}
			for(int key : results.keySet())
				mean += (key * results.get(key)) / (double)num_samples;
			
			System.out.printf("\nFor %d samples of random rounding the mean, min and max value was %.2f, %d and %d",num_samples,mean,min,max);
			System.out.println("\n");
		}
		else if(method == Method.lp_relxation){
			start = System.nanoTime();
			solution = CplexSolver.LPRelxation(instance);
			end = System.nanoTime();
			System.out.println("Objective value LP-relaxation is: " + solution.GetObjectiveValue());
			System.out.printf("Took %.2fms (%.2fms with load time)",(end-start)/1000000.0,(end-start)/1000000.0+loadTime);
			System.out.println("\n");
		}
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
