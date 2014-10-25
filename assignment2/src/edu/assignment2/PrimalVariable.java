package edu.assignment2;

public class PrimalVariable {
	/** Set that the variable coorespond to */
	public final Set Set;
	
	/** Value of primal variable */
	public double value;
	
	public PrimalVariable(Set s, double val){
		this.Set = s;
		this.value = val;
	}
	
}
