import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class Auto {
	
	final int MAX_STATE = 100;
	

	final String FILE = "C:\\Users\\Ana Cuder\\workspace\\AutomaForster\\DFAGraph.txt";
	private String graph;
	
	private ArrayList<String>[][] auto;
	private Set<Integer> finalStates;
	
	private int startState;
	private int num_states;
	
	private int ini;
	private int fim;
	
	private boolean isAFD = false;
	private boolean noEps = false;
	private boolean onlySymbol = false;
	
	private Stack<Integer> vertex;
	private Stack<Integer> vertexIni;
	private boolean isUsed[];

	public Auto(){
		isUsed = new boolean[MAX_STATE];
		auto = new ArrayList[MAX_STATE][MAX_STATE];
		finalStates = new TreeSet<Integer>();
		
		for(int i = 0; i < MAX_STATE; i++){
			isUsed[i] = false;
			for(int j = 0; j < MAX_STATE; j++)
				auto[i][j] = new ArrayList<String>();
		}
		
		ini = 0;
		fim = 1;
		
		isUsed[ini] = true;
		isUsed[fim] = true;
		
		vertexIni = new Stack<Integer>();
		vertex = new Stack<Integer>();
		
		
	}
	
	public void addEdge (int s1, int s2, String w){
		if (auto[s1][s2] == null)
			auto[s1][s2] = new ArrayList<String>();
		
		auto[s1][s2].add(w);
	}
	
	public void addEdge (int s1, int s2, ArrayList<String> weights){
		auto[s1][s2] = weights;
	}
	
	public void removeEdge (int s1, int s2, String w){
		auto[s1][s2].remove(w);
	}
	
	public boolean hasConnection(int s1, int s2){
		return !auto[s1][s2].isEmpty();
	}
	
	
	public ArrayList<String> getWeights(int s1, int s2){
		return auto[s1][s2];
	}
	
	private void copyWeights(int from, int to){
		//Deep Copy dos elementos
		for(int i = 0; i < MAX_STATE; i++){
			for(int j = 0; j < auto[from][i].size(); j++){
				auto[to][i].add(auto[from][i].get(j));
			}
			auto[from][i] = new ArrayList<String>();
		}
	}
	
	//ITEM 1 ----------------------------------------------
	
	public void createNFAFromRE(String regex){
		ini = 0;
		fim = 1;
		int grafoIni, grafoFim;
		
		//vertexIni.push(auxini);
		//vertexFim.push(auxfim);
		addEdge(0,1,regex);
		processaRegex(regex, 0);
		
		grafoIni = vertex.pop();
		grafoFim = vertex.pop();
		
		addEdge(ini, grafoIni, "&");
		addEdge(grafoFim, fim, "&");
		
		auto[ini][fim] = new ArrayList<String>();
		
		generateGraphFile();
		
	}
	
	public void processaRegex(String substring, int op){
		String a,b;
		
		int auxfim;
		int auxini;
		int aIni, aFim, bIni, bFim;
		
		if(substring.length() == 1){
			int i = getIndex();
			setUsed(i);
			int j = getIndex();
			setUsed(j);
			addEdge(i,j,substring);
			vertex.push(j);
			vertex.push(i);
			return;
		}
		if (hasParenthesis(substring)){
			a = substring.substring(1, substring.length() - 1);
			processaRegex(a, 0);
		}
		else{
			if(isUnion(substring)){
				op = 1;
				//Quebra a string onde ha o +
				int plus = getUnionPos(substring);
				a = substring.substring(0, plus);
				b = substring.substring(plus+1, substring.length());
				
				processaUnion(a,b);
			}
			else{
				if(isStar(substring)){
					op = 3;
					//Altera a matriz para o fecho de Kleene
					if(substring.length() == 2){
						a = substring.substring(0,1);
						processaStar(a);
					}
						
					
					if(substring.length() > 2){
						a = substring.substring(1, substring.length()-2);
						processaStar(a);
					}
					
				}
				else{
					//Altera a matriz para o caso de concatenação
					op = 2;
					int counter = 0;
					int abre = -1, fecha = -1;
					char aux;
					ArrayList<String> weights = new ArrayList<String>();
					
					for(int i = 0; i < substring.length(); i++){
						aux = substring.charAt(i);
						if(aux == '('){
							if(counter == 0)
								abre = i;
							counter++;
						}
						if(aux == ')'){
							counter--;
							if(counter == 0)
								fecha = i;
						}
						if(abre >= 0){
							if(substring.charAt(i) == ')' && counter == 0){
								//Parenteses com estrela
								if(substring.length() != i + 1 && substring.charAt(i+1) == '*'){
									a = substring.substring(abre, fecha+2);
									b = substring.substring(fecha+2, substring.length());
									
									processaCat(a,b);
									
									break;
									//i = i+1; //Passar pela posição do asterisco
								}
								//Parenteses comuns
								else{
									a = substring.substring(abre+1, fecha);
									b = substring.substring(fecha+1, substring.length());
									
									processaCat(a,b);
									
									break;
								}
								
							}
						}
						else{
							//letra com estrela
							if(i + 1 != substring.length() && substring.charAt(i+1) == '*'){
								a = substring.substring(i, i+2);
								b = substring.substring(i+2, substring.length());
	
								processaCat(a,b);
								
								break;
							}
							//somente letra
							else{
								a = substring.substring(i,i+1);
								b = substring.substring(i+1,substring.length());
	
								processaCat(a,b);
								
								break;
								//Altera a matriz
								
							}
							
						}
							
					}
					
				}
			}
		}
		
	}
	
	private void processaUnion(String a, String b){
		int aIni, aFim, bIni, bFim, auxini, auxfim;
		int op = 1;
		
		this.processaRegex(a, op);
		aIni = vertex.pop();
		aFim = vertex.pop();
		this.processaRegex(b, op);
		bIni = vertex.pop();
		bFim = vertex.pop();
		
		auxini = getIndex();
		setUsed(auxini);
		auxfim = getIndex();
		setUsed(auxfim);
		
		if(auto[aIni][aFim].size() == 1 && auto[bIni][bFim].size() == 1){ 
			auto[aIni][aFim].add(auto[aIni][aFim].get(0) + ","+ auto[bIni][bFim].get(0));
			
			auto[aIni][aFim].remove(0);
			
			auto[bIni][bFim] = new ArrayList<String>();
			vertex.push(aFim);
			vertex.push(aIni);
		}
		else{
		
			addEdge(auxini, aIni, "&");
			addEdge(auxini, bIni, "&");
			addEdge(aFim, auxfim, "&");
			addEdge(bFim, auxfim, "&");
			
			vertex.push(auxfim);
			vertex.push(auxini);
		}
		
		
	}
	
	private void processaCat(String a, String b){
		int aIni, aFim, bIni, bFim;
		int op = 2;
		
		processaRegex(a, op);
		aIni = vertex.pop();
		aFim = vertex.pop();
		processaRegex(b, op);
		bIni = vertex.pop();
		bFim = vertex.pop();
		
		copyWeights(bIni, aFim);
		
		//addEdge(aFim, bFim, b);
		//removeEdge(bIni,bFim,b);
		
		vertex.push(bFim);
		vertex.push(aIni);
		
	}
	
	private void processaStar(String a){
		int aIni, aFim, auxini, auxfim;
		int op = 2;
		processaRegex(a, op);
		aIni = vertex.pop();
		aFim = vertex.pop();

		auxini = getIndex();
		setUsed(auxini);
		auxfim = getIndex();
		setUsed(auxfim);
		
		for(int i = 0; i < MAX_STATE; i++){
			for(int j = 0; j < auto[i][aFim].size(); j++){
				if(auto[i][aFim].size() != 0)
					auto[i][aIni].add(auto[i][aFim].get(j));
			}
			auto[i][aFim] = new ArrayList<String>();
		}
		
		addEdge(auxini, aIni, "&");
		addEdge(aIni, auxfim, "&");
		
		vertex.push(auxfim);
		vertex.push(auxini);
	}
	
	public boolean hasParenthesis(String substring){
		return substring.charAt(0) == '(' && substring.charAt(substring.length()-1) == ')'; 
	}
	
	public boolean isUnion(String substring){
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
	
	private int getUnionPos(String substring){
		int counter = 0;
		char aux;
		
		for(int i = 0; i < substring.length(); i++){
			aux = substring.charAt(i);
			if(aux == '(')
				counter++;
			if(aux == ')')
				counter--;
			if(aux == '+' && counter == 0)
				return i;
		}
		return -1;
	}
	
	public boolean isStar(String substring){
		int abre = 0;
		int fecha = 0;
		if(substring.length() == 2)
			if(substring.charAt(substring.length()-1) == '*')
				return true;
		if(substring.length() > 2){
			if(substring.charAt(substring.length()-1) == '*' &&
					substring.charAt(substring.length()-2) == ')' &&
					substring.charAt(0) == '('){
				substring = substring.substring(1, substring.length()-2);
				for(int i = 1; i < substring.length();i++){
					if(substring.charAt(i) == ')')
						fecha++;
					if(substring.charAt(i) == '(')
						abre++;
					if(fecha > abre)
						return false;
				}
				return true;
			}
		}
			
		return false;
	}
	
	public int getIndex(){
		for(int i = 0; i < MAX_STATE;i++){
			if(!isUsed[i]){
				return i;
			}
		}
		return -1;
	}
	
	//Marca o valor de estado como utilizado
	public void setUsed(int index){
		isUsed[index] = true;
	}
	
	public void generateGraphFile(){
		PrintWriter out;
		ArrayList<String> aux = new ArrayList<String>();
		
		try{
			out = new PrintWriter(FILE);
			out.println("digraph {");
	
			for(int i = 0; i < MAX_STATE; i++){
				for(int j = 0; j < MAX_STATE; j++){
					if(hasConnection(i,j)){
						aux = getWeights(i,j);
						int size = aux.size();
						for(int k = 0; k < size; k++){
							String weight = aux.get(k);
							out.println("\t"+i+" -> "+j+"[label =\""+weight+"\",weight=\""+weight+"\"];");
						}
					}
				}
			}
			
			out.println("}");
			out.close();
		}catch(Exception e){}
		
		
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