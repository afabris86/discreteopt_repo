package edu.assignment2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sun.java2d.pipe.SpanShapeRenderer.Simple;

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
		
		start = System.nanoTime();
		solution = SolveWithRounding(instance);
		end = System.nanoTime();
		System.out.println("Objective value with rounding is: " + solution.GetObjectiveValue());
		System.out.printf("Took %.2fms (%.2fms with load time)",(end-start)/1000000.0,(end-start)/1000000.0+loadTime);
		System.out.println("\n");
		
		start = System.nanoTime();
		solution = SolveWithRandomizedRounding(instance);
		end = System.nanoTime();
		System.out.println("Objective value with random rounding is: " + solution.GetObjectiveValue());
		System.out.printf("Took %.2fms (%.2fms with load time)",(end-start)/1000000.0,(end-start)/1000000.0+loadTime);
		System.out.println("\n");
		
		start = System.nanoTime();
		solution = PrimalDualSolver.Solve(instance);
		end = System.nanoTime();
		System.out.println("Objective value with primal-dual is: " + solution.GetObjectiveValue());
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
		PrimalVariable var;
		for(int i = 0; i < solution.NonZeroPrimals.size(); i++){
			var = solution.NonZeroPrimals.get(i);
			if(var.value >= fInv)
				var.value = 1.0;
			else {
				solution.NonZeroPrimals.remove(var);
				i--;
			}
		}
		
		if (Utility.IsDebug)
			assert (solution.IsILPFeasible()) : "Solution in rouding was not feasible";
		
		if(Utility.IsDebug)
			System.out.println("f is : " + instance.f);
		
		return solution;
	}
	
	public static Solution SolveWithRandomizedRounding(SetCoverInstance instance){
		// Get fractional solution
		Solution fractionalSolution = CplexSolver.LPRelxation(instance);
		
		// Round all Non zero primals to one as they will be referenced later
		for(PrimalVariable var : fractionalSolution.NonZeroPrimals)
			var.value = 1.0;
		
		// Perform random rounding
		int simulation;
		double maxNumReps = Math.log(fractionalSolution.NonZeroPrimals.size());
		boolean isFeasible = false;
		Solution integerSolution = null;
		Random randomGenerator = new Random();
		List<PrimalVariable> includedVars = new ArrayList<PrimalVariable>();
		List<PrimalVariable> excludedVars;
		
		simulation = 0;
		while(simulation > 100 || !isFeasible){
			simulation++;
			includedVars.clear();
			excludedVars = new ArrayList<PrimalVariable>(fractionalSolution.NonZeroPrimals);
			
			for(int i = 0; i <maxNumReps;i++){
				for(int j =0; j<excludedVars.size();j++){
					if(randomGenerator.nextBoolean()) continue;
					
					includedVars.add(excludedVars.remove(j));
					j--;
				}
			}
			
			integerSolution = new Solution(includedVars, instance);
			isFeasible = integerSolution.IsILPFeasible();
		}
		
		if(Utility.IsDebug)
			assert (simulation <= 100) : "Rounding solution exited while loop due to max simulation constrain";
		
		if(Utility.IsDebug) 
			System.out.println("Number of simulations before feasible solution in random rounding: " + simulation);
		
		return integerSolution;
	}
}
