package edu.assignment2;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;

public class Assignment2 {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String data1  = "../../../scpa3.dat";
		//String data2  = "../../../scpc3.dat";
		//String data3  = "../../../scpnrf1.dat";
		//String data4  = "../../../scpnrg5.dat";
		
		try {
			IloCplex cplex = new IloCplex();
			cplex.importModel("../../../ModelSparse.mod");
		} catch (IloException e){
			 System.err.println("Concert exception caught:" + e); 
		}
	}
}
