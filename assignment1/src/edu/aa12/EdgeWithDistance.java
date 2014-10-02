package edu.aa12;

public class EdgeWithDistance extends Edge implements Comparable<EdgeWithDistance> {
	
	public double distance;
	public Edge edge;
	
	public EdgeWithDistance(Edge e, double d){
		super(e.u,e.v);
		
		distance = d;
		edge = e;
	}

	@Override
	public int compareTo(EdgeWithDistance o) {
		if(this.distance < o.distance) return -1;
		if(this.distance > o.distance) return 1;
		
		return 0;
	}
}
