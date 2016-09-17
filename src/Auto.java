import java.util.ArrayList;

public class Auto {
	
	final int MAX_STATE = 100;
	
	private ArrayList<String>[][] auto;
	
	private boolean isAFD = false;
	private boolean noEps = false;
	private int ini;
	private int fim;
	
	public Auto(){
		auto = new ArrayList[MAX_STATE][MAX_STATE];
		for(int i = 0; i < 100; i++)
			for(int j = 0; j < 100; j++){
				auto[i][j] = new ArrayList<String>();
			}
	}
	
	private void addEdge (int s1, int s2, String w){
		auto[s1][s2].add(w);
	}
	
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
	
}
