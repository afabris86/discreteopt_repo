package edu.aa12;

/** An edge in a graph, represented by the indices of the vertices. */
public class Edge implements Comparable{
	public int u,v;
	
	public Edge(int u, int v){
		this.u = u;
		this.v = v;
	}
	
	public boolean equals(Object o){
		if(o instanceof Edge){
			Edge e = (Edge)o;
			return (u==e.u&&v==e.v)||(u==e.v&&v==e.u);
		}
		return false;
	}
	
	public String toString(){
		return String.format("Edge[%d-%d]",u,v);
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
