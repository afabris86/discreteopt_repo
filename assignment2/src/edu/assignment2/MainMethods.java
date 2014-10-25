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
		
		//String data  = "./scpa3.txt";
		//String data = "./scpc3.txt";
		//String data = "./scpnrf1.txt";
		String data = "./scpnrg5.txt";
		SolveWithRounding(data);
		SolveWithRandomizedRounding(data);
	}
	
	public static void SolveWithRounding(String path) throws IOException{
		// Get fractional solution
		SetCoverInstance instance = LoadInstancce.Load(path);
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
		
		System.out.println("f is : " + instance.f);
		System.out.println("Object value with rounding is: " + solution.GetObjectiveValue());
	}
	
	public static void SolveWithRandomizedRounding(String path) throws IOException{
		// Get fractional solution
		SetCoverInstance instance = LoadInstancce.Load(path);
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
		
		System.out.println("Number of simulations before feasible solution: " + simulation);
		System.out.println("Random rounding objective value: " + integerSolution.GetObjectiveValue());
	}
}
