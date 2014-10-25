package edu.assignment2;

import java.util.List;

import com.sun.xml.internal.messaging.saaj.soap.Envelope;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.UnknownObjectException;

public class CplexSolver {
	private static IloNumVar[] currentVars;
	
	public static Solution LPRelxation(SetCoverInstance instance){
		IloCplex model;
		Solution result = null;
		
		try {
			model = GetModel(instance, true);
			model.solve();
			if (Utility.IsDebug)
				System.out.println("Simpelx objective value: " + model.getObjValue());
			result = GetResult(instance,model);
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private static Solution GetResult(SetCoverInstance instance,IloCplex model) throws UnknownObjectException, IloException {
		double[] primals = new double[instance.numSets];
		
		int i = 0;
		for(IloNumVar var : currentVars){
			primals[i] = model.getValue(var);
			i++;
		}
		
		return new Solution(primals, instance);
	}

	private static IloCplex GetModel(SetCoverInstance instance, boolean isLPRelxation) throws IloException {
		IloCplex model = new IloCplex();
		if(isLPRelxation)
			currentVars = model.numVarArray(instance.numSets,0.0,Double.POSITIVE_INFINITY);
		else
			currentVars = model.boolVarArray(instance.numSets);
		
		model.setOut(null);
		
		// Create objective function
		int i = 0;
		IloLinearNumExpr objective = model.linearNumExpr();
		for(IloNumVar var : currentVars){
			objective.addTerm(instance.Costs[i], var);
			i++;
		}
		model.addMinimize(objective);
		
		// Add constrains
		IloLinearNumExpr constrain;
		for(List<Set> setsCoveringVertex : instance.CoveringSets){
			constrain = model.linearNumExpr();
			for(Set s: setsCoveringVertex)
				constrain.addTerm(1.0, currentVars[s.Index]);
			model.addLe(1.0, constrain);
		}
		
		return model;
	}
}
