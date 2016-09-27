import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class Auto {
	
	final int MAX_STATE = 100;
	
	public ArrayList<String>[][] auto; //PRIVATE
	public Set<Integer> finalStates; //PRIVATE
	
	private boolean isAFD = false;
	private boolean noEps = false;
	private boolean onlySymbol = false;
	public int num_states; //PRIVATE
	
	
	public Auto(){
		auto = new ArrayList[MAX_STATE][MAX_STATE];
		finalStates = new TreeSet<Integer>();
		finalStates.add(1); //APAGA ESSA PORRA
		num_states = 7; //APAGA ESSA PORRA
	}
	
	public void addEdge (int s1, int s2, String w){
		if (auto[s1][s2] == null)
			auto[s1][s2] = new ArrayList<String>();
		
		auto[s1][s2].add(w);
	}
	
	public void removeEps () {
		boolean[][] closure = new boolean[num_states][num_states];
		
		//passo I
		System.out.println("Passo I");
		for (int i = 0; i < num_states; i++)
			closureOf(i, closure);
		
		//remove efetivamente as arestas "&"
		for (int i = 0; i < num_states; i++) {
			for (int j = 0; j < num_states; j++) {
				if (auto[i][j] != null) {
					for (int k = 0; k < auto[i][j].size(); k++)
						if (auto[i][j].get(k).equals("&"))
							auto[i][j].remove(k);					
				} //if
			} //for j
		} //for i
		noEps = true;
		
		//passo II
		System.out.println("Passo II");
		for (int X = 0; X < num_states; X++) {
			for (int A = 0; A < num_states; A++) {
				if (auto[A][X] != null) {
					for (int Y = 0; Y < num_states; Y++) {
						if (closure[X][Y]){
							for (int i = 0; i < auto[A][X].size(); i++) {
								if (auto[A][Y] == null)
									auto[A][Y] =  new ArrayList<String>();
								auto[A][Y].add(auto[A][X].get(i));
							} //for
						} //if
					} //for Y
				} //if
			} //for A
		} //for X
		
		
		//passo III
		System.out.println("Passo III");
		for (int X = 0; X < num_states; X++) {
			for (int Y = 0; Y < num_states; Y++) {
				if (closure[X][Y]) {
					for (int i = 0; i < num_states; i++) {
						if (auto[Y][i] != null) {
							for (int j = 0; j < auto[Y][i].size(); j++) {
								if (auto[X][i] == null)
									auto[X][i] = new ArrayList<String>();
								auto[X][i].add(auto[Y][i].get(j));
							} //for
						} //if
					} //for
				} //if
			} //for Y
		} //for X
		
		
		//passo IV
		System.out.println("Passo IV");
		for (int X = 0; X < num_states; X++) {
			if (!finalStates.contains(X)) {
				for (int Y = 0; Y < num_states; Y++) {
					if (closure[X][Y] && finalStates.contains(Y))
						finalStates.add(X);
				} //for Y				
			} //if
		} //for X
		
	} //removeEps
	
	public void closureOf (int node, boolean[][] closure) {
		
		closure[node][node] = true;
		
		for (int i = 0; i < num_states; i++) {
			if (auto[node][i] != null) {
				for (int k = 0; k < auto[node][i].size(); k++) {
					if (auto[node][i].get(k).equals("&") && !closure[i][i]) {
						closure[node][i] = true;
						closureOf(i, closure);
						
						for (int j = 0; j < num_states; j++) {
							if (closure[i][j])
								closure[node][j] = true;
						} //for j
					} //if
				} //for k
			} //if
		} //for i
		
		for(int i = 0; i < num_states; i++)
			closure[i][i] = false;
	} //closureOf
	
}
