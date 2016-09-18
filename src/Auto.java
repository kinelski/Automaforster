import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;
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
		auto[s1][s2].add(w);
	}
	
	//ITEM 1 ----------------------------------------------
	
	public void createNFAFromRE(String regex){
		ini = 0;
		fim = 1;
		
		//vertexIni.push(auxini);
		//vertexFim.push(auxfim);
		addEdge(0,1,regex);
		processaRegex(regex, 0);
	}
	
	public void processaRegex(String substring, int op){
		String a,b;
		
		int auxfim;
		int auxini;
		
		
		/*if(substring.length() == 1 || (substring.length() == 2 && op == 3)){
			switch(op){
				//Union
				case 1:
					addEdge(auxini,auxfim, substring);
					//auto[auxini][auxfim].add(substring);
					break;
				//Concatenation
				case 2:
					auxfim++;
					
					auto[auxini][auxfim].add(substring);
					auxfim++;
				
				return;
			}
		}
		*/
		
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
		
		if(isUnion(substring)){
			op = 1;
			//Quebra a string onde ha o +
			int plus = getUnionPos(substring);
			a = substring.substring(0, plus);
			b = substring.substring(plus+1, substring.length());
			
			//Altera Matriz
			//auto[auxini][auxfim].add(a);
			//auto[auxini][auxfim].add(b);
			
			//vertexIni.push(auxini);
			//vertexFim.push(auxfim);
			this.processaRegex(a, 1);
			int aIni = vertex.pop();
			int aFim = vertex.pop();
			this.processaRegex(b, 1);
			int bIni = vertex.pop();
			int bFim = vertex.pop();
			
			auxini = getIndex();
			setUsed(auxini);
			auxfim = getIndex();
			setUsed(auxfim);
			
			addEdge(auxini, aIni, "&");
			addEdge(auxini, bIni, "&");
			addEdge(aFim, auxfim, "&");
			addEdge(bFim, auxfim, "&");
			
			vertex.push(auxfim);
			vertex.push(auxini);
			
			
		}
		else{
			if(isStar(substring)){
				op = 3;
				//Altera a matriz para o fecho de Kleene
				if(substring.length() == 2){}
					processaRegex(substring, op);
					//Alterar a matriz
				
				if(substring.length() > 2){
					a = substring.substring(1, substring.length()-2);
					processaRegex(a, op);
				}
			}
			else{
				//Altera a matriz para o caso de concatenação
				op = 2;
				int counter = 0;
				int abre = 0, fecha = 0;
				char aux;
				
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
					if(substring.charAt(i) == ')' && counter == 0){
						//Parenteses com estrela
						if(substring.charAt(i+1) == '*'){
							a = substring.substring(abre, fecha+2);
							b = substring.substring(abre+2, substring.length());
							processaRegex(a, op);
							processaRegex(b, op);
							i = i+1; //Passar pela posição do asterisco
						}
						//Parenteses comuns
						else{
							a = substring.substring(abre, fecha+1);
							processaRegex(a, op);
						}
					}
					else{
						//letra com estrela
						if(i + 1 != substring.length() && substring.charAt(i+1) == '*'){
							a = substring.substring(i, i+2);
							processaRegex(a, op);
						}
						//somente letra
						else{
							a = substring.substring(i,i+1);
							processaRegex(a, 2);
							//Altera a matriz
							
						}
						
					}
						
				}
			}
		}
		
	}
	
	private void processaUnion(String regex){
		
	}
	
	private void processaCat(String regex){
		
	}
	
	private void processaStar(String regex){
		
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
	
	public void setUsed(int index){
		isUsed[index] = true;
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
	
}
