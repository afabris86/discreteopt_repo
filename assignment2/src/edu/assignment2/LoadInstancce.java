package edu.assignment2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadInstancce {
	public static SetCoverInstance Load(String path) throws IOException{
		int numVertices;
		int numSets;
		int[] cost;
		List<Integer>[] covers;
		 
		// code from http://www.programcreek.com/2011/03/java-read-a-file-line-by-line-code-example/
		File fin = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(fin));
		 
		String line = null;
		int[] numbers;
		
		// Read first line
		numbers = GetNumbersFromLine(br.readLine());
		numVertices = numbers[0];
		numSets = numbers[1];
		if(Utility.IsDebug)
			assert (numbers.length == 2) : "First line did not have two numbers";
		
		// Read costs 
		int i = 0;
		cost = new int[numSets];
		while (i < numSets && (line = br.readLine()) != null) {
			numbers = GetNumbersFromLine(line);
			for(int num : numbers){
				cost[i] = num;
				i++;
			}
		}
		
		// Read cover
		i = 0;
		int j;
		int numCoveringSets;
		covers = new List[numVertices];
		while (i < numVertices && (line = br.readLine()) != null) {
			numbers = GetNumbersFromLine(line);
			numCoveringSets = numbers[0];
			covers[i] = new ArrayList<Integer>();
			
			if(Utility.IsDebug && numbers.length != 1) {
				String tmp = "";
				for(int n : numbers) tmp += " " + n;
				assert false : "Found more than one line for the number of covering sets:" + tmp;
			}
			
			j = 0;
			while (j < numCoveringSets && (line = br.readLine()) != null) {
				numbers = GetNumbersFromLine(line);
				for(Integer num : numbers) 
					covers[i].add(num - 1); // Assumes that vertices are one-index in the loaded file
				j += numbers.length;
			}
			if(Utility.IsDebug)
				assert (j == numCoveringSets) : "Did not find enough covering sets indicies for vertex " + i;
				
			i++;
		}
		
		if(Utility.IsDebug)
			assert (numVertices == i) : "Cover while loop did not exits because covers for all vertices was found";
		
		br.close();
		
		// Return result
		SetCoverInstance result = new SetCoverInstance(cost, covers);
		return result;
	}
	
	private static int[] GetNumbersFromLine(String line){
		line = line.trim();
		String[] temp = line.split(" ");
		int[] result = new int[temp.length];
		
		int i = -1;
		for(String num : temp){
			i +=1;
			result[i] = Integer.parseInt(num);
		}
		
		return result;
	}
}
