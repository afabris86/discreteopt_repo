package edu.assignment2;

import java.util.ArrayList;
import java.util.List;

public class Set {
	/** List of covered vertices */
	public final List<Integer> Cover;
	
	public final int Index;
	
	public Set(int i){
		this.Cover = new ArrayList<Integer>();
		this.Index = i;
	}
	
	public String toString(){
		return String.format("Set %d covering " + Cover.toString(),Index);
	}
	
	public boolean equals(Object o){
		if(o instanceof Set){
			return ((Set)o).Index == this.Index;
		}
		return false;
	}
}
