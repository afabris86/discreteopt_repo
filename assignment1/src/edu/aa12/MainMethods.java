package edu.aa12;

public class MainMethods {

	public static void main(String[] args){
		//solveGraph(new Instance1());
		//solveGraph(new Instance2());
		solveGraph(new Instance3());
	}
	
	public static void solveGraph(Graph g){
		BranchAndBound_TSP solver = new BranchAndBound_TSP(g);
		long start = System.nanoTime();
		BnBNode n = solver.solve();
		long end = System.nanoTime();
		
		System.out.printf("Took %.2fms\n",(end-start)/1000000.0);
		System.out.println(n);
		Visualization.visualizeSolution(g, n);//Requires ProGAL (www.diku.dk/~rfonseca/ProGAL)
		
		// Solve with clpex
		start = System.nanoTime();
		CPLEXSolver cplexSolver = new CPLEXSolver(g);
		end = System.nanoTime();
		
		System.out.println("CPLEX found min val: " + cplexSolver.minimumValue);
		System.out.printf("Took %.2fms\n",(end-start)/1000000.0);
		System.out.println(n);
		
		Visualization.visualizeSolution(g, cplexSolver.GetFinalNode());//Requires ProGAL (www.diku.dk/~rfonseca/ProGAL)
	}
}
