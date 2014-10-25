package edu.assignment2;
import java.util.LinkedList;
import java.util.List;

public class SetCoverInstance {
	/** Number of vertices in problem */
	public final int numVertecies;
	
	/** Number of sets*/
	public final int numSets;
	
	/** Cost of each set */ 
	public final int[] Costs;
	
	/** Return the number for the vertex covered by most set */
	public int f;
	
	/* Dunno how much we can save by using simpler objects?
	/** A list for each vertex for which sets that cover it 
	public final List<List<Integer>> SetsCoveringVertices;
	*/
	
	/** A array with list; one for each vertex for which sets that cover it */
	public final List<Set>[] CoveringSets;
	
	/** List of sets */
	public final Set[] Sets;
	
	public SetCoverInstance(int[] cst, List<Integer>[] cov){
		this.Costs = cst;
		this.numSets = cst.length;
		this.Sets = new Set[numSets];
		for(int i=0;i<numSets;i++) this.Sets[i] = new Set(i);
		
		this.numVertecies = cov.length;
		CoveringSets = new List[numVertecies];
		
		List<Integer> coveringsSets;
		this.f = 0;
		for(int vertexIndex = 0; vertexIndex < cov.length; vertexIndex++){
			//System.out.println(vertexIndex);
			
			CoveringSets[vertexIndex] = new LinkedList<Set>();
			coveringsSets = cov[vertexIndex];

			this.f = Math.max(f, coveringsSets.size());
			for(Integer setIndex : coveringsSets){
				Sets[setIndex].Cover.add(vertexIndex);
				CoveringSets[vertexIndex].add(Sets[setIndex]);
			}
		}
	}
	
	public String toString(){
		String result;
		result = String.format("# sets: %d, # vertices: %d",numSets, numVertecies);
		int i =0;
		
		result += "\n Costs are: ";
		for(i = 0; i < Costs.length; i++)
			result += Costs[i] + ", ";
		
		i =0;
		for(List<Set> cover : CoveringSets){
			i++;
			if((i % 50) != 0) continue;
			result += String.format("\n Vertex %d is covered by: ", i-1);
			for(Set s : cover) 
				result += s.Index + " ";
		}
		
		i = 0;
		for(Set s : Sets){
			i++;
			if((i % 50) != 0) continue;
			result += "\n" + s.toString();
		}
			
		return result;
	}
}
