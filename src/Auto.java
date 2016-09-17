import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class Auto {
	
	final int MAX_STATE = 100;
	
	private ArrayList<String>[][] auto;
	private Set<Integer> finalStates;
	
	private int startState;
	private int num_states;
	
	private boolean isAFD = false;
	private boolean noEps = false;
	private boolean onlySymbol = false;
	
	public Auto(){
		auto = new ArrayList[MAX_STATE][MAX_STATE];
		finalStates = new TreeSet<Integer>();
		
		finalStates.add(1);
		startState = 0;
	}
	
	public void addEdge (int s1, int s2, String w){
		if (auto[s1][s2] == null)
			auto[s1][s2] = new ArrayList<String>();
		
		auto[s1][s2].add(w);
	}
	
	private void getOutput (String chain, Set<Integer> statesSet, int state){
		if (chain.length() == 0){
			statesSet.add(state);
			return;
		}
		
		char symbol = chain.charAt(0);
		String newChain = chain.substring(1);
		
		for (int i=0; i<num_states; i++){
			ArrayList<String> edgesList = auto[state][i];
			if (edgesList == null)
				continue;
			
			for (String edge : edgesList){
				if (symbol == edge.charAt(0))
					getOutput (newChain, statesSet, i);
			}
		}
	}
	
	public boolean getOutput (String chain, Set<Integer> statesSet){
		if (!onlySymbol)
			return false;
		
		getOutput (chain, statesSet, startState);
		Set<Integer> intersection = new TreeSet<Integer>(statesSet);
		intersection.retainAll(finalStates);
		
		return !intersection.isEmpty();
	}
	
}
