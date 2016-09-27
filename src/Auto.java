import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class Auto {
	
	final int MAX_STATE = 100;
	
	private ArrayList<String>[][] auto;
	private Set<Integer> finalStates;
	
	private int startState;
	private int num_states;
	
	private int ini;
	private int fim;
	
	private boolean isAFD = false;
	private boolean noEps = false;
	private boolean onlySymbol = false;

	public Auto(){
		auto = new ArrayList[MAX_STATE][MAX_STATE];
		finalStates = new TreeSet<Integer>();
		
		for(int i = 0; i < MAX_STATE; i++)
			for(int j = 0; j < MAX_STATE; j++)
				auto[i][j] = new ArrayList<String>();
		
		//TESTES
		num_states = 4;
		startState = 0;
		finalStates.add(3);
		//------
	}
	
	public void addEdge (int s1, int s2, String w){
		auto[s1][s2].add(w);
	}
	
	//ITEM 1 ----------------------------------------------
	
	public void createNFAFromRE(String regex){
		ini = 0;
		fim = 1;
		
		auto[0][1].add(regex);
		
	}
	
	private void processaRegex(String substring){
		if(isUnion(substring)){
			//Altera a matriz para união
		}
		else{
			if(isStar(substring)){
				//Altera a matriz para o fecho de Kleene
			}
			else{
				//Altera a matriz para o caso de concatenação
			}
		}
		
	}
	
	private boolean isUnion(String substring){
		int counter = 0;
		char aux;
		
		for(int i = 0; i < substring.length(); i++){
			aux = substring.charAt(i);
			if(aux == '(')
				counter++;
			if(aux == ')')
				counter--;
			if(aux == '+' && counter == 0)
				return true;
				
		}
		return false;
	}
	
	private boolean isCat(String substring){
		return true;
	}
	
	private boolean isStar(String substring){
		if(substring.length() > 2){
			if(substring.charAt(substring.length()-1) == '*')
				return true;
		}
			
		return false;
	}

	//ITEM 2 ----------------------------------------------
	
	private void getOutput (String chain, Set<Integer> statesSet, int state, boolean[][] epsFlag){
		epsFlag[state][chain.length()] = true;
		
		for (int i=0; i<num_states; i++){
			ArrayList<String> edgesList = auto[state][i];
			
			for (String edge : edgesList){
				if (edge.charAt(0) == '&' && !epsFlag[i][chain.length()])
					getOutput (chain, statesSet, i, epsFlag);
			}
		}
		
		if (chain.length() == 0){
			statesSet.add(state);
			return;
		}
		
		char symbol = chain.charAt(0);
		String newChain = chain.substring(1);
		
		for (int i=0; i<num_states; i++){
			ArrayList<String> edgesList = auto[state][i];
			
			for (String edge : edgesList){
				if (edge.charAt(0) == symbol)
					getOutput (newChain, statesSet, i, epsFlag);
			}
		}
	}
	
	public boolean getOutput (String chain, Set<Integer> statesSet){
		if (!onlySymbol)
			return false;
		
		boolean[][] epsFlag = new boolean[num_states][chain.length()+1];
		for (int i=0; i<num_states; i++)
			for (int j=0; j<chain.length(); j++)
				epsFlag[i][j] = false;
		
		getOutput (chain, statesSet, startState, epsFlag);
		Set<Integer> intersection = new TreeSet<Integer>(statesSet);
		intersection.retainAll(finalStates);
		
		return !intersection.isEmpty();
	}
	
	//ITEM 4 ----------------------------------------------
	
	private String union (String s1, String s2){
		if (s1.length() > 1)
			s1 = "(" + s1 + ")";
		if (s2.length() > 1)
			s2 = "(" + s2 + ")";
		
		return s1 + "+" + s2;
	}
	
	private String concat (String s1, String s2){
		if (s1.length() > 1)
			s1 = "(" + s1 + ")";
		if (s2.length() > 1)
			s2 = "(" + s2 + ")";
		
		return s1 + s2;
	}
	
	private String kleene (String s){
		if (s.length() > 1)
			s = "(" + s + ")";
		
		return s + "*";
	}
	
	private void generateUnionEdges(){
		for (int i=0; i<num_states; i++){
			for (int j=0; j<num_states; j++){
				
				ArrayList<String> edgesList = auto[i][j];
				
				if (edgesList.size() > 1){
					String newEdgeRegex = "";
					for (String edge : edgesList)
						newEdgeRegex = union(newEdgeRegex, edge);
					
					edgesList.clear();
					edgesList.add(newEdgeRegex);
				}
				
			}
		}
	}
	
	private void removeState (int state){
		String selfLoopRegex = "";
		
		ArrayList<String> stateList = auto[state][state];
		if (stateList.size() > 0)
			selfLoopRegex = kleene(stateList.get(0));
		
		for (int from=0; from<num_states; from++){
			if (from == state) continue;
			
			ArrayList<String> fromStateList = auto[from][state];
			if (fromStateList.isEmpty()) continue;
			
			String fromStateRegex = fromStateList.get(0);
			
			for (int to=0; to<num_states; to++){
				if (to == state) continue;
				
				ArrayList<String> stateToList = auto[state][to];
				if (stateToList.isEmpty()) continue;
				
				String stateToRegex = stateToList.get(0);
				
				String newEdgeRegex = concat(fromStateRegex, selfLoopRegex);
				newEdgeRegex = concat(newEdgeRegex, stateToRegex);
				
				ArrayList<String> fromToList = auto[from][to];
				if (fromToList.size() > 0){
					String fromToRegex = fromToList.get(0);
					newEdgeRegex = union(newEdgeRegex, fromToRegex);
					fromToList.clear();
				}
				
				fromToList.add(newEdgeRegex);
			}
		}
		
		for (int i=0; i<num_states; i++){
			auto[i][state].clear();
			auto[state][i].clear();
		}
	}
	
	public String getRegex(){
		//CHAMAR FUNCAO DE REMOVER OS EPS!
		generateUnionEdges();
		
		for (int i=0; i<num_states; i++){
			if (i != startState && !finalStates.contains(i)){
				removeState(i);
				print();
				System.out.println("");
			}
		}
		
		return "";
	}
	
	public void print(){
		for (int i=0; i<num_states; i++){
			System.out.print(i + ":");
			for (int j=0; j<num_states; j++){
				if (auto[i][j].size() == 0) continue;
				
				System.out.print(" " + j + "[");
				for (String s : auto[i][j])
					System.out.print(s + "-");
				System.out.print("]");
			}
			
			System.out.println("");
		}
	}
	
}
